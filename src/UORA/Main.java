package UORA;

import UORA.AP.AP;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        for(int i=1; i<=100; i++) {
            run(i);
        }

        AP.fileWriter.close();

    }

    public static void run(int numStation) throws IOException {

        AP ap = new AP();
        ap.setNumStation(numStation);
        ap.initStaticArray();

        int num_simulation = 50;
        for(int i=0; i<num_simulation; i++) {

            ap.init();
            ap.run();
            // ap.dynamicStation();
            ap.writePerformance();

        }
        ap.printAvgPerformance();

    }

}
