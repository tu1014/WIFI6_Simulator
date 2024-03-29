package UORA.Station.OptimalAlpha;

import UORA.Station.OBOInterface;

import java.util.Random;

public class OptimalAlphaOBO implements OBOInterface {

    private double obo;
    private double ocw;

    private double a = 1; // 알파
    private double newResultRate = 0.125; // 가중치

    private static double A_MIN = 0.1;
    // private static double A_MIN = 0.1;
    private static double A_MAX = 10;

    private static double ocwMin = 8;
    private static double ocwMax = 1024;
    private static Random random = new Random();

    private double prevOCW = 0;
    private int count = 0;

    private int collisionRateCount = 0;
    private double collisionRate = 0;
    private int num_trans = 0;
    private int num_collision = 0;

    private double OPTIMAL_COLLISION_RATE = 0.632;
    private double MARGIN = 0.03;
    private double K = 10;

    public OptimalAlphaOBO() {
        ocw = ocwMin;
        initOBO();
    }


    @Override
    public void minus(double... params) {

        double num_raru = params[0];
        double num_station = params[1];
        double num_fail = params[2];

        double networkStatus = num_raru/(num_station + num_fail);

        calAlpha(networkStatus);
        obo -= a*num_raru;

    }

    public void calAlpha(double networkStatus) {
        double tmp = networkStatus;

        /*if(networkStatus >= 1) {
            tmp = ((double)1) + (networkStatus * ocwMin / ocwMax);
        } else {
            tmp = ((double)1) - (ocwMin/ocwMax/networkStatus);
        }

        if(tmp < A_MIN) tmp = A_MIN;
        if(tmp > A_MAX) tmp = A_MAX;*/

        a = (((double)1) - newResultRate)*a + newResultRate*tmp;

    }

    @Override
    public void initOBO() {
        obo = random.nextInt((int)ocw) + 1;
    }

    @Override
    public boolean isAvailable() {
        return (obo <= 0);
    }

    private void initOCW() {
        double e = collisionRate - OPTIMAL_COLLISION_RATE;

        if(e > MARGIN || e <-MARGIN)
            ocw = ocw * (1 + K * e);

        if(ocw > ocwMax) ocw = ocwMax;
        if(ocw < ocwMin) ocw = ocwMin;
    }

    @Override
    public void success() {

        num_trans++;
        collisionRateCount++;
        double tmp = (double)num_collision / (double) num_trans;
        collisionRate = (double)(collisionRateCount-1)/(double)collisionRateCount * collisionRate + tmp/(double)collisionRateCount;

        initOCW();

        count++;
        prevOCW = (double)(count-1)/(double)count*prevOCW + (double)ocw/count;

        initOBO();
    }

    @Override
    public void fail() {

        num_trans++;
        num_collision++;
        collisionRateCount++;
        double tmp = (double)num_collision / (double) num_trans;
        collisionRate = (double)(collisionRateCount-1)/(double)collisionRateCount * collisionRate + tmp/(double)collisionRateCount;

        initOCW();

        count++;
        prevOCW = (double)(count-1)/(double)count*prevOCW + (double)ocw/count;

        initOBO();
    }

    @Override
    public void setStationNum(int stationNum) {

    }

    @Override
    public double getAlpha() {
        return a;
    }

    @Override
    public double getAvgOCW() {
        return prevOCW;
    }
}
