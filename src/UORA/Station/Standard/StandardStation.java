package UORA.Station.Standard;

import UORA.AP.AP;
import UORA.Station.OBOInterface;
import UORA.Station.StationInterface;
import UORA.resource.RARU;
import UORA.resource.TriggerFrame;

import java.util.Random;

public class StandardStation implements StationInterface {

    private static int idCounter = 0;
    private static Random random = new Random();

    private AP ap;
    private int id;
    private OBOInterface obo;

    private int failCount = 0;
    private double prevFailCount = 0;
    private int count = 0;


    private double total_dti = 0;
    private int current_dti = 0;



    public StandardStation(OBOInterface obo) {
        id = idCounter++;
        this.obo = obo;
    }

    @Override
    public void receiveTF(TriggerFrame tf) {

        // 지연시간 체크를 위한 코드
        current_dti++;


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

        if(isSuccess) {

            count++;
            prevFailCount = (double)(count-1)/(double)count*prevFailCount + (double)failCount/count;

            failCount = 0;

            obo.success();

            // 지연시간 체크를 위한 코드
            total_dti += current_dti;
            current_dti = 0;

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

    @Override
    public double getTotalDTI() {
        return total_dti;
    }


    @Override
    public String toString() {
        return id + "번 STA";
    }



}
