package servlets;

import DecryptionManager.DM;
import EnginePackage.Battlefield;
import WebConstants.Constants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.AllyManager;
import users.DMManager;
import users.HierarchyManager;
import users.ReadyManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "ResetAllyServlet", urlPatterns = {"/resetAllyServlet"})
public class ResetAllyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String uBoatName = request.getParameter(Constants.UBOAT_NAME);
        String allyName = SessionUtils.getUsername(request);
        Battlefield battlefield = ServletUtils.getBattlefield(getServletContext(), uBoatName);

        // reset dm ally

        DMManager DMManager = ServletUtils.getBattlefield(getServletContext(),uBoatName).getDmManager();
        DMManager.removeDM(allyName);

        // reset hierarchy ally // TODO : RESET?
        /*HierarchyManager hierarchyManager = ServletUtils.getHierarchyManager(getServletContext());
        hierarchyManager.disconnectAllyFromUBoat(allyName);*/

        // set ready to false

        ReadyManager readyManager = ServletUtils.getBattlefield(getServletContext(),uBoatName).getReadyManager();
        readyManager.removeAlliesToSet(allyName);

        //TODO : stop create task for DM ?

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
