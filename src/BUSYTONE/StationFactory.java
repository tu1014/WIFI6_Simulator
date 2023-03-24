package BUSYTONE;


import BUSYTONE.Standard.StandardOBO;
import BUSYTONE.Standard.StandardStation;

public class StationFactory {

    public static StationInterface createStandardStation() {
        return new StandardStation(new StandardOBO());
    }



}
