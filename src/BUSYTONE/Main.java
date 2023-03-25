package BUSYTONE;


import BUSYTONE.AP.AP;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {


        // test 1
        /*for(int i=1; i<=100; i++) {
            run(i);
        }

        AP.fileWriter.close();*/



        // test 2
        /*test(75); // 5 15 75
        Writer.fileWriter.close();*/

        run(1);
        for(int i=5; i<=300; i+=5) {
            run(i);
        }

        AP.fileWriter.close();

    }

    public static void test(int numStation) {

        AP ap = new AP();
        ap.setNumStation(numStation);
        ap.initStaticArray();

        ap.init();
        ap.run();


    }

    public static void run(int numStation) throws IOException {

        AP ap = new AP();
        ap.setNumStation(numStation);
        ap.initStaticArray();

        int num_simulation = 5;
        for(int i=0; i<num_simulation; i++) {

            ap.init();
            ap.run();
            ap.writePerformance(); // 임시 저장

        }
        ap.printAvgPerformance(); // 시뮬레이션 평균 값 파일에 저장

    }

}
