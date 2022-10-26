package servlets;

import DecryptionManager.DM;
import WebConstants.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.DMManager;
import users.ReadyManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "CreateTasksServlet", urlPatterns = {"/createTasksServlet"})
public class CreateTasksServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uBoatName = request.getParameter(Constants.UBOAT_NAME);
        String allyNameFromSession = SessionUtils.getUsername(request);
        DMManager dmManager = ServletUtils.getBattlefield(getServletContext(),uBoatName).getDmManager();
        DM curDM = dmManager.getDM(allyNameFromSession);
        ReadyManager readyManager = ServletUtils.getBattlefield(getServletContext(),uBoatName).getReadyManager();
        curDM.setSentenceToCheck(readyManager.getuBoatAndSentence().getValue());

        curDM.run();
        int x = 1;
    }
}
