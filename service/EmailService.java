package service;

import model.entities.Patient;

public class EmailService implements NotificationService {

    private Patient patient;

    public EmailService(Patient patient) {
        this.patient = patient;
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("📧 Email Notification");
        System.out.println("To: " + patient.getName() + " (" + patient.getEmail() + ")");
        System.out.println("Message: " + message);
        System.out.println("────────────────────────────\n");
    }
}
