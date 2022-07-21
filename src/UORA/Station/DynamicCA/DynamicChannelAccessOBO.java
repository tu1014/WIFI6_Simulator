package UORA.Station.DynamicCA;

import UORA.Station.OBOInterface;

import java.util.Random;

public class DynamicChannelAccessOBO implements OBOInterface {

    private double obo;
    private double ocw;

    private double a = 1; // 알파
    private double newResultRate = 0.125; // 가중치

    private static double A_MIN = 0.01;
    // private static double A_MIN = 0.1;
    private static double A_MAX = 10;

    private static double ocwMin = 8;
    private static double ocwMax = 64;
    private static Random random = new Random();


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
        double tmp;

        if(networkStatus >= 1) {
            tmp = ((double)1) + (networkStatus * ocwMin / ocwMax);
        } else {
            tmp = ((double)1) - (ocwMin/ocwMax/networkStatus);
        }

        if(tmp < A_MIN) tmp = A_MIN;
        if(tmp > A_MAX) tmp = A_MAX;

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
        ocw = ocw/2;
        if(ocw < ocwMin) ocw = ocwMin;
        initOBO();
    }

    @Override
    public void fail() {
        ocw = ocw + ocwMin/2;
        if(ocw > ocwMax) ocw = ocwMax;
        initOBO();
    }

    @Override
    public double getAlpha() {
        return a;
    }
}
