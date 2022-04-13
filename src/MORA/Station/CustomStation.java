package MORA.Station;

import MORA.AP.CustomAP;
import MORA.AP.Frame.TriggerFrame;
import MORA.AP.RU.BusyToneContainer;
import MORA.AP.RU.RU;
import MORA.Station.OBO.OBOInterface;

import java.util.Random;

public class CustomStation implements StationInterface {

    private static Random random = new Random();
    private OBOInterface obo;
    private int allocatedRu;
    private int allocatedVru;
    private int availableRu;
    private int availableVru;
    private int availableBT;
    private int allocatedBT;

    private BusyToneContainer bt;

    public CustomStation(OBOInterface obo) {
        this.obo = obo;
        allocatedRu = 0;
        allocatedVru = 0;
        availableRu = 0;
        availableVru = 0;
    }


    @Override
    public void receiveTF(TriggerFrame triggerFrame) {
        obo.minus(triggerFrame.num_ru * triggerFrame.num_vru);
        availableRu = triggerFrame.num_ru;
        availableVru = triggerFrame.num_vru;
        availableBT = CustomAP.NUM_BT;
        bt = triggerFrame.bt;

        if(obo.isAvailable()) allocateRU();
    }

    @Override
    public void receiveBA(boolean result) {
        if(result) obo.success();
        else obo.fail();
    }

    @Override
    public void allocateRU() {
        int b = obo.getInitialValue();
        allocatedRu = b % availableRu;
        // allocatedVru = (b - allocatedRu)/availableRu;
        allocatedVru = b % availableVru;
        allocatedBT = random.nextInt(availableBT);

        // bt 전송?
        bt.bt[allocatedRu][allocatedVru].vru[allocatedBT] = true; // 사용한다 => true


    }

    // bt 체크하고 idle vru로 바꾼다?
    public boolean isCollision() {
        int i=0;
        for(; i<availableBT; i++) {
            if(bt.bt[allocatedRu][allocatedVru].vru[i] == true) break;
        }
        if(allocatedBT <= i) return false;
        else return true;
    }

    @Override
    public void sendData(TriggerFrame triggerFrame) {
        if(isCollision()) {

            System.out.println("충돌 감지 vru 변경이 필요합니다.");
        }
        // allocateRU();
        // System.out.println("allocatedRU : " + allocatedRu);
        // System.out.println("allocatedVRU : " + allocatedVru);
        triggerFrame.ru.ru[allocatedRu][allocatedVru].use(this);
    }

    @Override
    public boolean isAvailable() {
        return obo.isAvailable();
    }
}
