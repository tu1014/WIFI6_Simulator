package MORA.Station.OBO;

import java.util.Random;

public class StandardOBO implements OBOInterface {

    private int obo;
    private int ocw;
    private int initialObo;

    private static int OCW_MIN = 16;
    private static int OCW_MAX = 1024;
    private static Random random = new Random();

    public StandardOBO() {
        ocw = OCW_MIN;
        initOBO();
    }

    @Override
    public void minus(int amount) {
        obo -= amount;
    }

    @Override
    public void initOBO() {
        obo = random.nextInt(ocw)+1;
        initialObo = obo;
    }

    @Override
    public boolean isAvailable() {
        return (obo <= 0);
    }

    @Override
    public void success() {
        ocw = OCW_MIN;
        initOBO();
    }

    @Override
    public void fail() {
        ocw = 2*ocw;
        if(ocw > OCW_MAX) ocw = OCW_MAX;
        initOBO();
    }

    @Override
    public int getInitialValue() {
        return initialObo;
    }
}
