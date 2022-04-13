package MORA.AP.RU;

import MORA.Station.StationInterface;

import java.util.ArrayList;

public class VRU {

    public ArrayList<StationInterface> stationList;

    public VRU() {stationList = new ArrayList<>();}

    public void use(StationInterface station) {
        stationList.add(station);
    }

    @Override
    public String toString() {
        String str = "[";
        str += stationList.size();
        str += "] ";

        return str;
    }
}
