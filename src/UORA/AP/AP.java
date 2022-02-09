package UORA.AP;

import UORA.Station.StationFactory;
import UORA.Station.StationInterface;
import UORA.resource.RARU;
import UORA.resource.TriggerFrame;

import java.util.ArrayList;
import java.util.List;



public class AP {

    public static int NUM_STATION = 50;
    public static int NUM_TRANSMISSION = 100000;
    public static int DIFS = 18; // 단위 us
    public static int SIFS = 16; // 단위 us
    public static int DTI = 30; // 단위 us
    public static int NUM_RU = 6;
    public static long PK_SIZE = 1000; // 단위 byte
    public static int TF_SIZE = 89; // 단위 byte
    public static int DATA_RATE = 1; // Gbps
    public static int BA_SIZE = 32; // byte
    public static double TF_TRANSMIT_TIME = ((double)(TF_SIZE * 8))/((double)(DATA_RATE * 1000));
    public static double BA_TRANSMIT_TIME = ((double)(BA_SIZE*8))/((double)(DATA_RATE*1000));
    public static double TWT_INTERVAL = DTI + DIFS + TF_TRANSMIT_TIME + (2*SIFS) + BA_TRANSMIT_TIME; // us


    // 성능 측정에 사용할 값들
    public static int total_transmit; // 총 전송 시도 횟수
    public static int collisionCount;
    public static int successCount;
    public static double alpha;

    public static ArrayList<Double> MBsList = new ArrayList<>();
    public static ArrayList<Double> successRateList = new ArrayList<>();
    public static ArrayList<Integer> totalTransmitList = new ArrayList<>();
    public static ArrayList<Integer> totalSuccessList = new ArrayList<>();

    public void initPerformanceStatus() {
        total_transmit = 0;
        collisionCount = 0;
        successCount = 0;
        alpha = 0;
    }

    public void printAvgPerformance() {

        Double MBs = new Double(0);
        Double successRate = new Double(0);
        Double totalTransfer = new Double(0);
        Double successCount = new Double(0);

        int num_simulate = MBsList.size();

        for(int i=0; i<num_simulate; i++) {

            MBs += MBsList.get(i);
            successRate += successRateList.get(i);
            totalTransfer += totalTransmitList.get(i);
            successCount += totalSuccessList.get(i);

        }

        Double count = new Double(num_simulate);

        MBs /= count;
        successRate /= count;
        totalTransfer /= count;
        successCount /= count;

        System.out.println();
        System.out.println();
        System.out.println("******************************************************");
        System.out.println("NUM STA : " + NUM_STATION);
        System.out.println("NUM SIM : " + num_simulate);
        System.out.println("평균 성공 횟수 : " + successCount);
        System.out.println("평균 충돌 횟수 : " + (totalTransfer-successCount));

        System.out.println();
        System.out.println();

        System.out.println("평균 Throughput : " + MBs + "MB/s");
        System.out.println("평균 성공률 : " + successRate);
        System.out.println("평균 충돌 발생률 : " + (100-successRate));
        System.out.println("평균 전송 시도 횟수 : " + totalTransfer);

        System.out.println();
        System.out.println();

        System.out.println("각 STA 별 평균 전송 성공 횟수 : " + (successCount/NUM_STATION));

        double networkStatus = ((double)NUM_RU)/(double)NUM_STATION;
        double a;

        if(networkStatus >= 1) {
            a = ((double)1) + (networkStatus * 0.125);
        } else {
            a = ((double)1) - (0.125/networkStatus);
        }

        if(a < 0.1) a = 0.1;
        if(a > 10) a = 10;

        System.out.println("알파 예상값 : " + a);

        System.out.println("******************************************************");

    }

    public void printAlphaExpected() {

        for(int i=01; i<=50; i++) {

            double networkStatus = ((double)NUM_RU)/(double)i;
            double a;

            if(networkStatus >= 1) {
                a = ((double)1) + (networkStatus * 0.125);
            } else {
                a = ((double)1) - (0.125/networkStatus);
            }

            if(a < 0.1) a = 0.1;
            if(a > 10) a = 10;

            System.out.println("STA의 수가 " + i + "일 때 알파 예상값 : " + a);

        }
    }

    public void writePerformance() {

        // System.out.println("---------------------------------------------------------");

        /*double networkStatus = ((double)NUM_RU)/(double)NUM_STATION;
        double a;

        if(networkStatus >= 1) {
            a = ((double)1) + (networkStatus * 0.125);
        } else {
            a = ((double)1) - (0.125/networkStatus);
        }

        if(a < 0.1) a = 0.1;
        if(a > 10) a = 10;*/

        /*System.out.println("알파 예상값 : " + a);
        System.out.println("TWT_INTERVAL : " + TWT_INTERVAL);
        System.out.println("TF_TRANSMISSION : " + TF_TRANSMIT_TIME);
        System.out.println("BA_TRANSMISSION : " + BA_TRANSMIT_TIME);*/

//        System.out.println("---------------------------------------------------------");

//        System.out.println("총 전송 시도 횟수 : " + total_transmit);
        totalTransmitList.add(total_transmit);
//        System.out.println("성공 횟수 : " + successCount);
        totalSuccessList.add(successCount);
//        System.out.println("충돌 횟수 : " + collisionCount);


        double mbps = (double)(((double)(successCount*PK_SIZE*8))/((double)(NUM_TRANSMISSION*TWT_INTERVAL)));
//        System.out.println("Throughput : " + mbps + "Mbps");

        double MBs = mbps / (double)8;
//        System.out.println("Throughput : " + MBs + "MB/s");
        MBsList.add(MBs);


        double collisionRate = ((double)collisionCount/(double)total_transmit)*(double)100;
//        System.out.println("충돌 발생률 : " + collisionRate);
        double successRate = ((double)successCount/(double)total_transmit)*(double)100;
//        System.out.println("성공률 : " + successRate);
        successRateList.add(successRate);

//        System.out.println("---------------------------------------------------------");

    }

    public void run() {

        // station들 생성
        for(int i=0; i<NUM_STATION; i++) {
            addStation(StationFactory.createDynamicChannelAccessStation());
        }

        for(int i=0; i<NUM_TRANSMISSION; i++) {

            sendTF();
            sendACK();

        }

    }

    public void addStation(int amount) {
        for(int i=0; i<amount; i++)
            addStation(StationFactory.createDynamicChannelAccessStation());
    }

    public void removeStation(int amount) {
        for(int i=0; i<amount; i++) {
            stations.remove(0);
        }
    }

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

    public void dynamicRun() {

        // 시작 전 ap에 이미 연결되어 있는 스테이션들
        addStation(10);

        // 20000TF, 10STA
        for(int i=0; i<20000; i++) {
            sendTF();
            sendACK();
        }

        addStation(10);

        // 30000TF, 20STA
        for(int i=0; i<10000; i++) {
            sendTF();
            sendACK();
        }

        addStation(20);

        // 40000TF, 40STA
        for(int i=0; i<10000; i++) {
            sendTF();
            sendACK();
        }

        addStation(40);

        // 50000TF, 80STA
        for(int i=0; i<10000; i++) {
            sendTF();
            sendACK();
        }

        addStation(20);

        // 70000TF, 100STA
        for(int i=0; i<20000; i++) {
            sendTF();
            sendACK();
        }

        removeStation(20);

        // 80000TF, 80STA
        for(int i=0; i<10000; i++) {
            sendTF();
            sendACK();
        }

        removeStation(40);

        // 90000TF, 40STA
        for(int i=0; i<10000; i++) {
            sendTF();
            sendACK();
        }

        removeStation(30);

        // 100000TF
        for(int i=0; i<10000; i++) {
            sendTF();
            sendACK();
        }


    }

    private List<StationInterface> stations;
    private TriggerFrame triggerFrame;

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

        // System.out.println("ru를 할당한 tf" + triggerFrame);

    }

    public void sendACK() {

        // System.out.println("데이터 수신 이후" + triggerFrame);

        List<RARU> ruList = triggerFrame.getRuList();
        for(RARU ru : ruList) {

            List<StationInterface> stationList = ru.getStations();

            int num_station = stationList.size();

            if(num_station == 0) continue;

            total_transmit += num_station;

            for(StationInterface station : stationList) {
                // System.out.println(ru);
                boolean isSuccess = (num_station == 1);

                if(isSuccess)
                    successCount++;
                else
                    collisionCount++;

                station.receiveACK(isSuccess);
            }
        }

    }

    public void addStation(StationInterface station) {
        stations.add(station);
    }

}
