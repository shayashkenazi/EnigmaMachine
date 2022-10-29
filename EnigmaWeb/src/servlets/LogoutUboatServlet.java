package servlets;

import EnginePackage.Battlefield;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.HierarchyManager;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "LogoutUboatServlet", urlPatterns = {"/logoutUboatServlet"})
public class LogoutUboatServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        HierarchyManager hierarchyManager =ServletUtils.getHierarchyManager(getServletContext());

        String battlefieldName = ServletUtils.getBattlefield(getServletContext(),usernameFromSession).getName();
        hierarchyManager.removeUboat(usernameFromSession);
        userManager.removeUser(usernameFromSession);
        userManager.removeBattlefieldName(battlefieldName);
        userManager.removeUserUBoat(usernameFromSession);
        SessionUtils.clearSession(request);

        getServletContext().setAttribute(usernameFromSession,null); // init to null

    }


}

