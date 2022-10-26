package servlets;


import DTOs.DTO_CandidateResult;
import DecryptionManager.DmTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import javafx.util.Pair;
import users.ResultsManager;
import utils.ServletUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;


// agent send the request //
@WebServlet(name = "CandidatesServlet", urlPatterns = {"/candidatesServlet"})
public class CandidatesServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Reader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        Gson gson = new Gson();
        Type listCandidatesType = new TypeToken<List<DTO_CandidateResult>>() {
        }.getType();
        List<DTO_CandidateResult> listCandidates =  gson.fromJson(reader,listCandidatesType);
        //String json_candidatesList = partCur.getInputStream().toString();

        //List<DTO_CandidateResult> listCandidates = gson.fromJson(json_candidatesList, listCandidatesType);
        if(listCandidates.size() == 0){
            return;
        }
        String allyName = listCandidates.get(0).getAllyName();
        String uBoatName = ServletUtils.getHierarchyManager(getServletContext()).getParent(allyName);
        ResultsManager resultsManager = ServletUtils.getBattlefield(getServletContext(), uBoatName).getResultsManager();
        for (DTO_CandidateResult dto_candidateResult : listCandidates) {
            resultsManager.addCandidateResults(dto_candidateResult);
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
