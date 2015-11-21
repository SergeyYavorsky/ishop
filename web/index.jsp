<%--
  Created by IntelliJ IDEA.
  User: Just-a-man
  Date: 11/13/2015
  Time: 9:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html; charset=UTF-8" %>
<html>
  <head>
    <title></title>
    <script src="http://code.jquery.com/jquery-latest.js">
    </script>
    <script>
      $(document).ready(function() {
        $('#submit').click(function(event) {
          var username=$('#user').val();
          $.get('ActionServlet',null,function(responseText) {
            $('#welcometext').text(responseText);
          });
        });
      });
    </script>
  </head>
  <body>
  <form id="form1">
    <h1>AJAX Demo using Jquery in JSP and Servlet</h1>
    <input type="button" id="submit" value="Ajax Submit"/>
    <div id="welcometext"></div>
  </form>
  </body>
</html>
