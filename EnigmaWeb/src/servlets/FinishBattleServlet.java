package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.ReadyManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "FinishBattleServlet", urlPatterns = {"/finishBattleServlet"})
public class FinishBattleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String uBoatName = SessionUtils.getUsername(request);
        ReadyManager readyManager = ServletUtils.getBattlefield(getServletContext(),uBoatName).getReadyManager();
        readyManager.setIsAllReady(false);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
