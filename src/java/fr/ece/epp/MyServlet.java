/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ece.epp;

import fr.ece.epp.tools.Utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author xuanzhaopeng
 */
public class MyServlet extends HttpServlet {

    private String mvnCommand = "\\build\\install.bat";

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

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
        System.out.println("[Install]");
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec(path + "/" + name + "/install.bat");
        //Process pr = rt.exec("ping 192.168.1.9 -n 20");
        BufferedReader br = new BufferedReader(new InputStreamReader(
                pr.getInputStream()));
        String line = null;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
            out.println("<h4>" + line + "</h4>");
            out.flush();
        }
        //Step 6 save to db

        out.close();
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public void modifyPom(String node) {
    }
}
