package model.entities;

public class Doctor extends Person {
    private String specialty;
    

    public Doctor(int id, String name, String phone, String email, String specialty) {
        super(id, name, phone, email);
        this.specialty = specialty;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    @Override
    public String getDetails() {
        return String.format("DOKTORI: ID=%d, Emri=%s, Telefoni=%s, Email=%s, Specialiteti=%s",
                id, name, phone, email, specialty);
    }

    public String toFileString() {
        return id + "," + name + "," + phone + "," + email + "," + specialty;
    }

    public static Doctor fromFileString(String line) {
        String[] parts = line.split(",");
        return new Doctor(
                Integer.parseInt(parts[0]),
                parts[1],
                parts[2],
                parts[3],
                parts[4]
        );
    }
}