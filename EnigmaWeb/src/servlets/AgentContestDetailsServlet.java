package servlets;

import DTOs.DTO_ContestData;
import EnginePackage.Battlefield;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.HierarchyManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebServlet(name = "AgentContestDetailsServlet", urlPatterns = {"/agentContestDetailsServlet"})
public class AgentContestDetailsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String userNameFromSession = SessionUtils.getUsername(request); //AGENT USERNAME

        HierarchyManager hierarchyManager = ServletUtils.getHierarchyManager(getServletContext());
        String allyName = hierarchyManager.getParent(userNameFromSession);
        String uBoat = hierarchyManager.getParent(allyName);
        Battlefield battlefield = ServletUtils.getBattlefield(getServletContext(), uBoat);
        DTO_ContestData dto_contestData = new DTO_ContestData(battlefield.getName(), uBoat,
                battlefield.getReadyManager().isIsAllReady(), battlefield.getLevel(),
                battlefield.getNumOfAllies(), battlefield.getReadyManager().getAlliesSet().size());
        Gson gson = new Gson();
        String agentContestsDataJson = gson.toJson(dto_contestData);
        response.getWriter().println(agentContestsDataJson);
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
