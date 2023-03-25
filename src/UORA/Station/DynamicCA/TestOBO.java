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

    private double prevOCW = 0; // 성능 측정을 위한 값
    private int count = 0; // OCW 평균 필터에 사용

    // private double failCount = 0;
    private double nru = -1;

    private int stationNum;


    public TestOBO(int ocw) {
        this.ocw = ocw;
        initOBO();
    }


    @Override
    public void minus(double... params) {

        double num_raru = params[0];
        double num_station = params[1];
        double num_fail = params[2];
        double collision_ru_rate = params[3] / 100; // 아마 사용 안할 듯

        if(nru == -1) nru = num_raru;
        else nru = (((double)1) - newResultRate)*nru + newResultRate*num_raru;

        // failCount = num_fail;

        //////
        if(num_station <= num_raru) num_station = 1;
        else num_station -= (num_raru-1);
        //////

        double networkStatus;
        networkStatus = num_raru/(num_station+num_fail);
        // networkStatus = num_raru / (num_station + (1 + collision_ru_rate));


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

        // ocw = ocwMin + failCount;
        // ocw = ocw/2;
        // ocw = ocwMin + (nru - a)/nru*failCount;

        ocw = (1 + (nru - a)/nru/2) * ocwMin; // v2, 3, 4

        if(ocw < ocwMin) ocw = ocwMin;

        count++;
        prevOCW = (double)(count-1)/(double)count*prevOCW + (double)ocw/count;

        initOBO();
    }

    @Override
    public void fail() {

        // ocw = ocw + (ocwMin + failCount)/2; // best
        // ocw = ocw + ocwMin/2; // 기존 논문
        // ocw = ocw + (ocwMin + (nru - a)/nru*failCount)/2; // v1

        // ocw = ocw + ((1 + (nru - a)/nru/2) * ocwMin)/2; // v3
        ocw = ocw + ((nru - a)/nru * ocwMin)/2;         // v5

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
