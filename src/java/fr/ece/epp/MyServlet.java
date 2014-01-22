/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ece.epp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author xuanzhaopeng
 */
public class MyServlet extends HttpServlet {

    @Override
    public void service(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("service 被调用" + Thread.currentThread().getId());
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        /* TODO output your page here. You may use following sample code. */
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("ping 192.168.1.9 -n 25");

        out.println("<h1>Parent:" + Thread.currentThread().getId() + "</h1>");
        BufferedReader br = new BufferedReader(new InputStreamReader(
                pr.getInputStream()));
        String line = "";
        out.println("<h1>Child:" + Thread.currentThread().getId() + "</h1>");
        out.flush();

        while ((line = br.readLine()) != null) {
                out.println("<h1>" + line + "</h1>");
                out.flush();
        }
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

}
