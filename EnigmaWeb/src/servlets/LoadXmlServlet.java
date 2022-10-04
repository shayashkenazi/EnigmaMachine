package servlets;

import EnginePackage.EngineCapabilities;
import enigmaException.EnigmaException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.ServletUtils;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Scanner;

@WebServlet(name = "EngineServlet", urlPatterns = {"/LoadXmlServlet"}) // TODO: Correct
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class LoadXmlServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws  ServletException, IOException, FileNotFoundException{
        response.setContentType("text/plain");

        Collection<Part> parts = request.getParts();
        Part part;

        StringBuilder fileContent = new StringBuilder();
        try {
            for (Part curPart : parts) {
                part = curPart;
                EngineCapabilities engine = ServletUtils.getEngine(getServletContext());
                engine.createEnigmaMachineFromXMLInputStream(part.getInputStream(), true);
                break;
            }
        }
        catch (EnigmaException e)
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
        catch (JAXBException e)
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }

    }

    private void printPart(Part part, PrintWriter out) {
        StringBuilder sb = new StringBuilder();
        sb
                .append("Parameter Name: ").append(part.getName()).append("\n")
                .append("Content Type (of the file): ").append(part.getContentType()).append("\n")
                .append("Size (of the file): ").append(part.getSize()).append("\n")
                .append("Part Headers:").append("\n");

        for (String header : part.getHeaderNames()) {
            sb.append(header).append(" : ").append(part.getHeader(header)).append("\n");
        }

        out.println(sb.toString());
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

   /* private void printFileContent(String content, PrintWriter out) {
        out.println("File content:");
        out.println(content);
    }*/

    /*    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileNotFoundException {
        try{
            System.out.println("aahahahaha");
            loadFileRequest(request, response);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (EnigmaException e)
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
        catch (JAXBException e)
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
        //loadFileRequest(request, response);
    }*/

    protected void loadFileRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, EnigmaException, JAXBException, FileNotFoundException {

        //String fileSelected = request.getParameter("filePath"); // TODO: constant
        //engine.createEnigmaMachineFromXMLInputStream(fileSelected, true);

    }
}
