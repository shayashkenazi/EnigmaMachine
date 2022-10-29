package servlets;

import DTOs.DTO_AgentTasksDetails;
import WebConstants.Constants;
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
import java.util.Set;

@WebServlet(name = "UpdateAgentTasksDetailsServlet", urlPatterns = {"/updateAgentTasksDetailsServlet"})
public class UpdateAgentTasksDetailsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String userNameFromSession = SessionUtils.getUsername(request);
        String allyName = request.getParameter(Constants.ALLY_NAME);
        int countOfTasksTaken = Integer.parseInt(request.getParameter(Constants.COUNT_OF_TASKS_TAKEN));
        int CountTaskLeftInThreadPool = Integer.parseInt(request.getParameter(Constants.AGENT_TASKS_LEFT_POOL));

        AgentsDetailsManager agentsDetailsManager = ServletUtils.getAgentsDetailsManager(getServletContext(), allyName);
        Set<DTO_AgentTasksDetails> DTO_AgentTasksDetails = agentsDetailsManager.getSetAgentTasksDetails();
        for(DTO_AgentTasksDetails dto_agentTasksDetails:DTO_AgentTasksDetails){
            if(dto_agentTasksDetails.getAgentName().equals(userNameFromSession)){
                dto_agentTasksDetails.setCountOfTasksTaken(countOfTasksTaken);
                dto_agentTasksDetails.setCountTaskLeftInThreadPool(CountTaskLeftInThreadPool);
                break;
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}