package UORA;

import UORA.AP.AP;

public class Main {

    public static void main(String[] args) {

        AP ap = new AP();

        int num_simulation = 100;

        for(int i=0; i<num_simulation; i++) {

            ap.init();
            ap.run();
            // ap.dynamicStation();
            ap.writePerformance();

        }
        ap.printAvgPerformance();

    }
}
