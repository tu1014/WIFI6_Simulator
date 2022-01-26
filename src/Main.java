import UORA.AP.AP;

public class Main {

    public static void main(String[] args) {

        AP ap = new AP();

        for(int i=0; i<5; i++) {

            ap.init();
            ap.run();
            ap.printPerformance();

        }

    }
}
