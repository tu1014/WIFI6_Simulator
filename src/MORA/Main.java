package MORA;

import MORA.AP.APFactory;
import MORA.AP.APInterface;
import MORA.TestResult;

public class Main {

    private static int NUM_SIMULATION = 1;
    private static int NUM_STATION = 1;




    public static void main(String[] args) {

        APInterface ap = APFactory.createStandardAP();
        ap.setStationNum(NUM_STATION);

        for(int i=0; i<NUM_SIMULATION; i++) {

            ap.initStats();
            ap.run();
            ap.writeStats();

        }

        TestResult.printPerformance();

    }
}
