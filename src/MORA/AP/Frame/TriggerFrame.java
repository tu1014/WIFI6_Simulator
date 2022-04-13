package MORA.AP.Frame;

import MORA.AP.CustomAP;
import MORA.AP.RU.BusyToneContainer;
import MORA.AP.RU.RU;

public class TriggerFrame {

    public int num_ru;
    public int num_vru;
    public RU ru;

    public BusyToneContainer bt;
    public int num_bt;

    public TriggerFrame(int num_ru, int num_vru) {
        this.num_ru = num_ru;
        this.num_vru = num_vru;
        ru =  new RU(num_ru, num_vru);
        bt = new BusyToneContainer(num_ru, num_vru, CustomAP.NUM_BT);
        num_bt = CustomAP.NUM_BT;
    }


}
