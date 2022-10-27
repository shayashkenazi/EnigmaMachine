package servlets;

import DTOs.DTO_CandidateResult;
import DTOs.DTO_ContestData;
import EnginePackage.Battlefield;
import WebConstants.Constants;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebServlet(name = "ContestDataServlet", urlPatterns = {"/contestDataServlet"})
public class ContestDataServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String userNameFromSession = SessionUtils.getUsername(request); //ALLY USERNAME
        List<DTO_ContestData> dto_contestDataList = new ArrayList<>();
        Set<String> usersUboatSet = ServletUtils.getUserManager(getServletContext()).getUsersUBoat();
        for(String uBoat: usersUboatSet){
            Battlefield battlefield = ServletUtils.getBattlefield(getServletContext(),uBoat);
            DTO_ContestData dto_contestData = new DTO_ContestData(battlefield.getName(),uBoat,
                    battlefield.getReadyManager().isIsAllReady(),battlefield.getLevel(),
                    battlefield.getNumOfAllies(),battlefield.getReadyManager().getAlliesSet().size());
            dto_contestDataList.add(dto_contestData);
        }

        Gson gson = new Gson();
        String contestsDataJson = gson.toJson(dto_contestDataList);
        response.getWriter().println(contestsDataJson);
        response.setStatus(HttpServletResponse.SC_OK);


    }
}

