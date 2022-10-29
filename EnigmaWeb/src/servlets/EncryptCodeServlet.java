package servlets;

import DTOs.DTO_CodeDescription;
import EnginePackage.EngineCapabilities;
import WebConstants.Constants;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.util.Pair;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "EncryptCodeServlet", urlPatterns = {"/encryptCodeServlet"})
public class EncryptCodeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        //String userName = request.getParameter(Constants.USERNAME);
        String usernameuBoatFromSession = SessionUtils.getUsername(request);
        EngineCapabilities engine = ServletUtils.getBattlefield(getServletContext(), usernameuBoatFromSession).getEngine();
        String msgToEncode = request.getParameter(Constants.MSG_TO_DECODE);
        String tmpConfigurationBefore = createMachineConfigurationString(engine.createCodeDescriptionDTO());
        String resDecode = engine.encodeDecodeMsg(msgToEncode,false);
        String tmpConfigurationAfter = createMachineConfigurationString(engine.createCodeDescriptionDTO());
        List<String> list = new ArrayList<>();
        list.add(tmpConfigurationBefore);
        list.add(tmpConfigurationAfter);
        list.add(resDecode);
        Gson gson = new Gson();
        String json_configurationDetailsList = gson.toJson(list);
        response.getWriter().println(json_configurationDetailsList);
        response.setStatus(HttpServletResponse.SC_OK);
        //response.getOutputStream().print(resDecode);

    }
    public String createMachineConfigurationString(DTO_CodeDescription dto_codeDescription) {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        int index = dto_codeDescription.getRotorsInUseIDList().size() - 1;

        for(int i = dto_codeDescription.getRotorsInUseIDList().size() - 1; i >= 0; i--)
        {
            Pair<String , Pair<Integer,Integer>> rotorId = dto_codeDescription.getRotorsInUseIDList().get(i);
            sb.append(rotorId.getKey()).append(","); // need to have curr index eac h rotor
        }
        sb.replace(sb.length() - 1,sb.length(),">"); // for the last ','
        sb.append("<");
        Collections.reverse(dto_codeDescription.getStartingPositionList());
        for(Character ch : dto_codeDescription.getStartingPositionList()){
            Pair<String ,Pair<Integer,Integer>> rotorId = dto_codeDescription.getRotorsInUseIDList().get(index);
            int distance = Math.floorMod(dto_codeDescription.getNotch(rotorId) - dto_codeDescription.getCurrent(rotorId), dto_codeDescription.getABC().length()) ; //'% dto_codeDescription.'
            sb.append(ch).append("(").append(distance).append("),"); // need to have curr index eac h rotor
            index--;
        }
        sb.replace(sb.length() - 1,sb.length(),">"); // for the last ','
        sb.append("<").append(dto_codeDescription.getReflectorID()).append(">");
        Collections.reverse(dto_codeDescription.getStartingPositionList());

        if (dto_codeDescription.getPlugsInUseList().size() != 0) {
            sb.append("<");
            for (int i = 0; i < dto_codeDescription.getPlugsInUseList().size(); i++) {
                Pair<Character, Character> pair = dto_codeDescription.getPlugsInUseList().get(i);
                sb.append(pair.getKey()).append("|").append(pair.getValue()).append(",");
            }
            sb.replace(sb.length() - 1,sb.length(),">");// replace the last ',' with '>'
        }

        return sb.toString();
    }

}
