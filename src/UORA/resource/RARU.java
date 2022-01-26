package UORA.resource;

import UORA.Station.StationInterface;

import java.util.ArrayList;
import java.util.List;

public class RARU {

    public int id;
    private List<StationInterface> stations;

    public RARU(int id) {
        this.id = id;
        stations = new ArrayList<>();
    }

    public List<StationInterface> getStations() {return stations;}

    public void use(StationInterface station) {
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
