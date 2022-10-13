package utils;

import EnginePackage.Battlefield;
import EnginePackage.EngineCapabilities;
import EnginePackage.EnigmaEngine;
import jakarta.servlet.ServletContext;
import users.ResultsManager;
import users.UserManager;

public class ServletUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String BATTLEFIELD_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String ENGINE_ATTRIBUTE_NAME = "engine";

    private static final Object userManagerLock = new Object();
    private static final Object battlefieldManagerLock = new Object();
    private static final Object resultsManagerLock = new Object();
    private static final Object engineLock = new Object();
    private static final Object battlefieldLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }
    public static ResultsManager getResultsManager(ServletContext servletContext,String battlefieldName) {

        synchronized (resultsManagerLock) {
            if (servletContext.getAttribute(battlefieldName) == null) {
                servletContext.setAttribute(battlefieldName, new ResultsManager());
            }
        }
        return (ResultsManager) servletContext.getAttribute(battlefieldName);
    }

    public static Battlefield getBattlefield(ServletContext servletContext, String userName) {

        synchronized (battlefieldLock) {
            if (servletContext.getAttribute(userName) == null)
                servletContext.setAttribute(userName, new Battlefield());
        }

        return (Battlefield) servletContext.getAttribute(userName);
    }
}
