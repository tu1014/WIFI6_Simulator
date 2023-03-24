package BUSYTONE.Standard;


import BUSYTONE.AP.AP;
import BUSYTONE.OBOInterface;
import BUSYTONE.StationInterface;
import BUSYTONE.resource.RARU;
import BUSYTONE.resource.TriggerFrame;

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

    private int ru_id;

    private boolean sendFlag = false;

    private boolean[] myBusyTone;

    public StandardStation(OBOInterface obo) {
        id = idCounter++;
        this.obo = obo;
    }



    @Override
    public void receiveTF(TriggerFrame tf) {

        // System.out.println("<" + id + "번 STA>");
        // System.out.println("tf 수신 이전 obo : " + obo);

        // obo 감소
        obo.minus(tf.getTheNumberOfRARU());

        // 전송 가능하다면 비지톤을 전송해야함!
        if(obo.isAvailable()) {
            ru_id = random.nextInt(tf.getTheNumberOfRARU());
            sendFlag = true;

            if(tf.getN() != 0) {

                myBusyTone = new boolean[tf.getN()];

                for(int i=0; i<myBusyTone.length; i++) {
                    myBusyTone[i] = random.nextBoolean();
                }
            }
        }

    }

    public void sendBusyTone(TriggerFrame tf, int flagNumber) {
        if(obo.isAvailable() && sendFlag) {
            tf.bt_container[ru_id][flagNumber] |= myBusyTone[flagNumber];
        }
    }

    public void checkBusyTone(TriggerFrame tf, int flagNumber) {
        if(obo.isAvailable() && sendFlag == true && myBusyTone[flagNumber] == false) {
            boolean flag = tf.bt_container[ru_id][flagNumber];

            if(flag == true) { // 다른 sta의 busy톤을 감지했을 때
                sendFlag = false;
            }
        }
    }



    public void checkAndSendData(TriggerFrame tf) {
        // 전송 가능하다면 전송
        if(obo.isAvailable()) {

            if(sendFlag == true) {
                sendFlag = false;
                send(tf);
            }

            else {

                // fail로 간주한다 (비지톤 경쟁에서 졌을 떄)
                failCount++;
                obo.fail();

            }
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
        }
        else {
            failCount++;
            obo.fail();
        }

    }

    @Override
    public void send(TriggerFrame tf) {

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
    public String toString() {
        return id + "번 STA";
    }



}
