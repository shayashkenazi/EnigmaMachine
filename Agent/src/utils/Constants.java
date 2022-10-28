package utils;

import com.google.gson.Gson;

public class Constants {

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/EnigmaWeb_Web";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginServlet";
    public final static String TASKS = FULL_SERVER_PATH + "/tasksServlet";
    public final static String CHECK_READY_BATTLE = FULL_SERVER_PATH + "/checkBattleIsReadyServlet";
    public final static int REFRESH_RATE = 2000;

    public final static String CANDIDATES = FULL_SERVER_PATH + "/candidatesServlet";
    public final static String UPDATE_AGENT_TASKS_DETAILS = FULL_SERVER_PATH + "/updateAgentTasksDetailsServlet";

    public static final String DTO_ALLIES = "allies";
    public final static String USERNAME = "username";
    public final static String CLASS_TYPE = "classType";
    public final static String DTO = FULL_SERVER_PATH + "/dtoServlet";
    public final static Gson GSON_INSTANCE = new Gson();
}
