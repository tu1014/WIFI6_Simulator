package BUSYTONE;

public interface OBOInterface {

    public void minus(double... params);
    public void initOBO();
    public boolean isAvailable();
    public void success();
    public void fail();

    public void setStationNum(int stationNum);

    double getAlpha();

    double getAvgOCW();

}
