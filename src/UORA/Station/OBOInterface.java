package UORA.Station;

public interface OBOInterface {

    public void minus(double... params);
    public void initOBO();
    public boolean isAvailable();
    public void success();
    public void fail();

    double getAlpha();

}
