package UORA.resource;

import UORA.resource.RARU;

import java.util.ArrayList;
import java.util.List;

public class TriggerFrame {

    public TriggerFrame(int num_station) {
        ruList = new ArrayList<>();
        the_number_of_sta = num_station;
    }

    private List<RARU> ruList;
    private int the_number_of_sta;

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
