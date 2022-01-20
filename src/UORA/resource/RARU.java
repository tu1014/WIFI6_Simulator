package UORA.resource;

import UORA.Station.Station;

import java.util.ArrayList;
import java.util.List;

public class RARU {

    public int id;
    private List<Station> stations;

    public RARU(int id) {
        this.id = id;
        stations = new ArrayList<>();
    }

    public void use(Station station) {
        stations.add(station);
    }

}
