package MORA.AP;

import MORA.AP.Frame.TriggerFrame;
import MORA.AP.RU.RU;

public interface APInterface {

    public void sendTF();
    public void receiveData(TriggerFrame tf);
    public void sendBA(RU ru);
    public void initStats();
    public void writeStats();
    public void run();
    public void setStationNum(int num);

}
