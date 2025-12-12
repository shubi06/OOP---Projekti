package repository;

import model.entities.Patient;
import utils.FileUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientRepository {

    private final String filePath;

    public PatientRepository(String filePath) {
        this.filePath = filePath;
        // ensure file exists
        FileUtil.ensureFileExists(filePath);
    }

    public void addPatient(Patient patient) {
        FileUtil.writeLine(filePath, patient.toFileString());
    }

    public List<Patient> getAllPatients() {
        List<String> lines = FileUtil.readAll(filePath);
        List<Patient> patients = new ArrayList<>();
        for (String line : lines) {
            try {
                patients.add(Patient.fromFileString(line));
            } catch (Exception e) {
                System.out.println("Warning: nuk u mundua parse rreshti i pacientit: " + line);
            }
        }
        return patients;
    }

    public Optional<Patient> getPatientById(int id) {
        return getAllPatients().stream().filter(p -> p.getId() == id).findFirst();
    }

    public void updatePatient(Patient updatedPatient) {
        List<Patient> patients = getAllPatients();
        boolean found = false;

        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getId() == updatedPatient.getId()) {
                patients.set(i, updatedPatient);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Pacienti me ID " + updatedPatient.getId() + " nuk u gjet për përditësim.");
            return;
        }

        List<String> lines = new ArrayList<>();
        for (Patient p : patients) lines.add(p.toFileString());
        FileUtil.overwriteFile(filePath, lines);
    }
}
