package UORA.AP;

import UORA.Station.StationFactory;
import UORA.Station.StationInterface;
import UORA.Writer;
import UORA.resource.RARU;
import UORA.resource.TriggerFrame;

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
    public static double TWT_INTERVAL = DIFS + TF_TRANSMIT_TIME + (2*SIFS) + DTI + BA_TRANSMIT_TIME; // us

    private List<StationInterface> stations;
    private TriggerFrame triggerFrame;

    private boolean TIME_FLAG = false;

    public void setTIME_FLAG(boolean flag) {
        TIME_FLAG = flag;
    }


    // 성능 측정에 사용할 값들
    public static int total_transmit;
    public static int collisionCount;
    public static int successCount;

    public static double ruCollisionRate;
    public static double ruSuccessRate;
    public static double ruIdleRate;
    private static int ru_counter;

    private static double txTry;

    public static ArrayList<Double> MBsList = new ArrayList<>();
    public static ArrayList<Double> successRateList = new ArrayList<>();
    public static ArrayList<Integer> totalTransmitList = new ArrayList<>();
    public static ArrayList<Integer> totalSuccessList = new ArrayList<>();

    public static ArrayList<Double> alphaList = new ArrayList<>();

    public static ArrayList<Double> failCountList = new ArrayList<>();

    public static ArrayList<Double> txTryCount = new ArrayList<>();

    public static ArrayList<Double> avgOCW = new ArrayList<>();

    public static ArrayList<Double> ruCollisionRateList = new ArrayList<>();
    public static ArrayList<Double> ruSuccessRateList = new ArrayList<>();
    public static ArrayList<Double> ruIdleRateList = new ArrayList<>();

    public static ArrayList<Double> delayList = new ArrayList<>();

    public static FileWriter fileWriter;

    public int count = 0;

    static {
        try {
            fileWriter = new FileWriter("FINAL_PCS(1.5).txt", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 사용할 알고리즘을 변경하려면 StationFactory 에서 다른 메서드 사용
    public void addStation(int amount) {
        for(int i=0; i<amount; i++) {
            // addStation(StationFactory.createMyFinalIdea()); // 나의 기법
            // addStation(StationFactory.createStandardStation()); // 표준
            addStation(StationFactory.createChannelAccessStation()); // 기존 논문
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
        ruSuccessRate = 0;
        ruIdleRate = 0;
        ru_counter = 0;
        txTry = 0;
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
        ruSuccessRateList = new ArrayList<>();
        ruIdleRateList = new ArrayList<>();
        delayList = new ArrayList<>();
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
        Double ru_success_rate = Double.valueOf(0d);
        Double ru_idle_rate = Double.valueOf(0d);
        Double delay = Double.valueOf(0);

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
            ru_success_rate += ruSuccessRateList.get(i);
            ru_idle_rate += ruIdleRateList.get(i);
            delay += delayList.get(i);
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
        ru_success_rate /= count;
        ru_idle_rate /= count;
        delay /= count;

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
        fileWriter.write(delay + ","); // delay
        fileWriter.write(txTry + ",");
        fileWriter.write(avg_ocw + ",");
        fileWriter.write(ru_collision_rate + ",");
        fileWriter.write(ru_success_rate + ",");
        fileWriter.write(ru_idle_rate + "\n");

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
        double delay = 0;
        for(StationInterface station : stations) {
            d += station.getFailCount();
            avg += station.getAvgOCW();
            delay += station.getTotalDTI();
        }
        d /= stations.size();
        avg /= stations.size();
        delay = delay / (double)successCount * (double)TWT_INTERVAL;
        avgOCW.add(avg);
        failCountList.add(d);
        delayList.add(delay);

        ruCollisionRateList.add(ruCollisionRate);
        ruSuccessRateList.add(ruSuccessRate);
        ruIdleRateList.add(ruIdleRate);

        txTryCount.add(txTry);

    }

    // STA 수에 따른 성능 측정
    public void run() {

        addStation(NUM_STATION);
        innerRun(NUM_TRANSMISSION);

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

    public void sendACK() throws IOException {

        List<RARU> ruList = triggerFrame.getRuList();

        int num_tx_try_station = 0; // 한 번의 TF에서 전송을 시도한 STA의 수를 측정하기 위한 변수

        int failRUCount = 0;
        int successRUCount = 0;
        int idleRUCount = 0;

        for(RARU ru : ruList) {

            List<StationInterface> stationList = ru.getStations();

            int num_station = stationList.size();

            if(num_station == 0) {
                idleRUCount++;
                continue;
            }

            else if (num_station == 1) {
                successRUCount++;
            }

            else if(num_station != 0 && num_station != 1) {
                failRUCount++;
            }

            total_transmit += num_station;
            num_tx_try_station += num_station;

            for(StationInterface station : stationList) {

                boolean isSuccess = (num_station == 1);

                if(isSuccess)
                    successCount++;
                else
                    collisionCount++;

                station.receiveACK(isSuccess);
            }
        }

        double failRURate = (double)failRUCount * (double)100 / (double)NUM_RU;
        double successRURate = (double)successRUCount * (double)100 / (double) NUM_RU;
        double idleRURate = (double)idleRUCount * (double)100 / (double) NUM_RU;

        ru_counter++;
        ruCollisionRate = (double)(ru_counter-1)/(double)ru_counter*ruCollisionRate + (double)failRURate/ru_counter;
        ruSuccessRate = (double)(ru_counter-1)/(double)ru_counter*ruSuccessRate + (double)successRURate/ru_counter;
        ruIdleRate = (double)(ru_counter-1)/(double)ru_counter*ruIdleRate + (double)idleRURate/ru_counter;


        // 여기서 평균 필터 적용하고, write funtion에서 리스트에 add 하도록 수정해보자
        txTry = (double)(ru_counter-1)/(double)ru_counter*txTry + (double)num_tx_try_station/ru_counter;


        // x축이 시간일 때 측정을 위한 코드
        // TF 100번 마다 측정
        if (TIME_FLAG && count % 100 == 0 ) {

            double mbps = (double)(((double)(successCount*PK_SIZE*8))/((double)(NUM_TRANSMISSION*TWT_INTERVAL)));
            double MBs = mbps / (double)8;
            Writer.fileWriter.write(MBs + ",");

            ///

            double collisionRate = ((double)collisionCount/(double)total_transmit)*(double)100;
            Writer.fileWriter.write(collisionRate + ",");

            double avg = 0;
            for(StationInterface station : stations) {
                avg += station.getAvgOCW();
            }
            avg /= stations.size();
            Writer.fileWriter.write(avg + "\n");

        }

        // System.out.println(triggerFrame);

    }

    public void addStation(StationInterface station) {
        stations.add(station);
    }

}
