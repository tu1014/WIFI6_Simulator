package UORA.Station;

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
    public void minus(int the_number_of_ru) {
        obo -= the_number_of_ru;
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
    public String toString() {
        return "" + obo;
    }
}
