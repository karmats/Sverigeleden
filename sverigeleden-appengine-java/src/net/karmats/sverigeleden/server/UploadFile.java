package net.karmats.sverigeleden.server;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadFile extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader br = req.getReader();
        StringBuilder xmlString = new StringBuilder();

        boolean inXmlString = false;
        String next = null;
        while ((next = br.readLine()) != null) {
            if (next.startsWith("<?xml")) {
                inXmlString = true;
            }
            if (inXmlString && !next.startsWith("----")) {
                xmlString.append(next + "\n");
            }
        }
        if (xmlString.toString().isEmpty()) {
            resp.getWriter().write("The file couldn't be parsed, be sure it's an gpx or kml file.");
            return;
        } else {
            resp.setContentLength(xmlString.toString().length());
            resp.setContentType("text/html");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(xmlString.toString());
            resp.getWriter().flush();
            return;
        }
    }

}
