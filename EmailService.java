package ProjektiOOP;

public class EmailService implements NotificationService {
    private Person recipient;

    public EmailService(Person recipient) {
        this.recipient = recipient;
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("\n📧 ═══════════════════════════════════════");
        System.out.println("  NJOFTIM PËRMES EMAIL-IT");
        System.out.println("═══════════════════════════════════════");
        System.out.println("Marrësi: " + recipient.getName());
        System.out.println("Email: " + recipient.getEmail());
        System.out.println("Mesazhi: " + message);
        System.out.println("═══════════════════════════════════════\n");
    }
}