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

    public List<Station> getStations() {return stations;}

    public void use(Station station) {
        stations.add(station);
    }

    @Override
    public String toString() {
        return "RARU{" +
                "id=" + id +
                ", stations=" + stations +
                '}';
    }
}
