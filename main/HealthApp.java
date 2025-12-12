package main;

import model.entities.Appointment;
import model.entities.Doctor;
import model.entities.Patient;
import repository.AppointmentRepository;
import repository.DoctorRepository;
import repository.PatientRepository;
import service.EmailService;
import service.NotificationService;
import service.SMSService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class HealthApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        // use relative data folder
        String base = "./data/";
        String patientsFile = base + "patients.txt";
        String doctorsFile = base + "doctors.txt";
        String appointmentsFile = base + "appointments.txt";

        PatientRepository patientRepo = new PatientRepository(patientsFile);
        DoctorRepository doctorRepo = new DoctorRepository(doctorsFile);
        AppointmentRepository appointmentRepo = new AppointmentRepository(appointmentsFile);

        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- MENU ---");
            System.out.println("P - Menaxhim Pacient");
            System.out.println("D - Menaxhim Doktor");
            System.out.println("A - Menaxhim Termine");
            System.out.println("X - Dil nga programi");
            System.out.print("Zgjedh nje opsion: ");
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "P" -> handlePatient(patientRepo);
                case "D" -> handleDoctor(doctorRepo);
                case "A" -> handleAppointment(patientRepo, doctorRepo, appointmentRepo);
                case "X" -> exit = true;
                default -> System.out.println("Opsion i pavlefshëm.");
            }
        }
        System.out.println("Faleminderit! Po dalim.");
    }

    private static void handlePatient(PatientRepository patientRepo) {
        System.out.println("\n--- PACIENT MENU ---");
        System.out.println("1 - Krijo pacient të ri");
        System.out.println("2 - Modifiko pacient ekzistues");
        System.out.println("3 - Shiko të gjithë pacientët");
        System.out.print("Zgjedh nje opsion: ");
        String option = scanner.nextLine();

        switch (option) {
            case "1" -> {
                Integer id = readInteger("ID: ");
                String name = readString("Emri: ");
                String phone = readString("Telefoni: ");
                String email = readString("Email: ");
                Integer age = readInteger("Mosha: ");

                if (id != null && age != null) {
                    Patient newPatient = new Patient(id, name, phone, email, age);
                    patientRepo.addPatient(newPatient);
                    System.out.println("Pacienti u krijua me sukses!");
                }
            }
            case "2" -> {
                Integer editId = readInteger("Shkruaj ID e pacientit për modifikim: ");
                if (editId == null) return;

                Optional<Patient> patientOpt = patientRepo.getPatientById(editId);
                if (patientOpt.isPresent()) {
                    Patient p = patientOpt.get();
                    String newName = readString("Emri [" + p.getName() + "]: ", true);
                    if (!newName.isEmpty()) p.setName(newName);

                    String newPhone = readString("Telefoni [" + p.getPhone() + "]: ", true);
                    if (!newPhone.isEmpty()) p.setPhone(newPhone);

                    String newEmail = readString("Email [" + p.getEmail() + "]: ", true);
                    if (!newEmail.isEmpty()) p.setEmail(newEmail);

                    String ageStr = readString("Mosha [" + p.getAge() + "]: ", true);
                    if (!ageStr.isEmpty()) p.setAge(Integer.parseInt(ageStr));

                    patientRepo.updatePatient(p);
                    System.out.println("Pacienti u përditësua me sukses!");
                } else System.out.println("Pacienti nuk u gjet.");
            }
            case "3" -> {
                List<Patient> all = patientRepo.getAllPatients();
                if (all.isEmpty()) System.out.println("Nuk ka pacient të regjistruar.");
                for (Patient p : all) System.out.println(p.getDetails());
            }
            default -> System.out.println("Opsion i pavlefshëm.");
        }
    }

    private static void handleDoctor(DoctorRepository doctorRepo) {
        System.out.println("\n--- DOKTOR MENU ---");
        System.out.println("1 - Krijo doktor të ri");
        System.out.println("2 - Shiko të gjithë doktorët");
        System.out.print("Zgjedh nje opsion: ");
        String option = scanner.nextLine();

        if (option.equals("1")) {
            Integer id = readInteger("ID: ");
            String name = readString("Emri: ");
            String phone = readString("Telefoni: ");
            String email = readString("Email: ");
            String specialty = readString("Specialiteti: ");

            if (id != null) {
                Doctor newDoctor = new Doctor(id, name, phone, email, specialty);
                doctorRepo.addDoctor(newDoctor);
                System.out.println("Doktori u krijua me sukses!");
            }
        } else if (option.equals("2")) {
            List<Doctor> all = doctorRepo.getAllDoctors();
            if (all.isEmpty()) System.out.println("Nuk ka doktor të regjistruar.");
            for (Doctor d : all) System.out.println(d.getDetails());
        } else {
            System.out.println("Opsion i pavlefshëm.");
        }
    }

    private static void handleAppointment(PatientRepository patientRepo, DoctorRepository doctorRepo, AppointmentRepository appointmentRepo) {
        System.out.println("\n--- TERMIN MENU ---");
        System.out.println("1 - Planifiko termin");
        System.out.println("2 - Ndrysho statusin e terminit");
        System.out.println("3 - Shfaq termine sipas statusit");
        System.out.println("4 - Shfaq termine sipas pacientit (ID)");
        System.out.print("Zgjedh nje opsion: ");
        String option = scanner.nextLine();

        switch (option) {
            case "1" -> {
                Integer appId = readInteger("ID Termini: ");
                Integer patientId = readInteger("ID Pacienti: ");
                Integer doctorId = readInteger("ID Doktori: ");
                Date date = readDate("Data (dd/MM/yyyy HH:mm): ");
                if (appId == null || patientId == null || doctorId == null || date == null) {
                    System.out.println("Të dhëna të paplota.");
                    return;
                }

                List<Patient> patients = patientRepo.getAllPatients();
                List<Doctor> doctors = doctorRepo.getAllDoctors();
                Patient patient = patients.stream().filter(p -> p.getId() == patientId).findFirst().orElse(null);
                Doctor doctor = doctors.stream().filter(d -> d.getId() == doctorId).findFirst().orElse(null);

                if (patient != null && doctor != null) {
                    Appointment appointment = new Appointment(appId, patient, doctor, date);
                    appointmentRepo.addAppointment(appointment);
                    NotificationService service = new EmailService(patient);
                    service.sendNotification("Termini juaj u krijua: " + sdf.format(date));
                    System.out.println("Termini u krijua me sukses!");
                } else System.out.println("Pacienti ose Doktori nuk u gjet.");
            }
            case "2" -> {
                Integer editId = readInteger("ID Termini: ");
                if (editId == null) return;

                List<Patient> allPatients = patientRepo.getAllPatients();
                List<Doctor> allDoctors = doctorRepo.getAllDoctors();

                Optional<Appointment> appOpt = appointmentRepo.getAppointmentById(editId, allPatients, allDoctors);
                if (appOpt.isPresent()) {
                    Appointment appointment = appOpt.get();
                    String statusStr = readString("Status (SCHEDULED, COMPLETED, CANCELLED): ");
                    try {
                        appointment.setStatus(model.enums.AppointmentStatus.valueOf(statusStr.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Status i pavlefshëm.");
                        return;
                    }

                    if (appointment.getStatus() == model.enums.AppointmentStatus.COMPLETED) {
                        String report = readString("Raporti: ");
                        appointment.setReport(report);
                    }

                    appointmentRepo.updateAppointment(appointment, allPatients, allDoctors);
                    System.out.println("Statusi u përditësua me sukses!");
                } else System.out.println("Termini nuk u gjet.");
            }
            case "3" -> {
                String statusFilter = readString("Statusi (SCHEDULED, COMPLETED, CANCELLED): ").toUpperCase();
                List<Patient> allPats = patientRepo.getAllPatients();
                List<Doctor> allDocs = doctorRepo.getAllDoctors();

                List<Appointment> filtered = appointmentRepo.getAllAppointments(allPats, allDocs).stream()
                        .filter(a -> a.getStatus().name().equals(statusFilter)).toList();

                System.out.println("--- Termine Filtruara ---");
                if (filtered.isEmpty()) System.out.println("Nuk u gjet asnjë termin me këtë status.");
                for (Appointment a : filtered) System.out.println(a.getDetails());
            }
            case "4" -> {
                Integer pid = readInteger("ID Pacienti: ");
                if (pid == null) return;
                List<Patient> allPats = patientRepo.getAllPatients();
                List<Doctor> allDocs = doctorRepo.getAllDoctors();

                List<Appointment> filtered = appointmentRepo.getAllAppointments(allPats, allDocs).stream()
                        .filter(a -> a.getPatient().getId() == pid).toList();

                System.out.println("--- Termine për pacientin ID=" + pid + " ---");
                if (filtered.isEmpty()) System.out.println("Nuk u gjet asnjë termin për këtë pacient.");
                for (Appointment a : filtered) System.out.println(a.getDetails());
            }
            default -> System.out.println("Opsion i pavlefshëm.");
        }
    }

    private static Integer readInteger(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        try { return Integer.parseInt(input.trim()); }
        catch (NumberFormatException e) { System.out.println("Vlera nuk është numër i vlefshëm."); return null; }
    }

    private static String readString(String prompt) { return readString(prompt, false); }

    private static String readString(String prompt, boolean allowEmpty) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        if (!allowEmpty && input.trim().isEmpty()) return readString(prompt, allowEmpty);
        return input.trim();
    }

    private static Date readDate(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        try { return sdf.parse(input.trim()); }
        catch (ParseException e) { System.out.println("Format i gabuar i dates."); return null; }
    }
}
