package UORA.AP;

import UORA.Station.Station;
import UORA.Station.StationFactory;
import UORA.Station.StationInterface;
import UORA.resource.RARU;
import UORA.resource.TriggerFrame;

import java.util.ArrayList;
import java.util.List;



public class AP {

    public static int NUM_STATION = 100;
    public static int NUM_TRANSMISSION = 100000;
    public static int DIFS = 18; // 단위 us
    public static int SIFS = 16; // 단위 us
    public static int DTI = 30; // 단위 us
    public static int NUM_RU = 8;
    public static long PK_SIZE = 1000; // 단위 byte
    public static int TF_SIZE = 89; // 단위 byte
    public static int DATA_RATE = 1; // Gbps
    public static int BA_SIZE = 32; // byte
    public static double TF_TRANSMIT_TIME = ((double)(TF_SIZE * 8))/((double)(DATA_RATE * 1000));
    public static double BA_TRANSMIT_TIME = ((double)(BA_SIZE*8))/((double)(DATA_RATE*1000));
    public static double TWT_INTERVAL = DIFS + TF_TRANSMIT_TIME + (2*SIFS) + DTI + BA_TRANSMIT_TIME; // us


    // 성능 측정에 사용할 값들
    public static int total_transmit; // 총 전송 시도 횟수
    public static int collisionCount;
    public static int successCount;

    public void initPerformanceStatus() {
        total_transmit = 0;
        collisionCount = 0;
        successCount = 0;
    }

    public void printPerformance() {

        System.out.println("---------------------------------------------------------");

        System.out.println("TWT_INTERVAL : " + TWT_INTERVAL);
        System.out.println("TF_TRANSMISSION : " + TF_TRANSMIT_TIME);
        System.out.println("BA_TRANSMISSION : " + BA_TRANSMIT_TIME);

        System.out.println("---------------------------------------------------------");

        System.out.println("총 전송 시도 횟수 : " + total_transmit);
        System.out.println("성공 횟수 : " + successCount);
        System.out.println("충돌 횟수 : " + collisionCount);


        double mbps = (double)(((double)(successCount*PK_SIZE*8))/((double)(NUM_TRANSMISSION*TWT_INTERVAL)));
        System.out.println("Throughput : " + mbps + "Mbps");

        double MBs = mbps / (double)8;
        System.out.println("Throughput : " + MBs + "MB/s");



        double collisionRate = ((double)collisionCount/(double)total_transmit)*(double)100;
        System.out.println("충돌 발생률 : " + collisionRate);
        double successRate = ((double)successCount/(double)total_transmit)*(double)100;
        System.out.println("성공률 : " + successRate);

        System.out.println("---------------------------------------------------------");

    }

    public void run() {

        // station들 생성
        for(int i=0; i<NUM_STATION; i++) {
            addStation(StationFactory.createStandardStation());
        }

        for(int i=0; i<NUM_TRANSMISSION; i++) {

            sendTF();
            sendACK();

        }

    }

    private List<Station> stations;
    private TriggerFrame triggerFrame;

    public AP() {
    }

    public void init() {
        stations = new ArrayList<>();
        initPerformanceStatus();
    }

    // 트리거 프레임 전송
    public void sendTF() {

        triggerFrame = new TriggerFrame(NUM_STATION);
        allocateRU();

        // stations.stream().forEach(station -> station.receiveTF(triggerFrame));

        for(Station station : stations) {
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

            for(Station station : stationList) {
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

    public void addStation(Station station) {
        stations.add(station);
    }

}
