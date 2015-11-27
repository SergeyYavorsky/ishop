package mypkg;

import javax.servlet.ServletContext;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.*;
import java.util.ArrayList;

public class DataManager {
    Connection conn = null;
    private String dbURL = "";
    private String dbUserName = "";
    private String dbPassword = "";

    public DataManager(String dbURL, String dbUserName, String dbPassword) {
        this.dbURL = dbURL;
        this.dbUserName = dbUserName;
        this.dbPassword = dbPassword;

    }

    private void initConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(getDbURL(), getDbUserName(), getDbPassword());
            Statement stmt = conn.createStatement();
            stmt.execute("ALTER SESSION SET NLS_LANGUAGE= 'AMERICAN' NLS_TERRITORY= 'AMERICA' NLS_DATE_FORMAT='DD/MM/RRRR'");
        } catch (Exception e) {
            System.out.println("Could not connect to DB: " + e.getMessage());
        }
    }

    public String getDbURL() {
        return dbURL;
    }

    public void setDbURL(String dbURL) {
        this.dbURL = dbURL;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    private String implode(ArrayList<String> data) {
        StringBuilder sb = new StringBuilder();
        for (String temp : data) {
            sb.append(temp);
            sb.append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        return sb.toString();
    }

    public String getData(int pageNum) {
        if (conn == null) {
            initConnection();
        }
        StringBuilder sb = new StringBuilder("{\"elements\": [{\"type\": \"bar_glass\", \"values\" : [");
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select cast(to_char(add_months(sysdate,mod(" + pageNum + ", 2)-1), 'fmMonth yyyy','NLS_DATE_LANGUAGE=RUSSIAN') as varchar2(30)) DAT from dual");
            rs.next();
            String dat = rs.getString("DAT");
            rs.close();
            StringBuilder title = new StringBuilder();
            String viewName;
            if (pageNum == 1 || pageNum == 2) {
                title.append("Заказы за ");
                viewName = "ord_retail_shop_ord_rate_vw";
            } else {
                title.append("Отгрузки за ");
                viewName = "ord_retail_shop_ttn_rate_vw";
            }
            title.append(dat);
            title.append(". Всего ");
            rs = stmt.executeQuery("select\n" +
                "  t.name,\n" +
                "  round(t.summa) summa,\n" +
                "  round(t.prcnt) prcnt,\n" +
                "  to_char(round(t.total), 'fm999G999G999G999', 'NLS_NUMERIC_CHARACTERS='', ''') total\n" +
                "from\n" +
                viewName + " t\n" +
                "where\n" +
                "  t.page_num = " + pageNum + "\n" +
                "order by 2");

            ArrayList<String> summas = new ArrayList<String>();
            ArrayList<String> names = new ArrayList<String>();
            ArrayList<String> tags = new ArrayList<String>();
            int i = -1;
            int maxSumma = 0;
            String total = "";
            while (rs.next()) {
                summas.add(rs.getString("SUMMA"));
                if (maxSumma < rs.getInt("SUMMA")) {
                    maxSumma = rs.getInt("SUMMA");
                }
                names.add("\"" + rs.getString("NAME") + "\"");
                i++;
                tags.add("{\"x\":" + Integer.toString(i) + ",\"y\":" + rs.getString("SUMMA") + ",\"text\":\"" + rs.getString("PRCNT") + "%\"}");
                if (total == "") {
                    total = rs.getString("TOTAL");
                }
            }
            rs.close();
            stmt.close();
            title.append(total);
            title.append(" руб.");
            sb.append(implode(summas));
            sb.append("],\"on-show\":{\"type\":\"pop\",\"cascade\":1,\"delay\":0.5}},{\"type\":\"tags\",\"values\":[");
            sb.append(implode(tags));
            sb.append("],\"font\":\"Verdana\",\"font-size\":12,\"colour\":\"#000000\",\"align-x\":\"center\"}],\"x_axis\":{\n" +
                    "    \"labels\": { \"size\": 14, \"labels\": [");
            sb.append(implode(names));
            int mult = (int) Math.pow(10, ("" + maxSumma).length() - 1);
            sb.append("]}\n" +
                    "  }, \"y_axis\": { \"min\": 0, \"max\": ");
            sb.append("" + (Math.ceil(maxSumma / (double) mult) * mult));

            sb.append(",\"steps\":" + mult + " },\"myTitle\":\"" + title.toString() + "\"}");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return sb.toString();
    }

    public String getTable(int tableNum) {
        String ret = "";
        if (conn == null) {
            initConnection();
        }
        try {

            Statement stmt = conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("select '<t>' || to_clob(" +
                    "xmlconcat(\n" +
                    "insertchildxml(xmltype('<table1/>'), '/table1', 'ROWSET',\n" +
                    "dbms_xmlgen.getXMLType('select\n" +
                    "  t.day, t.cnt_c, t.cnt_missed, t.cnt_o, (t.sum_o) sum_o, t.cnt_t, (t.sum_t) sum_t \n" +
                    "from\n" +
                    "  ord_retail_shop_day_rate_vw t\n" +
                    "order by 1')),\n" +
                    "insertchildxml(xmltype('<table2/>'), '/table2', 'ROWSET',\n" +
                    "dbms_xmlgen.getXMLType('select\n" +
                    "    nvl((select target from plan_ishop_target t where t.plan_month = trunc(sysdate,''month'')), 10000000) target,\n" +
                    "    to_number(to_char(sysdate, ''fmDD'')) worked_days,\n" +
                    "    to_number(to_char(last_day(sysdate), ''fmDD'')) days_in_month\n" +
                    "    from dual'))\n" +
                    "    )) || '</t>' val from dual");

            rs.next();
            ret = rs.getString("VAL");
            //System.out.println(ret);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }


        return ret;
    }
}
