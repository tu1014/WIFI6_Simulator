import UORA.AP.AP;

public class Main {

    public static void main(String[] args) {

        AP ap = new AP();

        int num_simulation = 50;

        /*for(int i=0; i<num_simulation; i++) {

            ap.init();
            // ap.run();
            // ap.dynamicRun();
            ap.dynamicStation();
            ap.writePerformance();

        }

        ap.printAvgPerformance();*/

        ap.printAlphaExpected();

    }
}
