package servlets;

import DTOs.DTO_AgentDetails;
import DTOs.DTO_AgentTasksDetails;
import DTOs.DTO_ProgressDM;
import DecryptionManager.DM;
import WebConstants.Constants;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.AgentsDetailsManager;
import users.DMManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.ArrayList;
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

        String uBoatName = request.getParameter(Constants.UBOAT_NAME);
        DMManager dmManager = ServletUtils.getBattlefield(getServletContext(),uBoatName).getDmManager();
        DM curDM = dmManager.getDM(userNameFromSession);
        int sum = 0;
        for(DTO_AgentTasksDetails dto_agentTasksDetails: dto_AgentTasksDetailsSet){
            sum += dto_agentTasksDetails.getCountOfTasksTaken();
        }
        DTO_ProgressDM dto_progressDM = new DTO_ProgressDM(curDM.getAllTaskSize(),curDM.getCounterOfCreatedTasks().get(),sum);
        dto_progressDM.setDto_AgentTasksDetailsSet(dto_AgentTasksDetailsSet);

        Gson gson = new Gson();
        String json_agentDetailsList = gson.toJson(dto_progressDM);
        response.getWriter().println(json_agentDetailsList);
    }
}
