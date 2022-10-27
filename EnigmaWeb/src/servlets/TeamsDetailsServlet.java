package servlets;

import DTOs.DTO_AllyDetails;
import DTOs.DTO_CandidateResult;
import WebConstants.Constants;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet(name = "TeamsDetailsServlet", urlPatterns = {"/teamsDetailsServlet"})
public class TeamsDetailsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String type = request.getParameter(Constants.CLASS_TYPE);
        String userNameFromSession = SessionUtils.getUsername(request);
        String uBoatName = "";
        switch (type) {
            case Constants.UBOAT_CLASS:
                uBoatName = userNameFromSession;
                List<DTO_AllyDetails> dto_allyDetailsList = ServletUtils.getAllyDetailsManager(getServletContext()).getListOfAllyDetailsWithUboatName(uBoatName);
                Gson gson = new Gson();
                String candidatesJson = gson.toJson(dto_allyDetailsList);
                response.getWriter().println(candidatesJson);
                response.setStatus(HttpServletResponse.SC_OK);
                break;
            case Constants.ALLIES_CLASS:
                uBoatName = ServletUtils.getHierarchyManager(getServletContext()).getParent(userNameFromSession);
                break;
        }

    }
}
