package MORA.AP.RU;

import java.util.Arrays;

public class RU {

    public VRU[][] ru;

    public RU(int num_ru, int num_vru) {

        ru = new VRU[num_ru][num_vru];

        for(int i=0; i<num_ru; i++) {
            for(int j=0; j<num_vru; j++) {
                ru[i][j] = new VRU();
            }
        }
    }

    @Override
    public String toString() {
        String str = "";

        str += "****************\n";
        for(int i=0; i<ru.length; i++) {
            for(int j=0; j<ru[i].length; j++) {
                str += ru[i][j].toString();
            }
            str += "\n";
        }

        return str;

    }
}
