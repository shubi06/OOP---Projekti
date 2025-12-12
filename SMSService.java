package ProjektiOOP;

public class SMSService implements NotificationService {
    private Person recipient;

    public SMSService(Person recipient) {
        this.recipient = recipient;
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("\n📱 ═══════════════════════════════════════");
        System.out.println("  NJOFTIM PËRMES SMS-IT");
        System.out.println("═══════════════════════════════════════");
        System.out.println("Marrësi: " + recipient.getName());
        System.out.println("Telefoni: " + recipient.getPhone());
        System.out.println("Mesazhi: " + message);
        System.out.println("═══════════════════════════════════════\n");
    }
}
