package Helper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DrugLogger {
    public static void Log(String message) throws IOException {
        BufferedWriter output = new BufferedWriter(new FileWriter("log.txt",true));  //clears file every time
        output.append(message+"\n");
        output.close();
    }
}
