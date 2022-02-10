package UORA.Station.CA;

import UORA.Station.OBOInterface;

import java.util.Random;

// 논문에서 제안한 알고리즘
public class ChannelAccessOBO implements OBOInterface {

    private double obo;
    private double ocw;

    private double a = 0.5; // 알파

    private static double ocwMin = 8;
    private static double ocwMax = 64;
    private static Random random = new Random();


    @Override
    public void minus(double... params) {

        double num_raru = params[0];

        obo -= a*num_raru;


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
}
