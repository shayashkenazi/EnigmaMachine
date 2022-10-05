package servlets;

import DTOs.DTO_MachineInfo;
import EnginePackage.EngineCapabilities;
import constants.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.FileNotFoundException;
import java.io.IOException;

@WebServlet(name = "DTOServlet", urlPatterns = {"/dtoServlet"})
public class DTOServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        EngineCapabilities engine = ServletUtils.getEngine(getServletContext());
        String dtoType = request.getParameter(Constants.DTO_TYPE);

        response.setStatus(HttpServletResponse.SC_OK); // is it ok to set to good and if bas set again to bad ?

        switch (dtoType) {
            //case Constants.DTO_MACHINE_INFO:
            case "machineInfo": // TODO: Constants
                DTO_MachineInfo dtoMachineInfo = engine.createMachineInfoDTO();
                response.getOutputStream().print(createMachineInfoAsString(engine, dtoMachineInfo));
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
}
