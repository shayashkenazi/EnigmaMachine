package servlets;

import DTOs.DTO_AgentDetails;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.AgentsDetailsManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetTeamsAgentDetailsServlet", urlPatterns = {"/getTeamsAgentDetailsServlet"})
public class GetTeamsAgentDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String userNameFromSession = SessionUtils.getUsername(request);
        AgentsDetailsManager agentsDetailsManager = ServletUtils.getAgentsDetailsManager(getServletContext(),userNameFromSession);
        List<DTO_AgentDetails> dto_agentDetailsList = agentsDetailsManager.getAgentTeamDetails(userNameFromSession);
        Gson gson = new Gson();
        String json_agentDetailsList = gson.toJson(dto_agentDetailsList);
        response.getWriter().println(json_agentDetailsList);
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
