import mypkg.DataManager;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


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
        response.setCharacterEncoding("UTF-8");
        int pageNum;
        try {
            pageNum = Integer.parseInt(request.getParameter("pageNum"));
        } catch (NumberFormatException nfe) {
            pageNum = 1;
        }
        if (pageNum < 5) {
            response.getWriter().write(dm.getData(pageNum));
        } else {
            StringBuilder sb = new StringBuilder();
            String tableData1 = dm.getTable(1);
            //response.getWriter().write(tableData1);

            try {
                StringReader reader = new StringReader(tableData1);
                StringWriter writer = new StringWriter();
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(getServletContext().getRealPath("/WEB-INF/classes/table1.xsl")));
                transformer.transform(new StreamSource(reader), new StreamResult(writer));
                //response.getWriter().write(writer.toString());
                sb.append(writer.toString());
            }
            catch (TransformerException e) {

            }
            response.getWriter().write(sb.toString());
            //response.getWriter().write(getServletContext().getRealPath("/WEB-INF/classes/table1.xsl"));
            //response.getWriter().write(dm.getTable());
        }
    }
}
