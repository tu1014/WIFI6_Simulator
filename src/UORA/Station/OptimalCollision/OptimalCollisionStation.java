package UORA.Station.OptimalCollision;

import UORA.Station.OBOInterface;
import UORA.Station.StationInterface;
import UORA.resource.RARU;
import UORA.resource.TriggerFrame;

import java.util.Random;

public class OptimalCollisionStation implements StationInterface {

    private static Random random = new Random();

    private OBOInterface obo;

    // 네트워크 상태를 파악하기 위한 변수
    // 전송에 성공하면 초기화
    private int failCount = 0;
    private double prevFailCount = 0;
    private double filteredFailCount = 0;
    private int count = 0;



    public OptimalCollisionStation(OBOInterface obo) {
        this.obo = obo;
    }

    @Override
    public void receiveTF(TriggerFrame tf) {

        obo.setStationNum(tf.getThe_number_of_sta());

        // obo 감소
        obo.minus(
                tf.getTheNumberOfRARU()
        );

        // 전송 가능하다면 전송
        if(obo.isAvailable()) {
            send(tf);
        }
    }

    @Override
    public void receiveACK(boolean isSuccess) {

        if(isSuccess) {
            // 원래 코드
            count++;
            prevFailCount = (double)(count-1)/(double)count*prevFailCount + (double)filteredFailCount/count;

            filteredFailCount = 0.7*filteredFailCount + 0.3*failCount;
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
    public double getAlpha() {
        return obo.getAlpha();
    }

    @Override
    public double getFailCount() {
        return prevFailCount;
    }

    @Override
    public double getAvgOCW() {
        return obo.getAvgOCW();
    }

}
