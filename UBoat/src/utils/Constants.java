package utils;

import com.google.gson.Gson;

public class Constants {

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/EnigmaWeb_Web";
    //public final static String DTO = "/dto";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public final static String LOAD_XML = FULL_SERVER_PATH + "/LoadXmlServlet";
    public final static String DTO = FULL_SERVER_PATH + "/dtoServlet";
    public final static String ENCRYPT_CODE = FULL_SERVER_PATH + "/encryptCodeServlet";
    public final static String SET_CODE = FULL_SERVER_PATH + "/SetCodeServlet";
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginServlet";

    public static final String SENTENCE_TO_CHECK = "sentenceToCheck";
    public static final String RESULT = FULL_SERVER_PATH + "/resultServlet";
    public static final String TEAMS_DETAILS = FULL_SERVER_PATH  + "/teamsDetailsServlet";
    public static final String FINISH_BATTLE = FULL_SERVER_PATH  + "/finishBattleServlet";
    public static final String LOGOUT_UBOAT = FULL_SERVER_PATH  + "/logoutUboatServlet";
    public final static String USERNAME = "username";
    public final static String CLASS_TYPE = "classType";
    public static final String BATTLEFIELD_NAME = "battlefieldName";
    public static final String UBOAT_NAME = "uboatName";
    public final static int REFRESH_RATE = 2000;
    public final static String CHECK_READY_BATTLE = FULL_SERVER_PATH + "/checkBattleIsReadyServlet";
    public static final String READY = FULL_SERVER_PATH + "/readyServlet";
    public static final String FINISHED_UBOAT = FULL_SERVER_PATH + "/finishedFromUboatServlet";

    public static final String DTO_TYPE = "dtoType";
    public final static String UBOAT_CLASS = "uBoat";
    public final static Gson GSON_INSTANCE = new Gson();
}
