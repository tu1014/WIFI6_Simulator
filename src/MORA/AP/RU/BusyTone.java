package MORA.AP.RU;

public class BusyTone {

    public boolean[] vru;

    public BusyTone(int num_bt) {
        vru = new boolean[num_bt];

        for(int i=0; i<num_bt; i++) vru[i] = false;

    }

}
