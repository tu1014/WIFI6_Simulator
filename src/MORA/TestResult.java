package MORA;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.OptionalDouble;

public class TestResult {

    // 성능 출력을 위한 변수들
    public static ArrayList<Double> collisionRates = new ArrayList<>();
    public static ArrayList<Double> successRates = new ArrayList<>();
    public static ArrayList<Double> throughputs = new ArrayList<>();
//    public static ArrayList<Integer> idleVRUCounts = new ArrayList<>();
    public static ArrayList<Double> idleRates = new ArrayList<>();

    public static FileWriter fileWriter;

    static {

        try {
            fileWriter = new FileWriter("MORA_TEST.txt", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initArrays() {
        collisionRates = new ArrayList<>();
        successRates = new ArrayList<>();
        throughputs = new ArrayList<>();
//        idleVRUCounts = new ArrayList<>();
        idleRates = new ArrayList<>();
    }

    public static void printPerformance() {

        double collisionRate = 0;
        double successRate = 0;
        double throughput = 0;
//        double idleVRUCount = 0;
        double idleRate = 0;

        int num = collisionRates.size();

        for(int i=0; i<num; i++) {
            collisionRate += collisionRates.get(i);
            successRate += successRates.get(i);
            throughput += throughputs.get(i);
//            idleVRUCount += idleVRUCounts.get(i);
            idleRate += idleRates.get(i);
        }

        collisionRate /= num;
        successRate /= num;
        throughput /= num;
//        idleVRUCount /= num;
        idleRate /= num;

        System.out.println("collisionRate : " + collisionRate);
        System.out.println("successRate : " + successRate);
        System.out.println("throughput : " + throughput);
//        System.out.println("IdleVRUCount : " + idleVRUCount);
        System.out.println("idleRate : " + idleRate);

        try {
            fileWriter.write(throughput + ",");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
