package MORA.AP;

import MORA.AP.Frame.TriggerFrame;
import MORA.AP.RU.RU;
import MORA.AP.RU.VRU;
import MORA.Station.StationFactory;
import MORA.Station.StationInterface;

import java.util.ArrayList;

import static MORA.TestResult.*;

public class CustomAP implements APInterface {

    // 성능 계산을 위해 기록하는 변수들
    private int transmitCount;
    private int successCount;
    private int idleVRUCount;


    // 테스트에 필요한 파라미터들
    private static int NUM_STATION = 0;
    private static int NUM_TRANSMIT = 20000;
    private static int DATA_RATE = 1; // Gbps
    private static int PK_SIZE = 1000; // byte
    private static int PREAMBLE_SIZE = 40; // byte
    private static int TF_SIZE = 89; // byte
    private static int BA_SIZE = 32; // byte
    private static int SIFS = 16; // us
    private static int DIFS = 18; // us
    private static int NUM_ANTENNA = 4;
    private static int NUM_RU = 8;
    private static int NUM_VRU = NUM_ANTENNA;
    private static int BT_TIME = 3; // us
    public static int NUM_BT = 3;
    private static double TF_TRANSMIT_TIME = ((double)(TF_SIZE * 8))/((double)(DATA_RATE * 1000));
    private static double BA_TRANSMIT_TIME = ((double)(BA_SIZE*8))/((double)(DATA_RATE*1000));
    private static double PREAMBLE_TRANSMIT_TIME = ((double)(PREAMBLE_SIZE * 8))/((double)(DATA_RATE * 1000));
    private static double PK_TRANSMIT_TIME = ((double)(PK_SIZE * 8))/((double)(DATA_RATE * 1000));
    private static double DTI = (PREAMBLE_TRANSMIT_TIME) + PK_TRANSMIT_TIME + (BT_TIME * NUM_BT * NUM_VRU);
    // private static double DTI = PREAMBLE_TRANSMIT_TIME + PK_TRANSMIT_TIME;
    public static double TWT_INTERVAL = TF_TRANSMIT_TIME + (2*SIFS) + DTI + BA_TRANSMIT_TIME; // us
    // ocw min = 16, ocw max 1024

    private ArrayList<StationInterface> stations;

    public CustomAP() {
        initStats();
    }

    @Override
    public void initStats() {
        stations = StationFactory.createCustomStation(NUM_STATION);
        transmitCount = 0;
        successCount = 0;
        idleVRUCount = 0;
    }

    @Override
    public void writeStats() {

        // System.out.println("TWT : " + TWT_INTERVAL);

        double successRate = ((double)successCount*(double)100)/(double)transmitCount;
        double collisionRate = 100 - successRate;
        /////////////
        /*System.out.println("successCount : " + successCount);
        System.out.println("transmitCount : " + transmitCount);*/
        /////////////
        double mbps = ((double)(successCount*PK_SIZE*8))/(NUM_TRANSMIT*TWT_INTERVAL);
        double MBs = mbps/8;

        successRates.add(successRate);
        collisionRates.add(collisionRate);
        throughputs.add(MBs);
        idleCounts.add(idleVRUCount);
    }

    @Override
    public void setStationNum(int numStation) {
        NUM_STATION = numStation;
    }

    @Override
    public void run() {
        for(int i=0; i<NUM_TRANSMIT; i++) {
            sendTF();
        }
    }

    @Override
    public void sendTF() {

        TriggerFrame tf = new TriggerFrame(NUM_RU, NUM_VRU);

        for(StationInterface station : stations) {
            station.receiveTF(tf);
        }

        tf.bt.findIdle();
        receiveData(tf);
        sendBA(tf.ru);
    }

    @Override
    public void receiveData(TriggerFrame tf) {
        for(StationInterface station : stations) {
            if(station.isAvailable()) station.sendData(tf);
        }
    }

    @Override
    public void sendBA(RU ru) {

        VRU[][] vrus = ru.ru;

        // System.out.println(ru);

        for(int i=0; i<vrus.length; i++) {
            boolean isIdle = true;
            for(int j=0; j<vrus[i].length; j++) {

                VRU vru = vrus[i][j];

                int count = vru.stationList.size();

                if(count != 0) {
                    isIdle = false;
                    transmitCount += count;
                    for(StationInterface station : vru.stationList) {

                        if(count == 1) {
                            successCount++;
                            // successVRUCount += vrus[i].length - j;
                            station.receiveBA(true);
                        }

                        else {
                            station.receiveBA(false);
                        }

                    }
                }
            }
            if(isIdle) idleVRUCount++;
        }

    } // end of send BA
}
