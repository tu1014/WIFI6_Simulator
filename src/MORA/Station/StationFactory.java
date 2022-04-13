package MORA.Station;

import MORA.Station.OBO.StandardOBO;

import java.util.ArrayList;

public class StationFactory {

    public static ArrayList<StationInterface> createStandardStations(int num_station) {
        ArrayList<StationInterface> stationList = new ArrayList<>();

        for(int i=0; i<num_station; i++) stationList.add(new StandardStation(new StandardOBO()));

        return stationList;
    }

    public static ArrayList<StationInterface> createCustomStation(int num_station) {
        ArrayList<StationInterface> stationList = new ArrayList<>();

        for(int i=0; i<num_station; i++) stationList.add(new CustomStation(new StandardOBO()));

        return stationList;
    }



}
