package UORA.Station.DynamicCA;

import UORA.AP.AP;
import UORA.Station.OBOInterface;
import UORA.Station.StationInterface;
import UORA.resource.RARU;
import UORA.resource.TriggerFrame;

import java.util.Random;

public class DynamicChannelAccessStation implements StationInterface {

    private static int idCounter = 0;
    private static Random random = new Random();

    private AP ap;
    private int id;
    private OBOInterface obo;

    private int failCount = 0;

    public DynamicChannelAccessStation(OBOInterface obo) {
        id = idCounter++;
        this.obo = obo;
    }

    @Override
    public void receiveTF(TriggerFrame tf) {

        // System.out.println("<" + id + "번 STA>");
        // System.out.println("tf 수신 이전 obo : " + obo);

        // obo 감소
        obo.minus(
                tf.getTheNumberOfRARU(),
                tf.getThe_number_of_sta(),
                failCount
                );

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

        if(isSuccess) {
            failCount = 0;
            obo.success();
        }
        else {
            failCount++;
            obo.fail();
        }

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
