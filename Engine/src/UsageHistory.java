import DTOs.DTO_CodeDescription;
import javafx.util.Pair;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


//////////////////////////////////////////////////////////////
//  Every node in the list is a code segment and for each   //
//  code segment we have a list of messages that were       //
//  encoded / decoded and the time it took to encode /      //
//  decode them.                                            //
//////////////////////////////////////////////////////////////

public class UsageHistory {

    private List<Pair<DTO_CodeDescription, List<Pair<Pair<String, String>, Duration>>>> data = new ArrayList<>();

    public void addCodeSegment(DTO_CodeDescription dto_codeDescription) {
        data.add(new Pair<>(dto_codeDescription, new ArrayList<>()));
    }

    public void addMsgAndTimeToCurrentCodeSegment(String beforeMsg, String afterMsg, Duration time) {
        data.get(data.size()).getValue().add(new Pair<>(new Pair<>(beforeMsg, afterMsg), time));
    }

    public List<Pair<DTO_CodeDescription, List<Pair<Pair<String, String>, Duration>>>> getData() {
        return data;
    }
}
