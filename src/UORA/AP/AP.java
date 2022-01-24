package UORA.AP;

import UORA.Station.Station;
import UORA.resource.RARU;
import UORA.resource.TriggerFrame;

import java.util.List;



public class AP {

    public static int DIFS;
    public static int SIFS;
    public static int DTI;
    public static int NUM_RU;
    public static int PK_SIZE;
    public static int collisionCount;
    public static int successCount;

    private List<Station> stations;
    private TriggerFrame triggerFrame;

    // 트리거 프레임 전송
    public void sendTF() {

        triggerFrame = new TriggerFrame();
        allocateRU(triggerFrame);

        stations.stream().forEach(station -> station.receiveTF(triggerFrame));

    }

    private void allocateRU(TriggerFrame tf) {

        for(int i=1; i<=NUM_RU; i++) {
            RARU ru = new RARU(i);
            tf.addRU(ru);
        }

    }

    public void sendACK() {

        List<RARU> ruList = triggerFrame.getRuList();
        for(RARU ru : ruList) {

            List<Station> stationList = ru.getStations();

            int num_station = stationList.size();

            if(num_station == 0) return;

            for(Station station : stationList) {
                System.out.println(ru);
                station.receiveACK((num_station == 1));
            }
        }

    }

    public void addStation(Station station) {
        stations.add(station);
    }

}
