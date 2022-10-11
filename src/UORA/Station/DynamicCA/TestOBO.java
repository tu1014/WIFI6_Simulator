package UORA.Station.DynamicCA;

import UORA.Station.OBOInterface;

import java.util.Random;

public class TestOBO implements OBOInterface {

    private double obo;
    private double ocw;

    private double a = 1; // 알파
    private double newResultRate = 0.125; // 가중치

    private static double A_MIN = 0.1;
    // private static double A_MIN = 0.1;
    private static double A_MAX = 10;

    private static double ocwMin = 8;
    private static double ocwMax = 64;
    private static Random random = new Random();

    private double prevOCW = 0;
    private int count = 0;

    // double failCount = 0;

    private int stationNum;

    public TestOBO() {
        ocw = 10;
        initOBO();


    }

    public TestOBO(int ocw) {
        this.ocw = ocw;
        initOBO();
    }


    @Override
    public void minus(double... params) {

        double num_raru = params[0];
        double num_station = params[1];
        double num_fail = params[2];

        //////
        if(num_station <= num_raru) num_station = 1;
        else num_station -= num_raru;
        //////

        double networkStatus = num_raru/(num_station);

        calAlpha(networkStatus);
        obo -= a*num_raru;

        ocwMin = num_raru;
        ocwMax = ocwMin * 8;


    }

    public void calAlpha(double networkStatus) {
        double tmp = networkStatus;
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

    @Override
    public void success() {

        // ocw = ocwMin + (1/a)/2;

        ocw = ocw/2;
        if(ocw < ocwMin) ocw = ocwMin;

        count++;
        prevOCW = (double)(count-1)/(double)count*prevOCW + (double)ocw/count;

        initOBO();
    }

    @Override
    public void fail() {
        // ocw = ocw + ocwMin/2;
        // ocw = ocw * 2;
        // ocw = stationNum;
        // ocw= 10;

        // ocw = ocw + (1/a)/2;
        ocw = ocw + ocwMin/2;
        if(ocw > ocwMax) ocw = ocwMax;

        count++;
        prevOCW = (double)(count-1)/(double)count*prevOCW + (double)ocw/count;
        initOBO();
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
