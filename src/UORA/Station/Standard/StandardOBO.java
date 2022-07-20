package UORA.Station.Standard;

import UORA.Station.OBOInterface;

import java.util.Random;

public class StandardOBO implements OBOInterface {

    private double obo;

    private static int ocwMin = 8;
    private static int ocwMax = 64;
    private static Random random = new Random();


    private int ocw;

    public StandardOBO() {
        ocw = ocwMin;
        initOBO();
    }

    @Override
    // param : raru의 수
    public void minus(double... params) {
        obo -= params[0];
    }

    @Override
    public void initOBO() {
        obo = random.nextInt(ocw) + 1;
    }

    @Override
    public boolean isAvailable() {
        return (obo <= 0);
    }

    @Override
    public void success() {
        ocw = ocwMin;
        initOBO();
    }

    @Override
    public void fail() {
        ocw = 2*ocw + 1;
        if(ocw > ocwMax) ocw = ocwMax;
        initOBO();
    }

    @Override
    public double getAlpha() {
        return 0;
    }

    @Override
    public String toString() {
        return "" + obo;
    }
}
