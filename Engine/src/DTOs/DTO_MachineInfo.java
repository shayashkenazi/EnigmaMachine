package DTOs;

import javafx.util.Pair;

import java.util.*;

public class DTO_MachineInfo {

    private String abc;
    private Integer numOfPossibleRotors;
    private List<Integer> notchPositionList;
    private Integer numOfReflectors;
    private Integer numOfMsgProcessed;
    private Integer numOfUsedRotors;

    public DTO_MachineInfo (String abc, Integer numOfPossibleRotors, Integer numOfUsedRotors,
                            List<Integer> notchPositionList, Integer numOfReflectors,
                            Integer numOfMsgProcessed) {

        this.abc = abc;
        this.numOfPossibleRotors = numOfPossibleRotors;
        this.notchPositionList = notchPositionList;
        this.numOfReflectors = numOfReflectors;
        this.numOfMsgProcessed = numOfMsgProcessed;
        this.numOfUsedRotors = numOfUsedRotors;
    }

    public String getABC() { return abc; }
    public int getNumOfPossibleRotors() { return numOfPossibleRotors; }
    public List<Integer> getNotchPositionList() { return notchPositionList; }
    public Integer getNumOfReflectors() { return numOfReflectors; }
    public Integer getNumOfMsgProcessed() { return numOfMsgProcessed; }
    public Integer getNumOfUsedRotors() { return numOfUsedRotors; }
}
