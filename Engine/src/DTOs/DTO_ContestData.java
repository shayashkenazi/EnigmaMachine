package DTOs;

import DecryptionManager.Difficulty;
import DecryptionManager.Status;
import DecryptionManager.Status;

public class DTO_ContestData {
    private String battlefieldName;
    private String uBoatName;
    private boolean status;
    private Difficulty level;
    private int numberOfTotalAllies;
    private int numberOfActiveAllies;


    public DTO_ContestData(String battlefieldName, String uBoatName, boolean status, Difficulty level, int numberOfTotalAllies, int numberOfActiveAllies) {
        this.battlefieldName = battlefieldName;
        this.uBoatName = uBoatName;
        this.status = status;
        this.level = level;
        this.numberOfTotalAllies = numberOfTotalAllies;
        this.numberOfActiveAllies = numberOfActiveAllies;
    }
    public String printDetailsContestData(){
        StringBuilder sb = new StringBuilder();
        sb.append("battlefield Name - ").append(battlefieldName).append("\n");
        sb.append("uBoat Name - ").append(uBoatName).append("\n");
        sb.append("status of the game is  - ").append(status ? "battle is a active" : "waiting for battle").append("\n");
        sb.append("the level is - ").append(level).append("\n");
        sb.append("allies team - ").append(numberOfActiveAllies).append("/").append(numberOfTotalAllies).append("\n");
        return sb.toString();
    }

    public String getBattlefieldName() {
        return battlefieldName;
    }

    public String getuBoatName() {
        return uBoatName;
    }
}
