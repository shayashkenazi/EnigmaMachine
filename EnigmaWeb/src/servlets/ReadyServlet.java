package servlets;

import DecryptionManager.DM;
import EnginePackage.Battlefield;
import WebConstants.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.util.Pair;
import users.HierarchyManager;
import users.ReadyManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
@WebServlet(name = "ReadyServlet", urlPatterns = {"/readyServlet"})
public class ReadyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String dtoType = request.getParameter(Constants.CLASS_TYPE);
        String userName = SessionUtils.getUsername(request);
        String uBoatName;
        if(dtoType.equals(Constants.ALLIES_CLASS)){
            HierarchyManager hierarchyManager = ServletUtils.getHierarchyManager(getServletContext());
            uBoatName = hierarchyManager.getParent(userName);
        }
        else
            uBoatName = userName;

        ReadyManager readyManager = ServletUtils.getBattlefield(getServletContext(),uBoatName).getReadyManager();
                switch (dtoType){
            case Constants.ALLIES_CLASS:
                readyManager.addAlliesToSet(userName);
                break;
            case Constants.UBOAT_CLASS:
                String sentenceToCheck = request.getParameter(Constants.SENTENCE_TO_CHECK);
                readyManager.setuBoatAndSentence(new Pair<>(uBoatName,sentenceToCheck));
                break;
        }

        Battlefield battlefield = ServletUtils.getBattlefield(getServletContext(), uBoatName);

        if(battlefield.getNumOfAllies() == readyManager.getAlliesSet().size() && readyManager.getuBoatAndSentence() != null){
            readyManager.setIsAllReady(true);
        }
        //DM dm = new DM(battlefield.getEngine(), battlefield.getLevel(), Integer.parseInt(taskSize));
        //request.getSession(false).setAttribute(Constants.DM, dm); //TODO CHECK IF TRUE AT THE GET SESSION
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
