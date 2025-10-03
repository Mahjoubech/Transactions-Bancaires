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
    public static void validateVirementDetails(String lieu) {
        if (lieu == null || lieu.isBlank()) {
            throw new IllegalArgumentException("Les détails du virement ne doivent pas être vides !");
        }

        String[] detailParts = lieu.split("->");
        if (detailParts.length != 2) {
            throw new IllegalArgumentException(
                    "Détails de virement invalides. Format attendu: 'source->destination'"
            );
        }

        String source = detailParts[0].trim();
        String destination = detailParts[1].trim();

        if (source.isEmpty() || destination.isEmpty()) {
            throw new IllegalArgumentException("Source ou destination du virement ne peut pas être vide !");
        }
    }

}