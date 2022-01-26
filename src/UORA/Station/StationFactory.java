package UORA.Station;

import UORA.Station.CA.ChannelAccessOBO;
import UORA.Station.DynamicCA.DynamicChannelAccessOBO;
import UORA.Station.DynamicCA.DynamicChannelAccessStation;
import UORA.Station.Standard.StandardOBO;
import UORA.Station.Standard.StandardStation;

public class StationFactory {

    public static StationInterface createStandardStation() {
        return new StandardStation(new StandardOBO());
    }

    public static StationInterface createDynamicChannelAccessStation() {
        return new DynamicChannelAccessStation(new DynamicChannelAccessOBO());
    }

    public static StationInterface createChannelAccessStation() {
        return new StandardStation(new ChannelAccessOBO());
    }



}
