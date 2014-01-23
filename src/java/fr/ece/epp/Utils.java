/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ece.epp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.json.JSONArray;
import org.json.JSONException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author xuanzhaopeng
 */
public class Utils {

    public static void createFolder(String path, String name) {
        new File(path + "\\" + name).mkdir();
        System.out.println("Create folder :" + path + "\\" + name);
    }

    public static void copy(File resFile, File objFolderFile) throws IOException {
        if (!resFile.exists()) {
            return;
        }
        if (!objFolderFile.exists()) {
            objFolderFile.mkdirs();
        }
        if (resFile.isFile()) {
            File objFile = new File(objFolderFile.getPath() + File.separator + resFile.getName());
            InputStream ins = new FileInputStream(resFile);
            FileOutputStream outs = new FileOutputStream(objFile);
            byte[] buffer = new byte[1024 * 512];
            int length;
            while ((length = ins.read(buffer)) != -1) {
                outs.write(buffer, 0, length);
            }
            ins.close();
            outs.flush();
            outs.close();
        } else {
            String objFolder = objFolderFile.getPath() + File.separator + resFile.getName();
            File _objFolderFile = new File(objFolder);
            _objFolderFile.mkdirs();
            for (File sf : resFile.listFiles()) {
                copy(sf, new File(objFolder));
            }
        }
    }

    public static void updatePom(String path, String[] repo, boolean outOrno) {
        Document document = null;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(path);
            Element root = document.getDocumentElement();
            Node repositories =  root.getElementsByTagName("repositories").item(0);
            for(int i = 0;i<repo.length;i++){
                Element repository =document.createElement("repository");
                
                Element id = document.createElement("id");
                id.appendChild(document.createTextNode("repository" + i));
                repository.appendChild(id);
                
                Element layout = document.createElement("layout");
                layout.appendChild(document.createTextNode("p2"));
                repository.appendChild(layout);
                
                Element url = document.createElement("url");
                url.appendChild(document.createTextNode(repo[i]));

                repository.appendChild(url);
                repositories.appendChild(repository);
            }
            output(root, path);
            if (outOrno) {
                output(root, null);
            }

        } catch (SAXException e) {
        } catch (IOException e) {
        } catch (ParserConfigurationException e) {
        }
    }
    
     public static void updateProduct(String path, String[] feature, boolean outOrno) {
        Document document = null;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(path);
            Element root = document.getDocumentElement();
            Node features =  root.getElementsByTagName("features").item(0);
            for(int i = 0;i<feature.length;i++){
                Element fea =document.createElement("feature");
       
                    if(feature[i].endsWith(".feature.group")){
                        int count = feature[i].length() - ".featyre.group".length();
                        String id = feature[i].substring(0, count);
                        fea.setAttribute("id", id);
                    }else{
                         fea.setAttribute("id", feature[i]);
                    }
                
                features.appendChild(fea);
            }
            output(root, path);
            if (outOrno) {
                output(root, null);
            }

        } catch (SAXException e) {
        } catch (IOException e) {
        } catch (ParserConfigurationException e) {
        }
    }

    public static void output(Node node, String filename) {
        TransformerFactory transFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty("encoding", "utf8");
            transformer.setOutputProperty("indent", "yes");
            DOMSource source = new DOMSource();
            source.setNode(node);
            StreamResult result = new StreamResult();
            if (filename == null) {
                result.setOutputStream(System.out);
            } else {
                result.setOutputStream(new FileOutputStream(filename));
            }
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static String foundZip(String path){
         System.out.println("path:" + path);
         File directory = new File(path);
         File[] files = directory.listFiles();
         for(File file : files){
             if(file.getName().endsWith(".zip")){
                 return file.getName();
             }
         }
         return "";
    }
    
    public static void writeBat(String path,String version) {
        try {
            File writename = new File(path);
            writename.createNewFile(); 
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write("%~d0\n\r");
            out.write("cd %~dp0\n\r");
            out.write("mvn install -P base,"+version);
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件  
        } catch (IOException ex) {
        }
    }
}
