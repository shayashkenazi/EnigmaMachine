package servlets;

import DTOs.DTO_CodeDescription;
import DTOs.DTO_MachineInfo;
import EnginePackage.EngineCapabilities;
import com.google.gson.Gson;
import constants.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.util.Pair;
import utils.ServletUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@WebServlet(name = "DTOServlet", urlPatterns = {"/dtoServlet"})
public class DTOServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String userName = request.getParameter("username");
        String dtoType = request.getParameter(Constants.DTO_TYPE);
        EngineCapabilities engine = ServletUtils.getBattlefield(getServletContext(), userName).getEngine();

        response.setStatus(HttpServletResponse.SC_OK); // is it ok to set to good and if bas set again to bad ?

        switch (dtoType) {
            //case Constants.DTO_MACHINE_INFO:
            case Constants.DTO_MACHINE_INFO_PARAMETER: // TODO: Constants
                DTO_MachineInfo dtoMachineInfo = engine.createMachineInfoDTO();
                response.getOutputStream().print(createMachineInfoAsString(engine, dtoMachineInfo));
                break;

            case Constants.DTO_MACHINE_CODE_DESCRIPTION_PARAMETER:
                DTO_CodeDescription dto_codeDescription = engine.createCodeDescriptionDTO();
                response.getOutputStream().print(createMachineConfigurationString(dto_codeDescription));
                break;

            case Constants.DICTIONARY:
                Set<String> dictionary = engine.getMachine().getMyDictionary();
                Gson gson = new Gson(); // TODO: Why like this and not use the Gson instance (i did it like aviad)
                String json_dictionary = gson.toJson(dictionary);
                response.getWriter().println(json_dictionary);
                break;

            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                break;
        }
    }

    public String createMachineInfoAsString(EngineCapabilities engine, DTO_MachineInfo machineInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("Machine Status:\n");
        sb.append("1.    a) Number of Possible Rotors: " + machineInfo.getNumOfPossibleRotors());
        sb.append("\n      b) Number of Rotors in use: " + machineInfo.getNumOfUsedRotors());
        sb.append("\n2. Number of Reflectors: " + machineInfo.getNumOfReflectors());
        sb.append("\n3. Machine number of messages processed: " + engine.getUsageHistory().getNumOfProcessMsg() + "\n");
        return sb.toString();
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
