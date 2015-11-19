/**
 * Created by Just-a-man on 11/13/2015.
 */
import mypkg.DataManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
            while (rs.next()) {
                sb.append(rs.getString("SUMMA"));
            }
        }
        catch (SQLException e) {
            System.out.println("Could not insert order: " + e.getMessage());
        }
        response.getWriter().write(sb.toString());
    }
}
