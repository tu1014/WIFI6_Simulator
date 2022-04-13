package MORA.Station;

import MORA.AP.Frame.TriggerFrame;
import MORA.AP.RU.RU;

public interface StationInterface {

    public void receiveTF(TriggerFrame triggerFrame);
    public void receiveBA(boolean result);
    public void allocateRU();
    public void sendData(RU ru);


}
