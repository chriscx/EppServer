<%-- 
    Document   : buildepp
    Created on : 23 janv. 2014, 00:57:10
    Author     : xuanzhaopeng
--%>

<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="fr.ece.epp.Utils" %>

<%
    response.setContentType("text/html;charset=UTF-8");

    String strFeature = request.getParameter("feature");
    String strRepo = request.getParameter("repo");

    String[] feature = strFeature.split(",");
    String[] repo = strRepo.split(",");

    System.out.println("service is called by " + Thread.currentThread().getId());
    String path = request.getServletContext().getRealPath("/build");
    String name = request.getSession().getId();

    //Step  1  create folder
    System.out.println("[Create folder]");
    Utils.createFolder(path, name);
    //Step 2 create pom
    System.out.println("[Copy pom]");
    Utils.copy(new File(path + "/pom.xml"), new File(path + "/" + name));
    System.out.println("[Modify pom]");
    Utils.updatePom(path + "/" + name + "/pom.xml", repo, true);
    //Step 3 create product file
    System.out.println("[Copy product]");
    Utils.copy(new File(path + "/eclipseplusplus.product"), new File(path + "/" + name));
    System.out.println("[Modify product]");
    Utils.updateProduct(path + "/" + name + "/eclipseplusplus.product", feature, true);
    //Step 4 copy install and modify
    System.out.println("[Copy Install]");
    Utils.copy(new File(path + "/install.bat"), new File(path + "/" + name));

    //Step 5 install
    //Step 6 save to db

%>

<!DOCTYPE html>
<html lang="en"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>Eclipse build platform</title>

        <!-- Bootstrap core CSS -->
        <link href="./css/bootstrap.css" rel="stylesheet">

        <!-- Custom styles for this template -->
        <link href="./css/jumbotron.css" rel="stylesheet">
        <script src="./js/jquery-1.10.2.min.js"></script>
        <script src="./js/bootstrap.min.js"></script>
        <script>
            $(document).ready(function() {              
                var value ="./download?id="+"<%=name%>";
                $("#downloadbtn").attr("href", value);
                $("#downloadbtn").html("Download ");
            });

        </script>

        <!-- Just for debugging purposes. Don't actually copy this line! -->
        <!--[if lt IE 9]><script src="../../docs-assets/js/ie8-responsive-file-warning.js"></script><![endif]-->

        <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
          <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
        <![endif]-->
    </head>

    <body style="">

        <!-- Main jumbotron for a primary marketing message or call to action -->
        <div class="jumbotron">
            <div class="container">
                <h1>Eclipse++ by ECE Paris & UPMC Lib6</h1>
                <p>Someone should write description here</p>
                <p><a id="downloadbtn" name="downloadbtn" class="btn btn-primary btn-lg" role="button">Building ... </a></p>
            </div>
        </div>

        <div class="container">
            <!-- Example row of columns -->
            <div class="row">
                <%                    System.out.println("[Install]");
                    Runtime rt = Runtime.getRuntime();
                    Process pr = rt.exec(path + "/" + name + "/install.bat");
                    //Process pr = rt.exec("ping 192.168.1.9 -n 20");
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            pr.getInputStream()));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                        out.println("<h5>" + line + "</h5>");
                        out.flush();
                    }
                %>
            </div>

            <hr>

            <footer>
                <p>© Company 2013</p>
            </footer>
        </div> <!-- /container -->


        <!-- Bootstrap core JavaScript
        ================================================== -->
        <!-- Placed at the end of the document so the pages load faster -->
        


    </body>
</html>