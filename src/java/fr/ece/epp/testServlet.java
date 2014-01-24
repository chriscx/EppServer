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
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author xuanzhaopeng
 */
public class testServlet extends HttpServlet {

    public static final int CALLBACK_TIMEOUT = 900000; // ms
    /**
     * executor service
     */
    private ExecutorService exec;

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);
        int size = 10;
        exec = Executors.newFixedThreadPool(size);
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.addHeader("Access-Control-Allow-Origin", "*");
        req.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
        final AsyncContext ctx = req.startAsync();
        final HttpSession session = req.getSession();
        Enumeration<String> sname = req.getSession().getAttributeNames();
        if(sname.hasMoreElements()){
            res.getWriter().println(sname.nextElement());
        }
        // set the timeout
        ctx.setTimeout(CALLBACK_TIMEOUT);

        // attach listener to respond to lifecycle events of this AsyncContext
        ctx.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {

            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {

            }

            @Override
            public void onError(AsyncEvent event) throws IOException {

            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {

            }
        });

        //enqueLongRunningTask(ctx, session);
    }

    /**
     * if something goes wrong in the task, it simply causes timeout condition
     * that causes the async context listener to be invoked (after the fact)
     * <p/>
     * if the {@link AsyncContext#getResponse()} is null, that means this
     * context has already timed out (and context listener has been invoked).
     */
    private void enqueLongRunningTask(final AsyncContext ctx, final HttpSession session) {

        exec.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ServletResponse response = ctx.getResponse();
                    if (response != null) {
                        Runtime rt = Runtime.getRuntime();
                        Process pr = rt.exec("ping 192.168.1.9 -n 5");
                        
                        BufferedReader br = new BufferedReader(new InputStreamReader(
                                pr.getInputStream()));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            response.getWriter().write(MessageFormat.format("Processing task in bgt_id:[{0},{1}]",
                                Thread.currentThread().getId(),line));
                            response.getWriter().flush();
                        }
                        response.getWriter().close();
                        ctx.complete();
                    } else {
                        throw new IllegalStateException(); // this is caught below
                    }
                } catch (IllegalStateException ex) {
                    System.out.println(ex.getMessage());
                } catch (Exception e) {
                }
            }
        });
    }

    /**
     * destroy the executor
     */
    @Override
    public void destroy() {

        exec.shutdown();
    }

}
