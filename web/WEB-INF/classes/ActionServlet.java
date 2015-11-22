/**
 * Created by Just-a-man on 11/13/2015.
 */
import mypkg.DataManager;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ActionServlet extends HttpServlet {
    DataManager dm;
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        dm = new DataManager(conf.getInitParameter("dbURL"),conf.getInitParameter("dbUserName"),conf.getInitParameter("dbPassword"));
    }

    public void doGet (HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
    }



    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.setCharacterEncoding("UTF-8");
        int pageNum;
        try {
            pageNum = Integer.parseInt(request.getParameter("pageNum"));
        } catch (NumberFormatException nfe) {
            pageNum = 1;
        }
        response.getWriter().write(dm.getData(pageNum));
    }
}
