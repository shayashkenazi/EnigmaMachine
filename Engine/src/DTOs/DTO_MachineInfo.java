package DTOs;

import javafx.util.Pair;

import java.util.*;

public class DTO_MachineInfo {

    private String abc;
    private Integer numOfPossibleRotors;
    private List<Integer> notchPositionList;
    private Integer numOfReflectors;
    private Integer numOfUsedRotors;
    private List<String> RotorOrderABC;

    public DTO_MachineInfo (String abc, Integer numOfPossibleRotors, Integer numOfUsedRotors,
                            List<Integer> notchPositionList, Integer numOfReflectors,
                            List<String> RotorOrderABC) {

        this.abc = abc;
        this.numOfPossibleRotors = numOfPossibleRotors;
        this.notchPositionList = notchPositionList;
        this.numOfReflectors = numOfReflectors;
        this.numOfUsedRotors = numOfUsedRotors;
        this.RotorOrderABC = RotorOrderABC;
    }

    public String getABC() { return abc; }
    public int getNumOfPossibleRotors() { return numOfPossibleRotors; }
    public List<Integer> getNotchPositionList() { return notchPositionList; }
    public Integer getNumOfReflectors() { return numOfReflectors; }
    public Integer getNumOfUsedRotors() { return numOfUsedRotors; }
    public String getABCOrderOfSpecificRotor(int numOfRotor) { return RotorOrderABC.get(numOfRotor);}
}
