package utils;

import EnginePackage.Battlefield;
import jakarta.servlet.ServletContext;
import users.*;

import java.util.Enumeration;

public class ServletUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String HIERARCHY_MANAGER_ATTRIBUTE_NAME = "hierarchyManager";
    private static final String BATTLEFIELD_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String ENGINE_ATTRIBUTE_NAME = "engine";

    private static final Object userManagerLock = new Object();
    private static final Object readyManagerLock = new Object();
    private static final Object userHierarchyLock = new Object();
    private static final Object battlefieldManagerLock = new Object();
    private static final Object resultsManagerLock = new Object();
    private static final Object dmManagerLock = new Object();
    private static final Object engineLock = new Object();
    private static final Object battlefieldLock = new Object();
    private static final Object battlefieldCheckExistLock = new Object();
    private static final Object agentManagerLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }
    public static HierarchyManager getHierarchyManager(ServletContext servletContext) {

        synchronized (userHierarchyLock) {
            if (servletContext.getAttribute(HIERARCHY_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(HIERARCHY_MANAGER_ATTRIBUTE_NAME, new HierarchyManager());
            }
        }
        return (HierarchyManager) servletContext.getAttribute(HIERARCHY_MANAGER_ATTRIBUTE_NAME);
    }
    public static Battlefield getBattlefield(ServletContext servletContext, String uBoatName) {

        synchronized (battlefieldLock) {
            if (servletContext.getAttribute(uBoatName) == null)
                servletContext.setAttribute(uBoatName, new Battlefield());
        }

        return (Battlefield) servletContext.getAttribute(uBoatName);
    }

    public static AgentsDetailsManager getAgentsDetailsManager(ServletContext servletContext, String allyName) {

        synchronized (agentManagerLock) {
            if (servletContext.getAttribute(allyName) == null)
                servletContext.setAttribute(allyName, new AgentsDetailsManager());
        }

        return (AgentsDetailsManager) servletContext.getAttribute(allyName);
    }

}
