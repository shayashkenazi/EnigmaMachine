package utils;

import EnginePackage.Battlefield;
import EnginePackage.EngineCapabilities;
import EnginePackage.EnigmaEngine;
import jakarta.servlet.ServletContext;
import users.UserManager;

public class ServletUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String ENGINE_ATTRIBUTE_NAME = "engine";

    private static final Object userManagerLock = new Object();
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

    public static EngineCapabilities getEngine(ServletContext servletContext) {

        synchronized (engineLock) {
            if (servletContext.getAttribute(ENGINE_ATTRIBUTE_NAME) == null)
                servletContext.setAttribute(ENGINE_ATTRIBUTE_NAME, new EnigmaEngine());
        }

        return (EngineCapabilities) servletContext.getAttribute(ENGINE_ATTRIBUTE_NAME);
    }
    public static Battlefield getBattlefield(ServletContext servletContext,String battlefieldName) {

        synchronized (battlefieldLock) {
            if (servletContext.getAttribute(battlefieldName) == null)
                servletContext.setAttribute(battlefieldName, new Battlefield());
        }

        return (Battlefield) servletContext.getAttribute(battlefieldName);
    }
}
