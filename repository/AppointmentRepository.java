package repository;

import model.entities.Appointment;
import utils.FileUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class AppointmentRepository {

    private final String filePath;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public AppointmentRepository(String filePath) {
        this.filePath = filePath;
        FileUtil.ensureFileExists(filePath);
    }

    // Shto appointment të ri
    public void addAppointment(Appointment appointment) {
        FileUtil.writeLine(filePath, appointment.toFileString());
    }

    // Merr të gjitha appointment-et (duke përdorur listat e patient/doctor që i jep përdoruesi)
    public List<Appointment> getAllAppointments(List<model.entities.Patient> patients,
                                                List<model.entities.Doctor> doctors) {
        List<String> lines = FileUtil.readAll(filePath);
        List<Appointment> appointments = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split("\\|", -1);
            try {
                int id = Integer.parseInt(parts[0]);
                int patientId = Integer.parseInt(parts[1]);
                int doctorId = Integer.parseInt(parts[2]);
                Date date = sdf.parse(parts[3]);
                String statusStr = parts.length > 4 ? parts[4] : "SCHEDULED";
                String report = parts.length > 5 ? parts[5] : "";
                // restore any '|' replaced with '~'
                report = report.replace("~", "|");

                model.entities.Patient patient = patients.stream()
                        .filter(p -> p.getId() == patientId)
                        .findFirst()
                        .orElse(null);
                model.entities.Doctor doctor = doctors.stream()
                        .filter(d -> d.getId() == doctorId)
                        .findFirst()
                        .orElse(null);

                if (patient != null && doctor != null) {
                    model.enums.AppointmentStatus status;
                    try {
                        status = model.enums.AppointmentStatus.valueOf(statusStr);
                    } catch (IllegalArgumentException ex) {
                        status = model.enums.AppointmentStatus.SCHEDULED;
                    }

                    Appointment appointment = new Appointment(id, patient, doctor, date);
                    appointment.setStatus(status);
                    appointment.setReport(report);
                    appointments.add(appointment);
                } else {
                    // skip appointment lines where patient/doctor are missing
                    System.out.println("Warning: Appointment me id=" + id + " ka patient/doctor mungues.");
                }
            } catch (ParseException | NumberFormatException e) {
                System.out.println("Gabim parsing appointment line: " + line);
            }
        }
        return appointments;
    }

    // Përditëso appointment
    public void updateAppointment(Appointment updatedAppointment, List<model.entities.Patient> patients,
                                  List<model.entities.Doctor> doctors) {
        List<Appointment> appointments = getAllAppointments(patients, doctors);
        boolean found = false;
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getId() == updatedAppointment.getId()) {
                appointments.set(i, updatedAppointment);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Termini me ID " + updatedAppointment.getId() + " nuk u gjet për përditësim.");
            return;
        }
        List<String> lines = new ArrayList<>();
        for (Appointment a : appointments) lines.add(a.toFileString());
        FileUtil.overwriteFile(filePath, lines);
    }

    // Merr sipas ID
    public Optional<Appointment> getAppointmentById(int id, List<model.entities.Patient> patients,
                                                    List<model.entities.Doctor> doctors) {
        return getAllAppointments(patients, doctors).stream()
                .filter(a -> a.getId() == id)
                .findFirst();
    }
}
