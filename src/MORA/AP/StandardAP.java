package MORA.AP;

import MORA.Station.StationInterface;

import java.util.ArrayList;

import static MORA.TestResult.*;

public class StandardAP implements APInterface {

    // 성능 계산을 위해 기록하는 변수들
    private int transmitCount;
    private int successCount;
    private int idleVRUCount;


    // 테스트에 필요한 파라미터들
    private static int NUM_STATION;
    private static int NUM_TRANSMIT = 100000;
    private static int DATA_RATE = 1; // Gbps
    private static int PK_SIZE = 1000; // byte
    private static int PREAMBLE_SIZE = 40; // byte
    private static int TF_SIZE = 89; // byte
    private static int BA_SIZE = 32; // byte
    private static int SIFS = 16; // us
    private static int DIFS = 18; // us
    public static double TF_TRANSMIT_TIME = ((double)(TF_SIZE * 8))/((double)(DATA_RATE * 1000));
    public static double BA_TRANSMIT_TIME = ((double)(BA_SIZE*8))/((double)(DATA_RATE*1000));
    public static double PREAMBLE_TRANSMIT_TIME = ((double)(PREAMBLE_SIZE * 8))/((double)(DATA_RATE * 1000));
    public static double TWT_INTERVAL = NUM_TRANSMIT + TF_TRANSMIT_TIME + (2*SIFS) + BA_TRANSMIT_TIME; // us
    // ocw min = 16, ocw max 1024

    private ArrayList<StationInterface> stations;

    public StandardAP() {
        // this.stations = stations;
        initStats();
    }

    @Override
    public void initStats() {
        transmitCount = 0;
        successCount = 0;
        idleVRUCount = 0;
    }

    @Override
    public void writeStats() {
        double successRate = ((double)successCount*(double)100)/(double)transmitCount;
        double collisionRate = 1 - successRate;
        double MBs = ((double)(successCount*PK_SIZE*8))/NUM_TRANSMIT*TWT_INTERVAL;

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

    }

    @Override
    public void sendTF() {

    }

    @Override
    public void sendBA() {

    }
}
