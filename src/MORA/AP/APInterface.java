package MORA.AP;

import MORA.AP.RU.RU;

public interface APInterface {

    public void sendTF();
    public void sendBA(RU ru);
    public void initStats();
    public void writeStats();
    public void run();
    public void setStationNum(int num);

}
