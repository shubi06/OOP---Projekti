package ProjektiOOP;

import java.text.SimpleDateFormat;
import java.util.Date;

class Appointment {
    private int id;
    private Patient patient;
    private Doctor staff;
    private Date date;
    private String report;
    private AppointmentStatus status;

    public Appointment(int id, Patient patient, Doctor staff, Date date) {
        this.id = id;
        this.patient = patient;
        this.staff = staff;
        this.date = date;
        this.status = AppointmentStatus.SCHEDULED;
        this.report = "";
    }

    public int getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }


    public Doctor getStaff() {
        return staff;
    }


    public void setDate(Date date) {
        this.date = date;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getDetails() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        StringBuilder details = new StringBuilder();
        details.append("====================================\n");
        details.append("TERMINI ID: ").append(id).append("\n");
        details.append("Data: ").append(sdf.format(date)).append("\n");
        details.append("Statusi: ").append(status.getDescription()).append("\n");
        details.append("Pacienti: ").append(patient.getName()).append(" (ID: ").append(patient.getId()).append(")\n");
        details.append("Doktori: ").append(staff.getName()).append(" - ").append(staff.getSpecialty()).append("\n");
        if (status == AppointmentStatus.COMPLETED && !report.isEmpty()) {
            details.append("Raporti: ").append(report).append("\n");
        }
        details.append("====================================");
        return details.toString();
    }

    public String toFileString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return id + "|" + patient.getId() + "|" + staff.getId() + "|" +
                sdf.format(date) + "|" + status.name() + "|" + report.replace("|", "~");
    }
}
