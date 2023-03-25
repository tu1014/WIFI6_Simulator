package BUSYTONE;


import BUSYTONE.resource.TriggerFrame;

public interface StationInterface {

    public void receiveTF(TriggerFrame tf);
    public void receiveACK(boolean isSuccess);
    public void send(TriggerFrame tf);

    public void checkAndSendData(TriggerFrame tf);

    double getAlpha();
    double getFailCount();

    double getAvgOCW();

    public void sendBusyTone(TriggerFrame tf, int flagNumber);
    public void checkBusyTone(TriggerFrame tf, int flagNumber);
    public double getTotalDTI();

}
