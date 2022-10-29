package servlets;

import DTOs.DTO_CandidateResult;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.ResultsManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.Set;

@WebServlet(name = "FinishedFromUboatServlet", urlPatterns = {"/finishedFromUboatServlet"})
public class FinishedFromUboatServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String usernameFromSession = SessionUtils.getUsername(request);
        ResultsManager resultsManager = ServletUtils.getBattlefield(getServletContext(),usernameFromSession).getResultsManager();
        resultsManager.clearCandidates();

    }
}
