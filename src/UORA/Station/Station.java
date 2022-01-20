package UORA.Station;

import UORA.AP.AP;

public class Station implements StationInterface {

    private AP ap;

    private OBOInterface obo;

    public Station(OBOInterface obo) {
        this.obo = obo;
    }


    @Override
    public void sendToAP() {

    }

    @Override
    public void receiveTF() {

    }
}
