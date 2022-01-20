package UORA.resource;

import UORA.resource.RARU;

import java.util.List;

public class TriggerFrame {

    private List<RARU> ruList;
    private int the_number_of_sta;

    public int getTheNumberOfRARU() {
        return ruList.size();
    }

    // 예외 처리 필요
    public RARU getRARU(int index) {
        return ruList.get(index);
    }

}
