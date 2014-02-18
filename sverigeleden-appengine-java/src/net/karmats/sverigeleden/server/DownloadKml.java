package net.karmats.sverigeleden.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadKml extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String s = req.getParameter("kmlData");
        resp.setContentType("application/vnd.google-earth.kml+xml");
        resp.setHeader("Content-Disposition", "inline; filename=" + "sveled.kml");
        PrintWriter writer = resp.getWriter();
        writer.write(s);
        writer.flush();
        writer.close();
    }

}
