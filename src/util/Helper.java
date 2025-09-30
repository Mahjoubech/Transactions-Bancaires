package util;

import java.util.UUID;

public class Helper {
    public static String generateIDNum(){
        String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        return "CPT - " + uuidPart.toUpperCase();
    }
}
