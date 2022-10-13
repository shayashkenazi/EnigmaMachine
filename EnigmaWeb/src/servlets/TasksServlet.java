package servlets;

import WebConstants.Constants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "TasksServlet", urlPatterns = {"/TasksServlet"})
public class TasksServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String agentName = request.getParameter(Constants.AGENT_NAME);
        int numberOfTasks = Integer.parseInt(request.getParameter(Constants.NUMBER_TASKS));
        String allyName = request.getParameter(Constants.ALLY_NAME);





    }

}
