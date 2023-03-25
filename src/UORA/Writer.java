package UORA;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Writer {
    public static FileWriter fileWriter;

    static {
        try {
            fileWriter = new FileWriter("time-UORA.txt", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
