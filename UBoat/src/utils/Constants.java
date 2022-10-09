package utils;

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


}
