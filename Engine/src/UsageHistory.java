import DTOs.DTO_CodeDescription;
import javafx.util.Pair;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


//////////////////////////////////////////////////////////////
//  Every node in the list is a code segment and for each   //
//  code segment we have a list of messages that were       //
//  encoded / decoded and the time it took to encode /      //
//  decode them.                                            //
//////////////////////////////////////////////////////////////

public class UsageHistory implements Serializable {

    private String xmlPath;
    private List<Pair<DTO_CodeDescription, List<Pair<Pair<String, String>, Long>>>> data = new ArrayList<>();

    public void addCodeSegment(DTO_CodeDescription dto_codeDescription) {
        data.add(new Pair<>(dto_codeDescription, new ArrayList<>()));
    }

    public void addMsgAndTimeToCurrentCodeSegment(String beforeMsg, String afterMsg, long time) {
        data.get(data.size() - 1).getValue().add(new Pair<>(new Pair<>(beforeMsg, afterMsg), time));
    }

    public List<Pair<DTO_CodeDescription, List<Pair<Pair<String, String>, Long>>>> getData() {
        return data;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public DTO_CodeDescription getFirstCodeDescription() {

        return data.get(0).getKey();
    }

    public DTO_CodeDescription getCurrentCodeDescription() {

        return data.get(data.size() - 1).getKey();
    }

    public int getNumOfProcessMsg() {
        int sum = 0;
        for (Pair<DTO_CodeDescription, List<Pair<Pair<String, String>, Long>>> pair : data)
            sum += pair.getValue().size();
        return sum;
    }
}
