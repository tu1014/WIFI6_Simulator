package MORA.Station;

import MORA.AP.Frame.TriggerFrame;
import MORA.AP.RU.RU;
import MORA.Station.OBO.OBOInterface;

import java.util.Random;

public class StandardStation implements StationInterface {

    private static Random random = new Random();
    private OBOInterface obo;
    private int allocatedRu;
    private int allocatedVru;
    private int availableRu;
    private int availableVru;


    public StandardStation(OBOInterface obo) {
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

        // if(obo.isAvailable()) sendData(triggerFrame);
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
        allocatedVru = (b) % availableVru;
    }

    @Override
    public void sendData(TriggerFrame triggerFrame) {
        allocateRU();
        // System.out.println("allocatedRU : " + allocatedRu);
        // System.out.println("allocatedVRU : " + allocatedVru);
        triggerFrame.ru.ru[allocatedRu][allocatedVru].use(this);
    }

    @Override
    public boolean isAvailable() {
        return obo.isAvailable();
    }
}
