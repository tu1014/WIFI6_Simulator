import UORA.AP.AP;
import UORA.Station.Station;
import UORA.Station.StationFactory;

import java.util.ArrayList;
import java.util.List;

public class Main {

    static int NUM_STATION = 10;
    static int NUM_TRANSMISSION = 30;

    public static void main(String[] args) {

        AP ap = new AP();

        // station들 생성
        for(int i=0; i<NUM_STATION; i++) {
            ap.addStation(StationFactory.createStandardStation());
        }

        for(int i=0; i<NUM_TRANSMISSION; i++) {

            ap.sendTF();
            ap.sendACK();

        }


    }

}
