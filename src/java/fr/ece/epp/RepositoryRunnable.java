/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.ece.epp;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 *
 * @author xuanzhaopeng
 */
public class RepositoryRunnable implements Runnable  {
     private String batPath = "D:\\pfe\\osgi_platform_web\\";
     private String repository;
     private PrintWriter out;
     
   public RepositoryRunnable(String repository){
       this.repository = repository;
   }
     
    @Override
    public void run() {
        this.WriteStringToFile5(batPath+"url.txt", repository);
        Runtime rt = Runtime.getRuntime();
        try {
            Process pr = rt.exec(batPath + "run.bat");
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    pr.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            RepositoryServlet.threadCount--;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            RepositoryServlet.threadCount--;
            e.printStackTrace();
        }
    }
    
    public void WriteStringToFile5(String filePath,String content) {  
        try {  
            FileOutputStream fos = new FileOutputStream(filePath);  
            String s = content;  
            fos.write(s.getBytes());  
            fos.close();  
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }
    
}

