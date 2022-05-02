package MORA.AP.RU;

public class BusyTone {

    public boolean[] vru;

    public BusyTone(int num_bt) {

        if(num_bt == 0) num_bt = 1;
        vru = new boolean[num_bt];

        for(int i=0; i<num_bt; i++) vru[i] = false;

    }

}
