package MORA;

import MORA.AP.APFactory;
import MORA.AP.APInterface;
import MORA.TestResult;

import java.io.IOException;

public class Main {

    private static int NUM_SIMULATION = 1;




    public static void main(String[] args) {

        for(int i=1; i<=100; i++) {
            run(i);
        }
//        run(100);

    }

    private static void run(int num_station) {
        APInterface ap = APFactory.createCustomAP();
        ap.setStationNum(num_station);
        TestResult.initArrays();
        for(int i=0; i<NUM_SIMULATION; i++) {

            ap.initStats();
            ap.run();
            ap.writeStats();

        }

        TestResult.printPerformance();
        try {
            TestResult.fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
