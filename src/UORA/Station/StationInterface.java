package UORA.Station;

import UORA.resource.TriggerFrame;

public interface StationInterface {

    public void receiveTF(TriggerFrame tf);
    public void receiveACK(boolean isSuccess);
    public void send(TriggerFrame tf);

    double getAlpha();

}
