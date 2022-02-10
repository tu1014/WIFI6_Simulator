package UORA.Station.DynamicCA;

import UORA.AP.AP;
import UORA.Station.OBOInterface;
import UORA.Station.StationInterface;
import UORA.resource.RARU;
import UORA.resource.TriggerFrame;

import java.util.Random;

public class DynamicChannelAccessStation implements StationInterface {

    private static Random random = new Random();

    private OBOInterface obo;

    // 네트워크 상태를 파악하기 위한 변수
    // 전송에 성공하면 초기화
    private int failCount = 0;

    public DynamicChannelAccessStation(OBOInterface obo) {
        this.obo = obo;
    }

    @Override
    public void receiveTF(TriggerFrame tf) {

        // obo 감소
        obo.minus(
                tf.getTheNumberOfRARU(),
                tf.getThe_number_of_sta(),
                failCount
                );

        // 전송 가능하다면 전송
        if(obo.isAvailable()) {
            send(tf);
        }
    }

    @Override
    public void receiveACK(boolean isSuccess) {

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

}
