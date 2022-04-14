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
    // isIdle의 row에 들어있는 Array>ist의 크기 안에서 랜덤 선택 -> 그 인덱스의 값 꺼내 그 값으로 바꾸기
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
            int randomSize = bt.isIdle[allocatedRu].size(); // 자신의 ru에서 idle한 vru의 수
            if (randomSize != 0) {
                int randomIndex = random.nextInt(randomSize);
                allocatedVru = bt.isIdle[allocatedRu].get(randomIndex);
                // System.out.println("충돌 회피 시도");
            }
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
