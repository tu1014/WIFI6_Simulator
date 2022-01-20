package UORA.Station;

public class StationFactory {

    public static Station createStandardStation() {
        return new Station(new StandardOBO());
    }

    public static Station createChannelAccessStation() {
        return new Station(new StandardOBO());
    }


}
