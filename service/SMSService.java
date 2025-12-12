package service;

import model.entities.Patient;

public class SMSService implements NotificationService {

    private Patient patient;

    public SMSService(Patient patient) {
        this.patient = patient;
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("📱 SMS Notification");
        System.out.println("To: " + patient.getName() + " (" + patient.getPhone() + ")");
        System.out.println("Message: " + message);
        System.out.println("────────────────────────────\n");
    }
}