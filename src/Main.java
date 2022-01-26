import UORA.AP.AP;
import UORA.Station.Station;
import UORA.Station.StationFactory;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        AP ap = new AP();

        for(int i=0; i<5; i++) {

            ap.init();
            ap.run();
            ap.printPerformance();

        }

    }
}
