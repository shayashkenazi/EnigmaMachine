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

import java.io.IOException;

@WebServlet(name = "CheckBattleIsReadyServlet", urlPatterns = {"/checkBattleIsReadyServlet"})
public class CheckBattleIsReadyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String allyName = request.getParameter(Constants.ALLY_NAME);
        HierarchyManager hierarchyManager = ServletUtils.getHierarchyManager(getServletContext());
        String uBoatName = hierarchyManager.getParent(allyName);
        String battlefieldName = ServletUtils.getBattlefield(getServletContext(),uBoatName).getName();
        ReadyManager readyManager = ServletUtils.getReadyManager(getServletContext(),battlefieldName);
        if(readyManager.isIsAllReady()){
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
