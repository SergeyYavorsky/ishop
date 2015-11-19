/**
 * Created by Just-a-man on 11/13/2015.
 */
import mypkg.DataManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ActionServlet extends HttpServlet {
    Connection conn;
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        DataManager dm = new DataManager();
        dm.setDbURL(conf.getInitParameter("dbURL"));
        dm.setDbUserName(conf.getInitParameter("dbUserName"));
        dm.setDbPassword(conf.getInitParameter("dbPassword"));
        conn = dm.getConnection();
    }
    public void doGet (HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
    }

    private String implode(ArrayList<String> data) {
        StringBuilder sb = new StringBuilder();
        for (String temp:data) {
            sb.append(temp);
            sb.append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        return sb.toString();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder("{\"elements\": [{\"type\": \"bar_glass\", \"values\" : [");
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select\n" +
                    "  t.name,\n" +
                    "  round(t.summa) summa,\n" +
                    "  round(t.prcnt) prcnt,\n" +
                    "  to_char(round(t.total), 'fm999G999G999G999', 'NLS_NUMERIC_CHARACTERS='', ''') total\n" +
                    "from\n" +
                    "  ord_retail_shop_ord_rate_vw t\n" +
                    "where\n" +
                    "  t.page_num = 1\n" +
                    "order by 2");
            ArrayList<String> summas = new ArrayList<String>();
            ArrayList<String> names = new ArrayList<String>();
            ArrayList<String> tags = new ArrayList<String>();
            int i = 0;
            while (rs.next()) {
                summas.add(rs.getString("SUMMA"));
                names.add("\""+rs.getString("NAME")+"\"");
                i++;
                tags.add("{\"x\":" + Integer.toString(i) + ",\"y\":"+rs.getString("SUMMA")+",\"text\":\""+rs.getString("PRCNT")+"%\"}");
            }
            sb.append(implode(summas));
            sb.append("],\"on-show\":{\"type\":\"pop\",\"cascade\":1,\"delay\":0.5}},{\"type\":\"tags\",\"values\":[");
            sb.append(implode(tags));
        }
        catch (SQLException e) {
            System.out.println("Could not insert order: " + e.getMessage());
        }
        response.getWriter().write(sb.toString());
    }
}
