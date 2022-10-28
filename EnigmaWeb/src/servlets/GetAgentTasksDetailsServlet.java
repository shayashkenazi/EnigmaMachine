package servlets;

import DTOs.DTO_AgentDetails;
import DTOs.DTO_AgentTasksDetails;
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
import java.util.Set;

@WebServlet(name = "GetAgentTasksDetailsServlet", urlPatterns = {"/getAgentTasksDetailsServlet"})
public class GetAgentTasksDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String userNameFromSession = SessionUtils.getUsername(request);
        AgentsDetailsManager agentsDetailsManager = ServletUtils.getAgentsDetailsManager(getServletContext(),userNameFromSession);
        Set<DTO_AgentTasksDetails> dto_AgentTasksDetailsSet = agentsDetailsManager.getSetAgentTasksDetails();
        Gson gson = new Gson();
        String json_agentDetailsList = gson.toJson(dto_AgentTasksDetailsSet);
        response.getWriter().println(json_agentDetailsList);
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
