package servlets;

import DTOs.DTO_CodeDescription;
import DTOs.DTO_MachineInfo;
import EnginePackage.Battlefield;
import EnginePackage.EngineCapabilities;
import com.google.gson.Gson;
import WebConstants.Constants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.util.Pair;
import users.UserManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@WebServlet(name = "DTOServlet", urlPatterns = {"/dtoServlet"})
public class DTOServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Gson gson;
        EngineCapabilities engine = null;
        String userName = request.getParameter(Constants.USERNAME);
        String dtoType = request.getParameter(Constants.DTO_TYPE);
        if(dtoType.equals(Constants.DTO_MACHINE_INFO_PARAMETER) || dtoType.equals(Constants.DTO_MACHINE_CONFIGURATION) || dtoType.equals(Constants.DICTIONARY))
            engine = ServletUtils.getBattlefield(getServletContext(), userName).getEngine();

        response.setStatus(HttpServletResponse.SC_OK); // is it ok to set to good and if bas set again to bad ?

        switch (dtoType) {
            //case Constants.DTO_MACHINE_INFO:
            case Constants.DTO_MACHINE_INFO_PARAMETER:
                DTO_MachineInfo dtoMachineInfo = engine.createMachineInfoDTO();
                response.getOutputStream().print(createMachineInfoAsString(dtoMachineInfo));
                break;

            case Constants.DTO_MACHINE_CONFIGURATION:
                DTO_CodeDescription dto_codeDescription = engine.createCodeDescriptionDTO();
                response.getOutputStream().print(createMachineConfigurationString(dto_codeDescription));
                break;
            case Constants.DICTIONARY:
                Set<String> dictionary = engine.getMachine().getMyDictionary();
                gson = new Gson();
                String json_dictionary = gson.toJson(dictionary);
                response.getWriter().println(json_dictionary);
                break;
            case Constants.DTO_ALLIES:
                Set<String> usersAllies = ServletUtils.getUserManager(getServletContext()).getUsersAllies();
                gson = new Gson();
                String usersAlliesJson = gson.toJson(usersAllies);
                response.getWriter().println(usersAlliesJson);
                break;
            case Constants.DTO_UBOATS:
                // set<pair<uboatName,battlefieldName>>
                Set<Pair<String,String>> setUboatBattlefield = new HashSet<>();
                Set<String> usersUboat = ServletUtils.getUserManager(getServletContext()).getUsersUBoat();
                for(String uboat: usersUboat){
                    setUboatBattlefield.add(new Pair<String,String>(uboat, ServletUtils.getBattlefield(getServletContext(),uboat).getName()));
                }
                gson = new Gson();
                String usersUBoatBattlefieldJson = gson.toJson(setUboatBattlefield);
                response.getWriter().println(usersUBoatBattlefieldJson);
                break;
            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                break;
        }
    }

    public String createMachineInfoAsString(DTO_MachineInfo machineInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("Machine Status:\n");
        sb.append("1.    a) Number of Possible Rotors: " + machineInfo.getNumOfPossibleRotors());
        sb.append("\n      b) Number of Rotors in use: " + machineInfo.getNumOfUsedRotors());
        sb.append("\n2. Number of Reflectors: " + machineInfo.getNumOfReflectors());
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
