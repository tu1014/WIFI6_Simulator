package UORA.Station;

import UORA.Station.CustomCA.CustomChannelAccessOBO;
import UORA.Station.CustomCA.CustomChannelAccessStation;
import UORA.Station.Standard.StandardOBO;
import UORA.Station.Standard.StandardStation;

public class StationFactory {

    public static StationInterface createStandardStation() {
        return new StandardStation(new StandardOBO());
    }

    public static StationInterface createChannelAccessStation() {
        return new CustomChannelAccessStation(new CustomChannelAccessOBO());
    }


}
