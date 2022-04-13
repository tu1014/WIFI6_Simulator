package MORA.Station.OBO;

public interface OBOInterface {

    public void minus(int amount);
    public void initOBO();
    public boolean isAvailable();
    public void success();
    public void fail();
    public int getInitialValue();

}
