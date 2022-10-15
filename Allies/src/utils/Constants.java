package utils;

import com.google.gson.Gson;

public class Constants {

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    public static final String UBOAT_NAME = "uboatName";

    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/EnigmaWeb_Web";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginServlet";
    public static final String HIERARCHY = FULL_SERVER_PATH + "/hierarchyServlet";
    public static final String DM = FULL_SERVER_PATH + "/allyDMServlet";

    public final static String USERNAME = "username";
    public final static String CLASS_TYPE = "classType";
    public final static String DTO = FULL_SERVER_PATH + "/dtoServlet";
    public static final String DTO_TYPE = "dtoType";
    public static final String DTO_UBOATS = "uboats";
    public static final String TASK_SIZE = "taskSize";

    public final static Gson GSON_INSTANCE = new Gson();
}
