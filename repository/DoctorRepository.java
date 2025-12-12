package repository;

import model.entities.Doctor;
import utils.FileUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoctorRepository {

    private final String filePath;

    public DoctorRepository(String filePath) {
        this.filePath = filePath;
        FileUtil.ensureFileExists(filePath);
    }

    public void addDoctor(Doctor doctor) {
        FileUtil.writeLine(filePath, doctor.toFileString());
    }

    public List<Doctor> getAllDoctors() {
        List<String> lines = FileUtil.readAll(filePath);
        List<Doctor> doctors = new ArrayList<>();
        for (String line : lines) {
            try {
                doctors.add(Doctor.fromFileString(line));
            } catch (Exception e) {
                System.out.println("Warning: nuk u mundua parse rreshti i doktorit: " + line);
            }
        }
        return doctors;
    }

    public Optional<Doctor> getDoctorById(int id) {
        return getAllDoctors().stream()
                .filter(d -> d.getId() == id)
                .findFirst();
    }

    public void updateDoctor(Doctor updatedDoctor) {
        List<Doctor> doctors = getAllDoctors();
        boolean found = false;
        for (int i = 0; i < doctors.size(); i++) {
            if (doctors.get(i).getId() == updatedDoctor.getId()) {
                doctors.set(i, updatedDoctor);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Doktori me ID " + updatedDoctor.getId() + " nuk u gjet për përditësim.");
            return;
        }
        List<String> lines = new ArrayList<>();
        for (Doctor d : doctors) lines.add(d.toFileString());
        FileUtil.overwriteFile(filePath, lines);
    }
}
