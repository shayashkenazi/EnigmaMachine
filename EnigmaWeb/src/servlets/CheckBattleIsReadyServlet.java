package servlets;

import WebConstants.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.HierarchyManager;
import users.ReadyManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "CheckBattleIsReadyServlet", urlPatterns = {"/checkBattleIsReadyServlet"})
public class CheckBattleIsReadyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userNameFromSession = SessionUtils.getUsername(request);
        HierarchyManager hierarchyManager = ServletUtils.getHierarchyManager(getServletContext());
        String typeClass = request.getParameter(Constants.CLASS_TYPE);
        String uBoatName = "";
        switch (typeClass){
            case Constants.ALLIES_CLASS:
                uBoatName = hierarchyManager.getParent(userNameFromSession);
                break;
            case Constants.UBOAT_CLASS:
                uBoatName = userNameFromSession;
                break;
            case Constants.AGENT_CLASS:
                String allyName = hierarchyManager.getParent(userNameFromSession);
                uBoatName = hierarchyManager.getParent(allyName);
                break;

        }
        ReadyManager readyManager = ServletUtils.getBattlefield(getServletContext(),uBoatName).getReadyManager();
        if(readyManager.isIsAllReady()){
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
