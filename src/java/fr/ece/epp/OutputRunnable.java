/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ece.epp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xuanzhaopeng
 */
public class OutputRunnable implements Runnable {

    private PrintWriter out;
    private BufferedReader br;

    public OutputRunnable(PrintWriter out, BufferedReader br) {
        this.out = out;
        this.br = br;
    }

    @Override
    public void run() {
        try {
            String line = "";
            out.println("<h1>Child:" + Thread.currentThread().getId() + "</h1>");
            out.flush();
            
            while ((line = br.readLine()) != null) {
                //out.println("<h1>" + line + "</h1>");
                //out.flush();
                
            }
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(OutputRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
