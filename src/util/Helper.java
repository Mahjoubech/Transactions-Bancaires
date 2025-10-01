package util;

import java.util.Random;
import java.util.UUID;
public class Helper {
    public static String generateIDClient() {
        String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        return "CLI - " + uuidPart.toUpperCase();
    }
    public static String generateCompteCode() {
        Random rand = new Random();
        int number = rand.nextInt(100000); // 0 à 99999
        return String.format("CPT - %05d", number);
    }
    public static String generateCompteNum() {
        String datePart = java.time.LocalDate.now().toString().replace("-", "");
        String randomPart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "ACC - " + datePart + "-" + randomPart;
    }
}
