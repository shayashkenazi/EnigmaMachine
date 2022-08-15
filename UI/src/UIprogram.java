import DTOs.DTO_MachineInfo;

public interface UIprogram {

    //boolean run();
    void showManu();
    void ReadMachineSetting();

    void createSpecificMachineSetting();

    boolean handleChoice(int choice);
    void showMachineStatus(DTO_MachineInfo machineInfo);

    void run();
    void showHistoryAndStatistics();
}
