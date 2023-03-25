package UORA.Station;

import UORA.Station.CA.ChannelAccessOBO;
import UORA.Station.DynamicCA.DynamicChannelAccessOBO;
import UORA.Station.DynamicCA.DynamicChannelAccessStation;
import UORA.Station.DynamicCA.TestOBO;
import UORA.Station.OptimalAlpha.OptimalAlphaOBO;
import UORA.Station.OptimalCollision.OptimalCollisionOBO;
import UORA.Station.OptimalCollision.OptimalCollisionStation;
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

    public static StationInterface createMyFinalIdea() { // 현재 내 최종 아이디어
        return new DynamicChannelAccessStation(new TestOBO(8));
    }

    public static StationInterface createOptimalCollisionStation() {
        return new OptimalCollisionStation(new OptimalCollisionOBO(8));
    }

    public static StationInterface createOptimalAlphaStation() {
        return new DynamicChannelAccessStation(new OptimalAlphaOBO());
    }


}
