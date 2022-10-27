package servlets;

import DTOs.DTO_AgentDetails;
import DTOs.DTO_AllyDetails;
import WebConstants.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.*;
import users.AgentsDetailsManager;
import users.AllyManager;
import users.HierarchyManager;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;


import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/loginServlet"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                         throws ServletException, IOException {
        String classType = request.getParameter(Constants.CLASS_TYPE);
        processRequest(request, response,classType);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response,String classType)
            throws ServletException, IOException {

        response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        HierarchyManager hierarchyManager =ServletUtils.getHierarchyManager(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet

            String usernameFromParameter = request.getParameter(Constants.USERNAME);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();

                synchronized (this) {
                    if (userManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getOutputStream().print(errorMessage);
                    }
                    else {
                        //add the new user to the users list
                        userManager.addUser(usernameFromParameter);
                        hierarchyManager.addNode(usernameFromParameter);
                        switch (classType){
                            case Constants.UBOAT_CLASS:
                                userManager.addUBoatUser(usernameFromParameter);
                                break;
                            case Constants.ALLIES_CLASS:
                                userManager.addAlliesUser(usernameFromParameter);
                                //AllyManager allyManager = ServletUtils.getAllyDetailsManager(getServletContext());
                                //new DTO_AllyDetails(usernameFromParameter,0,));
                                //allyManager.addAllyDetails()
                                break;
                            case Constants.AGENT_CLASS:
                                userManager.addAgentUser(usernameFromParameter);
                                String myAlly = request.getParameter("ally");
                                hierarchyManager.connectAgentToParent(myAlly,usernameFromParameter);
                                AgentsDetailsManager agentsDetailsManager = ServletUtils.getAgentsDetailsManager(getServletContext(),myAlly);
                                int numberOfTasks = Integer.parseInt(request.getParameter(Constants.NUMBER_TASKS));
                                int countOfThreads = Integer.parseInt(request.getParameter(Constants.THREADS_NUMBER));
                                agentsDetailsManager.addAgentDetails(myAlly,new DTO_AgentDetails(usernameFromParameter,countOfThreads,numberOfTasks));
                                ServletUtils.getAllyDetailsManager(getServletContext()).increaseAgentNumber(myAlly);
                                break;
                        }
                        request.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);

                        //redirect the request to the chat room - in order to actually change the URL
                        System.out.println("On login, request URI is: " + request.getRequestURI()); // TODO: UNDERSTAND THE GET URI
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            }
        } else {
            //user is already logged in
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
