package UORA.AP;

import UORA.Station.StationFactory;
import UORA.Station.StationInterface;
import UORA.resource.RARU;
import UORA.resource.TriggerFrame;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;



public class AP {

    public static int NUM_STATION = 0;
    public static int NUM_TRANSMISSION = 100000;
    public static int SIFS = 16; // 단위 us
    public static int DTI = 30; // 단위 us
    public static int NUM_RU = 8;
    public static long PK_SIZE = 1000; // 단위 byte
    public static int TF_SIZE = 89; // 단위 byte
    public static int DATA_RATE = 1; // Gbps
    public static int BA_SIZE = 32; // byte
    public static double TF_TRANSMIT_TIME = ((double)(TF_SIZE * 8))/((double)(DATA_RATE * 1000));
    public static double BA_TRANSMIT_TIME = ((double)(BA_SIZE*8))/((double)(DATA_RATE*1000));
    private static double PK_TRANSMIT_TIME = ((double)(PK_SIZE * 8))/((double)(DATA_RATE * 1000));
    public static double TWT_INTERVAL = DTI + TF_TRANSMIT_TIME + (2*SIFS) + BA_TRANSMIT_TIME; // us

    private List<StationInterface> stations;
    private TriggerFrame triggerFrame;


    // 성능 측정에 사용할 값들
    public static int total_transmit;
    public static int collisionCount;
    public static int successCount;

    public static ArrayList<Double> MBsList = new ArrayList<>();
    public static ArrayList<Double> successRateList = new ArrayList<>();
    public static ArrayList<Integer> totalTransmitList = new ArrayList<>();
    public static ArrayList<Integer> totalSuccessList = new ArrayList<>();

    public static ArrayList<Double> alphaList = new ArrayList<>();

    public static ArrayList<Double> failCountList = new ArrayList<>();

    public static ArrayList<Integer> txTryCount = new ArrayList<>();

    public static ArrayList<Double> avgOCW = new ArrayList<>();

    public static FileWriter fileWriter;

    static {
        try {
            fileWriter = new FileWriter("20220913-UORA.txt", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setNumStation(int numStation) {
        NUM_STATION = numStation;
    }


    public void initPerformanceStatus() {
        total_transmit = 0;
        collisionCount = 0;
        successCount = 0;
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
    }

    public void printAvgPerformance() throws IOException {

        Double MBs = new Double(0);
        Double successRate = new Double(0);
        Double totalTransfer = new Double(0);
        Double successCount = new Double(0);
        Double alpha = new Double(0);
        Double failCount = new Double(0);
        Double txTry = new Double(0);
        Double avg_ocw = new Double(0);

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
        }

        Double count = new Double(num_simulate);

        MBs /= count;
        successRate /= count;
        totalTransfer /= count;
        successCount /= count;
        alpha /= count;
        failCount /= count;
        txTry /= count;
        avg_ocw /= count;

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
        System.out.println("평균 전송 시도 횟수 : " + totalTransfer);
        System.out.println("평균 알파 : " + alpha);
        System.out.println("평균 재전송 횟수 : " + failCount);
        System.out.println("TF당 전송 시도 횟수 : " + txTry);
        System.out.println("평균 OCW : " + avg_ocw);
        // fileWriter.write(totalTransfer + ",");

        fileWriter.write(stations.size() + ",");
        fileWriter.write(MBs + ",");
        fileWriter.write(successRate + ",");
        fileWriter.write(totalTransfer + ",");
        fileWriter.write(alpha + ",");
        fileWriter.write(failCount + ",");
        fileWriter.write(txTry + ",");
        fileWriter.write(avg_ocw + "\n");

        System.out.println();
        System.out.println();

        System.out.println("각 STA 별 평균 전송 성공 횟수 : " + (successCount/NUM_STATION));

        System.out.println("******************************************************");

    }

    public void writePerformance() {

        totalTransmitList.add(total_transmit);
        totalSuccessList.add(successCount);

        double mbps = (double)(((double)(successCount*PK_SIZE*8))/((double)(NUM_TRANSMISSION*TWT_INTERVAL)));
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

    }

    // STA 수에 따른 성능 측정
    public void run() {

        addStation(NUM_STATION);
        innerRun(NUM_TRANSMISSION);

    }

    // 사용할 알고리즘을 변경하려면 StationFactory 에서 다른 메서드 사용
    public void addStation(int amount) {
        for(int i=0; i<amount; i++)
            addStation(StationFactory.createStandardStation());
    }

    public void removeStation(int amount) {
        for(int i=0; i<amount; i++) {
            stations.remove(0);
        }
    }

    // 가중치에 따른 성능을 비교하기 위한 테스트 케이스
    public void dynamicStation() {

        // 10
        addStation(10);
        innerRun(10000);

        // 20
        addStation(10);
        innerRun(10000);

        // 50
        addStation(30);
        innerRun(10000);

        // 40
        removeStation(10);
        innerRun(10000);

        // 8
        removeStation(32);
        innerRun(10000);

        // 23
        addStation(15);
        innerRun(10000);

        // 40
        addStation(17);
        innerRun(10000);

        // 40
        innerRun(10000);

        // 5
        removeStation(35);
        innerRun(10000);

        // 50
        addStation(45);
        innerRun(10000);

    }

    public void innerRun(int amount) {
        for(int i=0; i<amount; i++) {
            sendTF();
            sendACK();
        }
    }

    public AP() {
    }

    public void init() {
        stations = new ArrayList<>();
        initPerformanceStatus();
    }

    // 트리거 프레임 전송
    public void sendTF() {

        triggerFrame = new TriggerFrame(stations.size());
        allocateRU();

        for(StationInterface station : stations) {
            station.receiveTF(triggerFrame);
        }

    }

    private void allocateRU() {

        for(int i=1; i<=NUM_RU; i++) {
            RARU ru = new RARU(i);
            triggerFrame.addRU(ru);
        }

    }

    public void sendACK() {

        List<RARU> ruList = triggerFrame.getRuList();

        int i = 0;

        for(RARU ru : ruList) {

            List<StationInterface> stationList = ru.getStations();

            int num_station = stationList.size();

            if(num_station == 0) continue;

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

        txTryCount.add(i);

    }

    public void addStation(StationInterface station) {
        stations.add(station);
    }

}
