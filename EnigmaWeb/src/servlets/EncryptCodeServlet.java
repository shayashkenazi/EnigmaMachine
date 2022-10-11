package servlets;

import EnginePackage.EngineCapabilities;
import constants.Constants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "EncryptCodeServlet", urlPatterns = {"/encryptCodeServlet"})
public class EncryptCodeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String userName = request.getParameter("userName");
        EngineCapabilities engine = ServletUtils.getBattlefield(getServletContext(), userName).getEngine();
        String msgToEncode = request.getParameter(Constants.MSG_TO_DECODE);
        String resDecode = engine.encodeDecodeMsg(msgToEncode,false);
        response.getOutputStream().print(resDecode);

    }

}
