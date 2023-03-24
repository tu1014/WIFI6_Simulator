package BUSYTONE.Standard;


import BUSYTONE.OBOInterface;

import java.util.Random;

public class StandardOBO implements OBOInterface {

    private double obo;

    private static int ocwMin = 8;
    private static int ocwMax = 64;
    private static Random random = new Random();

    private double a = 1; // 알파

    private double prevOCW = 0;
    private int count = 0;


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

        count++;
        prevOCW = (double)(count-1)/(double)count*prevOCW + (double)ocw/count;

        initOBO();
    }

    @Override
    public void fail() {
        ocw = 2*ocw + 1;
        if(ocw > ocwMax) ocw = ocwMax;

        count++;
        prevOCW = (double)(count-1)/(double)count*prevOCW + (double)ocw/count;

        initOBO();
    }

    @Override
    public void setStationNum(int stationNum) {
        this.ocw = (int)ocw;
    }

    @Override
    public double getAlpha() {
        return a;
    }

    @Override
    public double getAvgOCW() {
        return prevOCW;
    }

    @Override
    public String toString() {
        return "" + obo;
    }
}
