package servlets;

import DTOs.DTO_CandidateResult;
import WebConstants.Constants;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.ResultsManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.Set;

@WebServlet(name = "ResultServlet", urlPatterns = {"/resultServlet"})
public class ResultServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String type = request.getParameter(Constants.CLASS_TYPE);
        String userNameFromSession = SessionUtils.getUsername(request);
        String uBoatName = "";
        switch (type){
            case Constants.UBOAT_CLASS:
                uBoatName = userNameFromSession;
                break;
            case Constants.ALLY_NAME:
                uBoatName = ServletUtils.getHierarchyManager(getServletContext()).getParent(userNameFromSession);
                break;
        }
        ResultsManager resultsManager = ServletUtils.getBattlefield(getServletContext(),uBoatName).getResultsManager();
        Set<DTO_CandidateResult> dto_candidateResultSet = resultsManager.getDto_candidateResultSet();
        Gson gson = new Gson();
        String candidatesJson = gson.toJson(dto_candidateResultSet);
        response.getWriter().println(candidatesJson);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}

