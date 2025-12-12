package ProjektiOOP;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HealthApp {
    private static final String PATIENTS_FILE = "patients.txt";
    private static final String DOCTORS_FILE = "doctors.txt";
    private static final String APPOINTMENTS_FILE = "appointments.txt";

    private List<Patient> patients;
    private List<Doctor> doctors;
    private List<Appointment> appointments;
    private Scanner scanner;

    public HealthApp() {
        patients = new ArrayList<>();
        doctors = new ArrayList<>();
        appointments = new ArrayList<>();
        scanner = new Scanner(System.in);
        loadData();
    }

    // ==================== METODA PËR TË DHËNAT ====================

    private void loadData() {
        loadPatients();
        loadDoctors();
        loadAppointments();
    }

    private void loadPatients() {
        try (BufferedReader br = new BufferedReader(new FileReader(PATIENTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                patients.add(Patient.fromFileString(line));
            }
        } catch (IOException e) {
            System.out.println("Nuk ka të dhëna për pacientë. Do të krijohet një file i ri.");
        }
    }

    private void loadDoctors() {
        try (BufferedReader br = new BufferedReader(new FileReader(DOCTORS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                doctors.add(Doctor.fromFileString(line));
            }
        } catch (IOException e) {
            System.out.println("Nuk ka të dhëna për doktorë. Do të krijohet një file i ri.");
        }
    }

    private void loadAppointments() {
        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENTS_FILE))) {
            String line;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                int id = Integer.parseInt(parts[0]);
                int patientId = Integer.parseInt(parts[1]);
                int doctorId = Integer.parseInt(parts[2]);
                Date date = sdf.parse(parts[3]);
                AppointmentStatus status = AppointmentStatus.valueOf(parts[4]);
                String report = parts.length > 5 ? parts[5].replace("~", "|") : "";

                Patient patient = findPatientById(patientId);
                Doctor doctor = findDoctorById(doctorId);

                if (patient != null && doctor != null) {
                    Appointment appointment = new Appointment(id, patient, doctor, date);
                    appointment.setStatus(status);
                    appointment.setReport(report);
                    appointments.add(appointment);
                }
            }
        } catch (IOException | ParseException e) {
            System.out.println("Nuk ka të dhëna për termine. Do të krijohet një file i ri.");
        }
    }

    private void savePatients() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PATIENTS_FILE))) {
            for (Patient patient : patients) {
                pw.println(patient.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Gabim në ruajtjen e pacientëve: " + e.getMessage());
        }
    }

    private void saveDoctors() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DOCTORS_FILE))) {
            for (Doctor doctor : doctors) {
                pw.println(doctor.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Gabim në ruajtjen e doktorëve: " + e.getMessage());
        }
    }

    private void saveAppointments() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(APPOINTMENTS_FILE))) {
            for (Appointment appointment : appointments) {
                pw.println(appointment.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Gabim në ruajtjen e termineve: " + e.getMessage());
        }
    }

    // ==================== METODA PËR PACIENTËT ====================

    public void registerPatient(String name, String phone, String email, int age) {
        int id = patients.isEmpty() ? 1 : patients.get(patients.size() - 1).getId() + 1;
        Patient patient = new Patient(id, name, phone, email, age);
        patients.add(patient);
        savePatients();
        System.out.println("\n✓ Pacienti u regjistrua me sukses!");
        System.out.println(patient.getDetails());
    }

    public void updatePatient(int id, String name, String phone, String email, int age) {
        Patient patient = findPatientById(id);
        if (patient != null) {
            patient.setName(name);
            patient.setPhone(phone);
            patient.setEmail(email);
            patient.setAge(age);
            savePatients();
            System.out.println("\n✓ Pacienti u përditësua me sukses!");
            System.out.println(patient.getDetails());
        } else {
            System.out.println("\n✗ Pacienti me ID " + id + " nuk u gjet!");
        }
    }

    public void deletePatient(int id) {
        Patient patient = findPatientById(id);
        if (patient != null) {
            patients.remove(patient);
            savePatients();
            System.out.println("\n✓ Pacienti u fshi me sukses!");
        } else {
            System.out.println("\n✗ Pacienti me ID " + id + " nuk u gjet!");
        }
    }

    public void listPatients() {
        if (patients.isEmpty()) {
            System.out.println("\nNuk ka pacientë të regjistruar.");
            return;
        }
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("  LISTA E PACIENTËVE");
        System.out.println("═══════════════════════════════════════");
        for (Patient patient : patients) {
            System.out.println(patient.getDetails());
            System.out.println("───────────────────────────────────────");
        }
    }

    // ==================== METODA PËR DOKTORËT ====================

    public void registerDoctor(String name, String phone, String email, String specialty) {
        int id = doctors.isEmpty() ? 1 : doctors.get(doctors.size() - 1).getId() + 1;
        Doctor doctor = new Doctor(id, name, phone, email, specialty);
        doctors.add(doctor);
        saveDoctors();
        System.out.println("\n✓ Doktori u regjistrua me sukses!");
        System.out.println(doctor.getDetails());
    }

    public void updateDoctor(int id, String name, String phone, String email, String specialty) {
        Doctor doctor = findDoctorById(id);
        if (doctor != null) {
            doctor.setName(name);
            doctor.setPhone(phone);
            doctor.setEmail(email);
            doctor.setSpecialty(specialty);
            saveDoctors();
            System.out.println("\n✓ Doktori u përditësua me sukses!");
            System.out.println(doctor.getDetails());
        } else {
            System.out.println("\n✗ Doktori me ID " + id + " nuk u gjet!");
        }
    }

    public void deleteDoctor(int id) {
        Doctor doctor = findDoctorById(id);
        if (doctor != null) {
            doctors.remove(doctor);
            saveDoctors();
            System.out.println("\n✓ Doktori u fshi me sukses!");
        } else {
            System.out.println("\n✗ Doktori me ID " + id + " nuk u gjet!");
        }
    }

    public void listDoctors() {
        if (doctors.isEmpty()) {
            System.out.println("\nNuk ka doktorë të regjistruar.");
            return;
        }
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("  LISTA E DOKTORËVE");
        System.out.println("═══════════════════════════════════════");
        for (Doctor doctor : doctors) {
            System.out.println(doctor.getDetails());
            System.out.println("───────────────────────────────────────");
        }
    }

    // ==================== METODA PËR TERMINET ====================

    public void scheduleAppointment(int patientId, int doctorId, Date date) {
        Patient patient = findPatientById(patientId);
        Doctor doctor = findDoctorById(doctorId);

        if (patient == null) {
            System.out.println("\n✗ Pacienti me ID " + patientId + " nuk u gjet!");
            return;
        }
        if (doctor == null) {
            System.out.println("\n✗ Doktori me ID " + doctorId + " nuk u gjet!");
            return;
        }

        int id = appointments.isEmpty() ? 1 : appointments.get(appointments.size() - 1).getId() + 1;
        Appointment appointment = new Appointment(id, patient, doctor, date);
        appointments.add(appointment);
        saveAppointments();

        System.out.println("\n✓ Termini u planifikua me sukses!");
        System.out.println(appointment.getDetails());

        // Dërgimi i njoftimit
        sendNotification(patient, "Termini juaj me Dr. " + doctor.getName() +
                " është planifikuar për " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date));
    }

    public void completeAppointment(int appointmentId, String report) {
        Appointment appointment = findAppointmentById(appointmentId);
        if (appointment != null) {
            appointment.setStatus(AppointmentStatus.COMPLETED);
            appointment.setReport(report);
            saveAppointments();
            System.out.println("\n✓ Termini u shënua si i përfunduar!");
            System.out.println(appointment.getDetails());

            sendNotification(appointment.getPatient(),
                    "Termini juaj me Dr. " + appointment.getStaff().getName() + " u përfundua.");
        } else {
            System.out.println("\n✗ Termini me ID " + appointmentId + " nuk u gjet!");
        }
    }

    public void cancelAppointment(int appointmentId) {
        Appointment appointment = findAppointmentById(appointmentId);
        if (appointment != null) {
            appointment.setStatus(AppointmentStatus.CANCELLED);
            saveAppointments();
            System.out.println("\n✓ Termini u anulua!");
            System.out.println(appointment.getDetails());

            sendNotification(appointment.getPatient(),
                    "Termini juaj me Dr. " + appointment.getStaff().getName() + " është anuluar.");
        } else {
            System.out.println("\n✗ Termini me ID " + appointmentId + " nuk u gjet!");
        }
    }

    public void listAppointmentsByPatient(int patientId) {
        List<Appointment> patientAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getId() == patientId) {
                patientAppointments.add(appointment);
            }
        }

        if (patientAppointments.isEmpty()) {
            System.out.println("\nNuk ka termine për këtë pacient.");
            return;
        }

        System.out.println("\n═══════════════════════════════════════");
        System.out.println("  TERMINET E PACIENTIT");
        System.out.println("═══════════════════════════════════════");
        for (Appointment appointment : patientAppointments) {
            System.out.println(appointment.getDetails());
        }
    }

    public void listAppointmentsByStatus(AppointmentStatus status) {
        List<Appointment> filteredAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getStatus() == status) {
                filteredAppointments.add(appointment);
            }
        }

        if (filteredAppointments.isEmpty()) {
            System.out.println("\nNuk ka termine me statusin: " + status.getDescription());
            return;
        }

        System.out.println("\n═══════════════════════════════════════");
        System.out.println("  TERMINET - " + status.getDescription().toUpperCase());
        System.out.println("═══════════════════════════════════════");
        for (Appointment appointment : filteredAppointments) {
            System.out.println(appointment.getDetails());
        }
    }

    public void listAllAppointments() {
        if (appointments.isEmpty()) {
            System.out.println("\nNuk ka termine të planifikuara.");
            return;
        }
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("  TË GJITHË TERMINET");
        System.out.println("═══════════════════════════════════════");
        for (Appointment appointment : appointments) {
            System.out.println(appointment.getDetails());
        }
    }

    // ==================== METODA PËR NJOFTIME ====================

    public void sendNotification(Person person, String message) {
        System.out.println("\n▶ Zgjidhni mënyrën e dërgimit të njoftimit:");
        System.out.println("1. Email");
        System.out.println("2. SMS");
        System.out.print("Zgjedhja juaj: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        NotificationService notificationService;
        if (choice == 1) {
            notificationService = new EmailService(person);
        } else {
            notificationService = new SMSService(person);
        }

        notificationService.sendNotification(message);
    }

    // ==================== METODA NDIHMËSE ====================

    private Patient findPatientById(int id) {
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                return patient;
            }
        }
        return null;
    }

    private Doctor findDoctorById(int id) {
        for (Doctor doctor : doctors) {
            if (doctor.getId() == id) {
                return doctor;
            }
        }
        return null;
    }

    private Appointment findAppointmentById(int id) {
        for (Appointment appointment : appointments) {
            if (appointment.getId() == id) {
                return appointment;
            }
        }
        return null;
    }

    // ==================== MENU INTERFACE ====================

    public void start() {
        boolean running = true;

        while (running) {
            showMainMenu();
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "P":
                    handlePatientMenu();
                    break;
                case "D":
                    handleDoctorMenu();
                    break;
                case "T":
                    handleAppointmentMenu();
                    break;
                case "X":
                    System.out.println("\n👋 Faleminderit që përdorët Sistemin e Menaxhimit të Shëndetit!");
                    running = false;
                    break;
                default:
                    System.out.println("\n✗ Zgjedhje e pavlefshme! Ju lutem provoni përsëri.");
            }
        }
        scanner.close();
    }

    private void showMainMenu() {
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║  SISTEMI I MENAXHIMIT TË SHËNDETIT    ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println("P - Menaxhimi i Pacientëve");
        System.out.println("D - Menaxhimi i Doktorëve");
        System.out.println("T - Menaxhimi i Termineve");
        System.out.println("X - Dalje");
        System.out.print("\nShtyp opsionin: ");
    }

    private void handlePatientMenu() {
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║     MENAXHIMI I PACIENTËVE            ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println("1 - Regjistro Pacient");
        System.out.println("2 - Modifiko Pacient");
        System.out.println("3 - Fshi Pacient");
        System.out.println("4 - Shfaq të Gjithë Pacientët");
        System.out.println("5 - Shfaq Terminet e Pacientit");
        System.out.println("0 - Kthehu");
        System.out.print("\nShtyp opsionin: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Emri: ");
                String name = scanner.nextLine();
                System.out.print("Telefoni: ");
                String phone = scanner.nextLine();
                System.out.print("Email: ");
                String email = scanner.nextLine();
                System.out.print("Mosha: ");
                int age = scanner.nextInt();
                scanner.nextLine();
                registerPatient(name, phone, email, age);
                break;
            case 2:
                System.out.print("ID e pacientit: ");
                int id = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Emri i ri: ");
                name = scanner.nextLine();
                System.out.print("Telefoni i ri: ");
                phone = scanner.nextLine();
                System.out.print("Email i ri: ");
                email = scanner.nextLine();
                System.out.print("Mosha e re: ");
                age = scanner.nextInt();
                scanner.nextLine();
                updatePatient(id, name, phone, email, age);
                break;
            case 3:
                System.out.print("ID e pacientit për fshirje: ");
                id = scanner.nextInt();
                scanner.nextLine();
                deletePatient(id);
                break;
            case 4:
                listPatients();
                break;
            case 5:
                System.out.print("ID e pacientit: ");
                id = scanner.nextInt();
                scanner.nextLine();
                listAppointmentsByPatient(id);
                break;
        }
    }

    private void handleDoctorMenu() {
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║     MENAXHIMI I DOKTORËVE             ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println("1 - Regjistro Doktor");
        System.out.println("2 - Modifiko Doktor");
        System.out.println("3 - Fshi Doktor");
        System.out.println("4 - Shfaq të Gjithë Doktorët");
        System.out.println("0 - Kthehu");
        System.out.print("\nShtyp opsionin: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Emri: ");
                String name = scanner.nextLine();
                System.out.print("Telefoni: ");
                String phone = scanner.nextLine();
                System.out.print("Email: ");
                String email = scanner.nextLine();
                System.out.print("Specialiteti: ");
                String specialty = scanner.nextLine();
                registerDoctor(name, phone, email, specialty);
                break;
            case 2:
                System.out.print("ID e doktorit: ");
                int id = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Emri i ri: ");
                name = scanner.nextLine();
                System.out.print("Telefoni i ri: ");
                phone = scanner.nextLine();
                System.out.print("Email i ri: ");
                email = scanner.nextLine();
                System.out.print("Specialiteti i ri: ");
                specialty = scanner.nextLine();
                updateDoctor(id, name, phone, email, specialty);
                break;
            case 3:
                System.out.print("ID e doktorit për fshirje: ");
                id = scanner.nextInt();
                scanner.nextLine();
                deleteDoctor(id);
                break;
            case 4:
                listDoctors();
                break;
        }
    }

    private void handleAppointmentMenu() {
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║     MENAXHIMI I TERMINEVE             ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println("1 - Planifiko Termin");
        System.out.println("2 - Përfundo Termin");
        System.out.println("3 - Anulo Termin");
        System.out.println("4 - Shfaq të Gjithë Terminet");
        System.out.println("5 - Shfaq Termine sipas Statusit");
        System.out.println("0 - Kthehu");
        System.out.print("\nShtyp opsionin: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("ID e pacientit: ");
                int patientId = scanner.nextInt();
                System.out.print("ID e doktorit: ");
                int doctorId = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Data (dd/MM/yyyy): ");
                String dateStr = scanner.nextLine();
                System.out.print("Ora (HH:mm): ");
                String timeStr = scanner.nextLine();
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Date date = sdf.parse(dateStr + " " + timeStr);
                    scheduleAppointment(patientId, doctorId, date);
                } catch (ParseException e) {
                    System.out.println("\n✗ Format i gabuar i datës!");
                }
                break;
            case 2:
                System.out.print("ID e terminit: ");
                int appointmentId = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Raporti mjekësor: ");
                String report = scanner.nextLine();
                completeAppointment(appointmentId, report);
                break;
            case 3:
                System.out.print("ID e terminit për anulim: ");
                appointmentId = scanner.nextInt();
                scanner.nextLine();
                cancelAppointment(appointmentId);
                break;
            case 4:
                listAllAppointments();
                break;
            case 5:
                System.out.println("Zgjedh statusin:");
                System.out.println("1 - I Planifikuar");
                System.out.println("2 - I Përfunduar");
                System.out.println("3 - I Anuluar");
                System.out.print("Zgjedhja: ");
                int statusChoice = scanner.nextInt();
                scanner.nextLine();
                AppointmentStatus status;
                switch (statusChoice) {
                    case 1: status = AppointmentStatus.SCHEDULED; break;
                    case 2: status = AppointmentStatus.COMPLETED; break;
                    case 3: status = AppointmentStatus.CANCELLED; break;
                    default:
                        System.out.println("\n✗ Zgjedhje e pavlefshme!");
                        return;
                }
                listAppointmentsByStatus(status);
                break;
        }
    }

    public static void main(String[] args) {
        HealthApp app = new HealthApp();
        app.start();
    }
}