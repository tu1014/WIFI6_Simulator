package MORA.Station;

import MORA.AP.Frame.TriggerFrame;

public interface StationInterface {

    public void receiveTF(TriggerFrame triggerFrame);
    public void receiveBA();
    public void allocateRU();
    public void sendData();


}
