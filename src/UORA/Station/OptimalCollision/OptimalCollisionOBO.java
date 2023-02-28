package UORA.Station.OptimalCollision;

import UORA.Station.OBOInterface;

import java.util.Random;

public class OptimalCollisionOBO implements OBOInterface {

    private double obo;
    private double ocw;

    private double a = 1; // 알파
    private double newResultRate = 0.125; // 가중치

    private static double ocwMin = 8;
    private static double ocwMax = 1023;
    private static Random random = new Random();

    private double prevOCW = 0; // 성능 측정을 위한 값
    private int count = 0; // OCW 평균 필터에 사용

    private double failCount = 0;
    private double nru = -1;

    private int stationNum;

    private int collisionRateCount = 0;
    private double collisionRate = 0;
    private int num_trans = 0;
    private int num_collision = 0;

    private double OPTIMAL_RATE = 0.632;
    private double MARGIN = 0.03;
    private double K = 9.5;


    public OptimalCollisionOBO(int ocw) {
        this.ocw = ocw;
        initOBO();
    }


    @Override
    public void minus(double... params) {

        double num_raru = params[0];
        obo -= num_raru;
    }

    @Override
    public void initOBO() {
        obo = random.nextInt((int)ocw) + 1;
    }

    @Override
    public boolean isAvailable() {return (obo <= 0);}

    @Override
    public void success() {

        num_trans++;
        collisionRateCount++;
        double tmp = (double)num_collision / (double) num_trans;
        collisionRate = (double)(collisionRateCount-1)/(double)collisionRateCount * collisionRate + tmp/(double)collisionRateCount;

        initOCW();

        if(ocw < ocwMin) ocw = ocwMin;

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

    private void initOCW() {

        double e = collisionRate - OPTIMAL_RATE;

        if(e > MARGIN || e <-MARGIN)
            ocw = ocw * (1 + K * e);

        if(ocw > ocwMax) ocw = ocwMax;
        if(ocw < ocwMin) ocw = ocwMin;

    }

    @Override
    public void setStationNum(int stationNum) {
        this.stationNum = stationNum;
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
