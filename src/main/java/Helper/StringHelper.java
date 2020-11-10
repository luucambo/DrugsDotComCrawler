package Helper;

public class StringHelper {
    public static String removeNewlineAndTrim(String str) {
        return str.replace("\n", " ").replace("\r", " ").trim();
    }
}
