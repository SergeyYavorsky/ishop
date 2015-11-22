<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Интернет-магазин</title>
    <link type="text/css" href="css/lightness/jquery-ui.min.css" rel="stylesheet"/>
    <link href="css/ishop-sh.css" type="text/css" rel="stylesheet" media="all"/>
    <script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="js/swfobject.js"></script>
    <script type="text/javascript" src="js/json2.js"></script>

    <script>
        var data;
        var pageNum = 1;



        function open_flash_chart_data() {
            return JSON.stringify(data);
        }

        function findSWF(movieName) {
            if (navigator.appName.indexOf("Microsoft") != -1) {
                return window[movieName];
            } else {
                return document[movieName];
            }
        }


        $(document).ready(function () {

            function setTitle(myTitle) {
                $("#header div").hide("blind", "slow");
                $("#header h1").html(myTitle);
                $("#header div").show("blind", "slow");
            }


            function setData(pageNum) {
                $.ajax({
                    type: 'POST',
                    url: 'ActionServlet',
                    data: 'pageNum=' + pageNum,
                    success: function (d) {
                        data = d;
                        tmp = findSWF("my_chart");
                        x = tmp.load(JSON.stringify(data));
                        setTitle(d['myTitle']);
                    },
                    error: function () {
                        data = null;
                    },
                    dataType: 'json',
                    async: false
                });
            };

            setData(1);
            
            setInterval(function() {
                if (pageNum < 4) {
                    pageNum++;
                } else {
                    pageNum = 1;
                }
                setData(pageNum);
            }, 10000);


        });
    </script>
</head>
<body>
<div id="header" class="ui-widget-header ui-corner-all">
    <img src="img/logo.png"/>

    <div class="ui-widget-header ui-corner-all" style="text-align:center;"><h1>Интернет-магазин</h1></div>
</div>
<div id="frame">
    <!--div id="my_chart_div"></div-->
    <object id="my_chart" width="1000" height="500" type="application/x-shockwave-flash" data="js/open-flash-chart.swf" style="visibility: visible;">
        <param name="flashvars" value="data-file=ActionServlet">
    </object>
    <div id="my_table"></div>
</div>
</div>
<div id="footer" class="ui-widget-header ui-corner-all">OAO MK Shatura <fmt:formatDate value="<%=new java.util.Date()%>"
                                                                                       pattern="yyyy"/>
    <div style="float:right">Страница обновлена <fmt:formatDate value="<%=new java.util.Date()%>"
                                                                pattern="yyyy-MM-dd HH:mm:ss"/></div>
</div>
</body>
</html>
