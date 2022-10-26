package DTOs;

public class DTO_CandidateResult {
    private String agentName;
    private String allyName;
    private String sentenceCheck;
    private String configuration;

    public DTO_CandidateResult(String agentName, String allyName, String sentenceCheck, String configuration) {
        this.agentName = agentName;
        this.allyName = allyName;
        this.sentenceCheck = sentenceCheck;
        this.configuration = configuration;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getAllyName() {
        return allyName;
    }

    public String getSentenceCheck() {
        return sentenceCheck;
    }

    public String getConfiguration() {
        return configuration;
    }

    public String getPrintedFormat(){
        StringBuilder sb = new StringBuilder();
        sb.append("Sentence - ").append(sentenceCheck).append("\n");
        sb.append("Ally name is - ").append(allyName).append("\n");
        sb.append("Configuration - ").append(configuration).append("\n");
        return sb.toString();
    }
}
