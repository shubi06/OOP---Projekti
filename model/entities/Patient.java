package model.entities;

public class Patient extends Person {
    private int age;

    public Patient(int id, String name, String phone, String email, int age) {
        super(id, name, phone, email);
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public String getDetails() {
        return String.format("PACIENTI: ID=%d, Emri=%s, Telefoni=%s, Email=%s, Mosha=%d",
            id, name, phone, email, age);
    }

    public String toFileString() {
        return id + "," + name + "," + phone + "," + email + "," + age;
    }


     public static Patient fromFileString(String line) {
        String[] parts = line.split(",");
        return new Patient(
                Integer.parseInt(parts[0]),
                parts[1],
                parts[2],
                parts[3],
                Integer.parseInt(parts[4])
        );
    }
}
