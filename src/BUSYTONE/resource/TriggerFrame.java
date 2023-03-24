package BUSYTONE.resource;

import BUSYTONE.StationInterface;

import java.util.ArrayList;
import java.util.List;

public class TriggerFrame {

    public TriggerFrame(int num_station, double collision_ru_rate) {
        ruList = new ArrayList<>();
        the_number_of_sta = num_station;
        this.collision_ru_rate = collision_ru_rate;
    }

    private List<RARU> ruList;
    private int the_number_of_sta;

    public boolean[][] bt_container;

    private int n;

    public void initBusyTone(int n) {

        this.n = n;

        if(this.n > 0) {

            int numRU = ruList.size();
            bt_container = new boolean[numRU][n];

        }
    }

    public int getN() {return n;}


    private double collision_ru_rate;

    public double getCollision_ru_rate() { return collision_ru_rate;}

    public int getThe_number_of_sta() {
        return the_number_of_sta;
    }

    public int getTheNumberOfRARU() {
        return ruList.size();
    }

    // 예외 처리 필요
    public RARU getRARU(int index) {
        return ruList.get(index);
    }
    public List<RARU> getRuList() {return ruList;}
    public void addRU(RARU ru) {
        ruList.add(ru);
    }

    @Override
    public String toString() {
        return "TriggerFrame{" +
                "ruList=" + ruList +
                '}';
    }
}
