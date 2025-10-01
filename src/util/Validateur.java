package util;

public class Validateur {
    public static boolean isValidCompteCode(String code) {
        return code != null && code.matches("CPT - \\d{5}");
    }
    public static boolean isPositiveAmount(double montant) {
        return montant > 0;
    }
    public static boolean isPositiveDouble(double value) {
        return value >= 0;
    }
    public static boolean isValidClientId(String id) {
        return id != null && id.matches("CLI - [A-Z0-9]{10,}");
    }
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

}