package UORA;

import UORA.AP.AP;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {


        // test 1

        /*for(int i=1; i<=100; i++) {
            test1(i);
        }

        AP.fileWriter.close();*/



        // test 2
        // test2(75); // 5 15 75


        // test3
        test1(1);
        for(int i = 5; i<=100; i+=5)
            test1(i);

        AP.fileWriter.close();

        /*test1(50);
        AP.fileWriter.close();*/


    }

    public static void test2(int numStation) throws IOException {

        AP ap = new AP();
        ap.setNumStation(numStation);
        ap.initStaticArray();
        ap.setTIME_FLAG(true);

        ap.init();
        ap.run();

        Writer.fileWriter.close();

    }

    public static void test1(int numStation) throws IOException {

        AP ap = new AP();
        ap.setNumStation(numStation);
        ap.initStaticArray();

        int num_simulation = 5;
        for(int i=0; i<num_simulation; i++) {

            ap.init();
            ap.run();
            // ap.dynamicStation();
            ap.writePerformance(); // 임시 저장

        }
        ap.printAvgPerformance(); // 시뮬레이션 평균 값 파일에 저장

    }

}
