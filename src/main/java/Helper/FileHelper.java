package Helper;


import java.io.*;

public class FileHelper {
    public static void writeToFile(java.util.ArrayList<String> list, String fileName) throws IOException {
        FileWriter fw = new FileWriter(fileName);

        for (int i = 0; i < list.size(); i++) {
            fw.write(list.get(i)+"\n");
        }

        fw.close();
    }

    public static void writeToFile(String content, String fileName) throws IOException {
        FileWriter fw = new FileWriter(fileName);
        fw.write(content);
        fw.close();
    }

    public static boolean isFileExist(String fileName) {
        File f = new File(fileName);
        if (f.exists() && !f.isDirectory()) {
            return true;
        }
        return false;
    }

    public static java.util.ArrayList<String> readAllLines(String fileName) {
        java.util.ArrayList<String> result = new java.util.ArrayList<String>();
        try {

            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            result.add(line);
            while (line != null) {
                result.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
