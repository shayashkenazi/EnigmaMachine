package servlets;

import DecryptionManager.DM;
import DecryptionManager.Difficulty;
import EnginePackage.Battlefield;
import EnginePackage.EngineCapabilities;
import WebConstants.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.HierarchyManager;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "AllyDMServlet", urlPatterns = {"/allyDMServlet"})
public class AllyDMServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uBoatName = request.getParameter(Constants.UBOAT_NAME);
        String taskSize = request.getParameter(Constants.TASK_SIZE);

        Battlefield battlefield = ServletUtils.getBattlefield(getServletContext(), uBoatName);

        DM dm = new DM(battlefield.getEngine(), battlefield.getLevel(), Integer.parseInt(taskSize));
        request.getSession(false).setAttribute(Constants.DM, dm); //TODO CHECK IF TRUE AT THE GET SESSION
        response.setStatus(HttpServletResponse.SC_OK);

    }

}
