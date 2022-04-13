package MORA.AP;

public class APFactory {



    public static APInterface createStandardAP() {
        return new StandardAP();
    }

    public static APInterface createCustomAP() {
        return new CustomAP();
    }


}
