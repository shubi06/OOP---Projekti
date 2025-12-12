package model.enums;

public enum AppointmentStatus {
	SCHEDULED {
        @Override
        public String toString() { return "I planifikuar"; }
    },
    COMPLETED {
        @Override
        public String toString() { return "Përfunduar"; }
    },
    CANCELLED {
        @Override
        public String toString() { return "Anuluar"; }
    }
    

}
