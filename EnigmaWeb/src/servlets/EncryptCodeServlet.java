package servlets;

import EnginePackage.EngineCapabilities;
import WebConstants.Constants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "EncryptCodeServlet", urlPatterns = {"/encryptCodeServlet"})
public class EncryptCodeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        //String userName = request.getParameter(Constants.USERNAME);
        String usernameFromSession = SessionUtils.getUsername(request);
        EngineCapabilities engine = ServletUtils.getBattlefield(getServletContext(), usernameFromSession).getEngine();
        String msgToEncode = request.getParameter(Constants.MSG_TO_DECODE);
        String resDecode = engine.encodeDecodeMsg(msgToEncode,false);
        response.getOutputStream().print(resDecode);

    }

}
