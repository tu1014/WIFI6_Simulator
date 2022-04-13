package MORA;

import MORA.AP.APFactory;
import MORA.AP.APInterface;
import MORA.TestResult;

public class Main {

    private static int NUM_SIMULATION = 100;




    public static void main(String[] args) {

        APInterface ap = APFactory.createStandardAP();
        ap.setStationNum(100);

        for(int i=0; i<NUM_SIMULATION; i++) {

            ap.initStats();
            // ap.run();
            ap.writeStats();


        }

        TestResult.printPerformance();

    }
}
