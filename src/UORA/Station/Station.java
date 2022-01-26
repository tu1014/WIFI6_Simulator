package UORA.Station;

import UORA.AP.AP;
import UORA.resource.RARU;
import UORA.resource.TriggerFrame;

import java.util.Random;

public class Station implements StationInterface {

    private static int idCounter = 0;
    private static Random random = new Random();

    private AP ap;
    private int id;
    private OBOInterface obo;

    public Station(OBOInterface obo) {
        id = idCounter++;
        this.obo = obo;
    }

    @Override
    public void receiveTF(TriggerFrame tf) {

        // System.out.println("<" + id + "번 STA>");
        // System.out.println("tf 수신 이전 obo : " + obo);

        // obo 감소
        obo.minus(tf.getTheNumberOfRARU());

        // System.out.println("tf 수신 이후 obo : " + obo);

        // 전송 가능하다면 전송
        if(obo.isAvailable()) {
            // System.out.println("전송");
            send(tf);
        }
    }

    @Override
    public void receiveACK(boolean isSuccess) {

        // System.out.println(id + "번 STA 전송 성공 여부 : " + isSuccess);

        if(isSuccess) obo.success();
        else obo.fail();

    }

    @Override
    public void send(TriggerFrame tf) {

        int ru_id = random.nextInt(tf.getTheNumberOfRARU());
        RARU ru = tf.getRARU(ru_id);
        ru.use(this);

    }

    @Override
    public String toString() {
        return id + "번 STA";
    }
}
