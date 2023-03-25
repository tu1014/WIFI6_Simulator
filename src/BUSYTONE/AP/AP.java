package BUSYTONE.AP;

import BUSYTONE.StationFactory;
import BUSYTONE.StationInterface;
import BUSYTONE.resource.RARU;
import BUSYTONE.resource.TriggerFrame;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class AP {

    public static int NUM_STATION = 0;
    public static int NUM_TRANSMISSION = 100000;
    public static int SIFS = 16; // 단위 us

    public static int DIFS = 18;
    public static int DTI = 32; // 단위 us
    public static int NUM_RU = 8;
    public static long PK_SIZE = 1000; // 단위 byte
    public static int TF_SIZE = 89; // 단위 byte
    public static int DATA_RATE = 1; // Gbps
    public static int BA_SIZE = 32; // byte
    public static double TF_TRANSMIT_TIME = ((double)(TF_SIZE * 8))/((double)(DATA_RATE * 1000));
    public static double BA_TRANSMIT_TIME = ((double)(BA_SIZE*8))/((double)(DATA_RATE*1000));
    private static double PK_TRANSMIT_TIME = ((double)(PK_SIZE * 8))/((double)(DATA_RATE * 1000));
    public static double TWT_INTERVAL = TF_TRANSMIT_TIME + (2*SIFS) + DTI + BA_TRANSMIT_TIME; // us

    private List<StationInterface> stations;
    private TriggerFrame triggerFrame;

    public int numBT = 6; // 범석 아이디어의 비지톤 슬롯 개수

    public static int slotTime = 9; // us


    private boolean TIME_FLAG = false;

    public void setTIME_FLAG(boolean flag) {
        TIME_FLAG = flag;
    }


    // 성능 측정에 사용할 값들
    public static int total_transmit;
    public static int collisionCount;
    public static int successCount;

    public static double ruCollisionRate;

    public static ArrayList<Double> MBsList = new ArrayList<>();
    public static ArrayList<Double> successRateList = new ArrayList<>();
    public static ArrayList<Integer> totalTransmitList = new ArrayList<>();
    public static ArrayList<Integer> totalSuccessList = new ArrayList<>();

    public static ArrayList<Double> alphaList = new ArrayList<>();

    public static ArrayList<Double> failCountList = new ArrayList<>();

    public static ArrayList<Integer> txTryCount = new ArrayList<>();

    public static ArrayList<Double> avgOCW = new ArrayList<>();

    public static ArrayList<Double> ruCollisionRateList = new ArrayList<>();

    public static FileWriter fileWriter;

    public int count = 0;

    public int busyToneCounter = 0;

    static {
        try {
            fileWriter = new FileWriter("BS_BUSY_N6.txt", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 사용할 알고리즘을 변경하려면 StationFactory 에서 다른 메서드 사용
    public void addStation(int amount) {
        for(int i=0; i<amount; i++) {
            // addStation(StationFactory.createMyFinalIdea()); // 나의 기법
            // addStation(StationFactory.createStandardStation()); // 표준
            addStation(StationFactory.createStandardStation());
        }
    }

    public void setNumStation(int numStation) {
        NUM_STATION = numStation;
    }

    public void setNumTransmission(int numTransmission) {
        NUM_TRANSMISSION = numTransmission;
    }


    public void initPerformanceStatus() {
        total_transmit = 0;
        collisionCount = 0;
        successCount = 0;
        ruCollisionRate = 0;
        busyToneCounter = 0;
    }

    public void initStaticArray() {
        MBsList = new ArrayList<>();
        successRateList = new ArrayList<>();
        totalTransmitList = new ArrayList<>();
        totalSuccessList = new ArrayList<>();
        alphaList = new ArrayList<>();
        failCountList = new ArrayList<>();
        txTryCount = new ArrayList<>();
        avgOCW = new ArrayList<>();
        ruCollisionRateList = new ArrayList<>();
    }

    public void printAvgPerformance() throws IOException {

        Double MBs = Double.valueOf(0d);
        Double successRate = Double.valueOf(0d);
        Double totalTransfer = Double.valueOf(0d);
        Double successCount = Double.valueOf(0d);
        Double alpha = Double.valueOf(0d);
        Double failCount = Double.valueOf(0d);
        Double txTry = Double.valueOf(0d);
        Double avg_ocw = Double.valueOf(0d);
        Double ru_collision_rate = Double.valueOf(0d);

        int num_simulate = MBsList.size();

        for(int i=0; i<num_simulate; i++) {

            MBs += MBsList.get(i);
            successRate += successRateList.get(i);
            totalTransfer += totalTransmitList.get(i);
            successCount += totalSuccessList.get(i);
            alpha += alphaList.get(i);
            failCount += failCountList.get(i);
            txTry += txTryCount.get(i);
            avg_ocw += avgOCW.get(i);
            ru_collision_rate += ruCollisionRateList.get(i);
        }

        Double count = Double.valueOf(num_simulate);

        MBs /= count;
        successRate /= count;
        totalTransfer /= count;
        successCount /= count;
        alpha /= count;
        failCount /= count;
        txTry /= count;
        avg_ocw /= count;
        ru_collision_rate /= count;

        System.out.println();
        System.out.println();
        System.out.println("TWT INTERVAL : " + TWT_INTERVAL);
        System.out.println("******************************************************");
        System.out.println("NUM STA : " + NUM_STATION);
        System.out.println("NUM SIM : " + num_simulate);
        System.out.println("평균 성공 횟수 : " + successCount);
        System.out.println("평균 충돌 횟수 : " + (totalTransfer-successCount));

        System.out.println();
        System.out.println();

        System.out.println("평균 Throughput : " + MBs + "MB/s");
        // fileWriter.write(MBs + ",");
        System.out.println("평균 성공률 : " + successRate);
        System.out.println("평균 충돌 발생률 : " + (100-successRate));
        System.out.println("ruCollisionRate : " + ru_collision_rate);
        System.out.println("평균 전송 시도 횟수 : " + totalTransfer);
        System.out.println("평균 알파 : " + alpha);
        System.out.println("평균 재전송 횟수 : " + failCount);
        System.out.println("TF당 전송 시도 횟수 : " + txTry);
        System.out.println("평균 OCW : " + avg_ocw);
        // fileWriter.write(totalTransfer + ",");

        // fileWriter.write("NUM_STATION,THROUGHPUT,SUCCESS_RATE,COLLISION_RATE,NUM_TRANSMIT,ALPHA,NUM_FAIL,DELAY,CONTENTION_STATION_NUM,AVG_OCW,RU_COLLISION_RATE\n");
        fileWriter.write(stations.size() + ",");
        fileWriter.write(MBs + ",");
        fileWriter.write(successRate + ",");
        fileWriter.write((100-successRate) + ",");
        fileWriter.write(totalTransfer + ",");
        fileWriter.write(alpha + ",");
        fileWriter.write(failCount + ",");
        fileWriter.write(failCount*TWT_INTERVAL + ",");
        fileWriter.write(txTry + ",");
        fileWriter.write(avg_ocw + ",");
        fileWriter.write(ru_collision_rate + "\n");

        System.out.println();
        System.out.println();

        System.out.println("각 STA 별 평균 전송 성공 횟수 : " + (successCount/NUM_STATION));

        System.out.println("******************************************************");

    }

    public void writePerformance() {

        totalTransmitList.add(total_transmit);
        totalSuccessList.add(successCount);

        double mbps = (double)(((double)(successCount*PK_SIZE*8))/((double)(NUM_TRANSMISSION*TWT_INTERVAL + slotTime*busyToneCounter)));
        double MBs = mbps / (double)8;
        MBsList.add(MBs);


        double collisionRate = ((double)collisionCount/(double)total_transmit)*(double)100;
        double successRate = ((double)successCount/(double)total_transmit)*(double)100;
        successRateList.add(successRate);

        double result = 0;
        for(StationInterface station : stations) {
            result += station.getAlpha();
        }
        result /= stations.size();
        alphaList.add(result);

        double d = 0;
        double avg = 0;
        for(StationInterface station : stations) {
            d += station.getFailCount();
            avg += station.getAvgOCW();
        }
        d /= stations.size();
        avg /= stations.size();
        avgOCW.add(avg);
        failCountList.add(d);

        ruCollisionRateList.add(ruCollisionRate);

    }

    // STA 수에 따른 성능 측정
    public void run() {

        addStation(NUM_STATION);
        innerRun(NUM_TRANSMISSION);

    }

    public void removeStation(int amount) {
        for(int i=0; i<amount; i++) {
            stations.remove(0);
        }
    }


    public void innerRun(int amount) {
        for(int i=0; i<amount; i++) {
            count++;
            sendTF();
            try {
                sendACK();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public AP() {
    }

    public void init() {
        count = 0;
        stations = new ArrayList<>();
        initPerformanceStatus();
    }

    // 트리거 프레임 전송
    public void sendTF() {

        triggerFrame = new TriggerFrame(stations.size(), ruCollisionRate);
        allocateRU();
        triggerFrame.initBusyTone(numBT);
        busyToneCounter += numBT;

        for(StationInterface station : stations) {
            station.receiveTF(triggerFrame);  // Arbitration flag set
        }


        // Arbitration Phase
        for(int i = 0; i < numBT; i++) {
            for(StationInterface station : stations) {
                station.sendBusyTone(triggerFrame, i);
            }

            for(StationInterface station : stations) {
                station.checkBusyTone(triggerFrame, i);
            }
        }

        // 데이터 전송
        for(StationInterface station : stations) {
            station.checkAndSendData(triggerFrame);
        }

    }

    private void allocateRU() {

        for(int i=1; i<=NUM_RU; i++) {
            RARU ru = new RARU(i);
            triggerFrame.addRU(ru);
        }

    }

    public void sendACK() throws IOException {

        List<RARU> ruList = triggerFrame.getRuList();

        int i = 0;

        int failRUCount = 0;

        for(RARU ru : ruList) {

            List<StationInterface> stationList = ru.getStations();

            int num_station = stationList.size();

            if(num_station == 0) continue;

            if(num_station != 0 && num_station != 1) failRUCount++;

            total_transmit += num_station;
            i += num_station;

            for(StationInterface station : stationList) {

                boolean isSuccess = (num_station == 1);

                if(isSuccess)
                    successCount++;
                else
                    collisionCount++;

                station.receiveACK(isSuccess);
            }
        }

        double tmp = (double)failRUCount * (double)100 / (double)NUM_RU;
        double newResultRate = 0.3;
        ruCollisionRate = (((double)1) - newResultRate)*ruCollisionRate + newResultRate*tmp;

        txTryCount.add(i);

    }

    public void addStation(StationInterface station) {
        stations.add(station);
    }

}
