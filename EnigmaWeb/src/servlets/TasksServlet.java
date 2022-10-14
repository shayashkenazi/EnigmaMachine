package servlets;

import DecryptionManager.DM;
//import DecryptionManager.DmTask;
import DecryptionManager.DmTask;
import WebConstants.Constants;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.util.Pair;
import users.DMManager;
import users.HierarchyManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "TasksServlet", urlPatterns = {"/tasksServlet"})
public class TasksServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String agentName = request.getParameter(Constants.AGENT_NAME);
        int numberOfTasks = Integer.parseInt(request.getParameter(Constants.NUMBER_TASKS));
        String allyName = request.getParameter(Constants.ALLY_NAME);

        HierarchyManager hierarchyManager =ServletUtils.getHierarchyManager(getServletContext());
        String uBoatName = hierarchyManager.getParent(allyName);

        String battlefieldName = ServletUtils.getBattlefield(getServletContext(),uBoatName).getName();
        DMManager userManager = ServletUtils.getDMManager(getServletContext(),battlefieldName);
        DM curDM = userManager.getDM(allyName);
        List<DmTask> dmTasks = curDM.getTasksForAgent(numberOfTasks);
        for (DmTask Task: dmTasks){
            Task.setAgentExecuteName(agentName);
            Task.setAllyName(allyName);
        }
        Gson gson = new Gson();
        Pair <String, List<DmTask>> pair = new Pair<>(agentName,dmTasks);
        String dmTasksJson = gson.toJson(pair);
        response.getWriter().println(dmTasksJson);

    }

}
