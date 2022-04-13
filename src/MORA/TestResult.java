package MORA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.OptionalDouble;

public class TestResult {

    // 성능 출력을 위한 변수들
    public static ArrayList<Double> collisionRates = new ArrayList<>();
    public static ArrayList<Double> successRates = new ArrayList<>();
    public static ArrayList<Double> throughputs = new ArrayList<>();
    public static ArrayList<Integer> idleCounts = new ArrayList<>();

    public static void printPerformance() {

        double collisionRate = 0;
        double successRate = 0;
        double throughput = 0;
        double idleCount = 0;

        int num = collisionRates.size();

        for(int i=0; i<num; i++) {
            collisionRate += collisionRates.get(i);
            successRate += successRates.get(i);
            throughput += throughputs.get(i);
            idleCount += idleCounts.get(i);
        }

        collisionRate /= num;
        successRate /= num;
        throughput /= num;
        idleCount /= num;

        System.out.println("collisionRate : " + collisionRate);
        System.out.println("successRate : " + successRate);
        System.out.println("throughput : " + throughput);
        System.out.println("idleCount : " + idleCount);
    }


}
