package servlets;

import EnginePackage.EngineCapabilities;
import EnginePackage.EnigmaEngine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.scene.control.Alert;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "EngineServlet", urlPatterns = {"/EngineServlet"}) // TODO: Correct
public class EngineServlet  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //loadFileRequest(request, response);
    }

    protected void loadFileRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        EngineCapabilities engine = ServletUtils.getEngine(getServletContext());
        String fileSelected = request.getParameter("filePath"); // TODO: constant
        engine.createEnigmaMachineFromXML(fileSelected, true);

        // TODO: construct a response
    }
}
