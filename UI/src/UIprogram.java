import DTOs.DTO_MachineInfo;

import java.io.FileNotFoundException;

public interface UIprogram {

    void showManu();
    void ReadMachineSetting();
    void createSpecificMachineSetting();
    boolean handleChoice(int choice);
    void showMachineStatus(DTO_MachineInfo machineInfo);
    void run();
    void showHistoryAndStatistics();
    void saveInfoToFile();
    void loadInfoFromFile();
}
