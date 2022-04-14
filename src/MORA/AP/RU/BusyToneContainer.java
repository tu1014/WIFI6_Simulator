package MORA.AP.RU;

import java.util.ArrayList;

public class BusyToneContainer {

    public BusyTone[][] bt;
    public ArrayList<Integer>[] isIdle;

    public BusyToneContainer(int num_ru, int num_vru, int num_bt) {

        bt = new BusyTone[num_ru][num_vru];

        for(int i=0; i<num_ru; i++) {
            for(int j=0; j<num_vru; j++) {
                bt[i][j] = new BusyTone(num_bt);
            }
        }

        isIdle = new ArrayList[num_ru];
        for(int i=0; i<isIdle.length; i++) isIdle[i] = new ArrayList<>();

    }

    public void findIdle() {
        int count = 0;
        for(int i=0; i<bt.length; i++) {
            for(int j=0; j<bt[i].length; j++) {
                int num_bt = bt[i][j].vru.length;
                for(int k=0; k<num_bt; k++) {
                    if(bt[i][j].vru[k] != true) { // 사용하지 않으면
                        isIdle[i].add(j); // isIdle에 낭비된 VRU를 기록
                        count++;
                    }
                }
            }
        }
        // System.out.println("idle vru : " + count);
    }


}
