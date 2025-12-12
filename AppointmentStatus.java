package ProjektiOOP;

enum AppointmentStatus {
    SCHEDULED("I Planifikuar"),
    COMPLETED("I Përfunduar"),
    CANCELLED("I Anuluar");

    private final String description;

    AppointmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}