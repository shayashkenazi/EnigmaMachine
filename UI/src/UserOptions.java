public enum UserOptions {
    LOAD_XML("Create machine settings using xml file"),
    SHOW_MACHINE_STATUS("Display machine specifications"),
    CREATE_SPECIFIC_MACHINE("Create Specific Machine Details"),
    CREATE_RANDOM_MACHINE("Create Random Machine Details"),
    ENCODE_DECODE("Insert input to Encode/ Decode"),
    CODE_INITIALIZE("Init Machine Details"),
    HISTORIC_AND_STATISTIC("Show Historic and Statistic"),
    SAVE_MACHINE_INFORMATION("Bonus - Save all the machine data to load it later"),
    LOAD_MACHINE_INFORMATION("Bonus - Load all the machine data from your previous save"),
    EXIT("Exit");

    private final String description;

    UserOptions(String optionDescription) {
        description = optionDescription;
    }


    public String getDescription() {
        return description;
    }
}
