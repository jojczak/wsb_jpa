package com.jpacourse.dto;

import java.time.LocalDateTime;

public class VisitTO {

    private Long id;

    private String description;

    private LocalDateTime time;

    private MedicalTreatmentTO medicalTreatment;

    private DoctorTO doctor;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getTime() { return time; }

    public void setTime(LocalDateTime time) { this.time = time; }

    public MedicalTreatmentTO getMedicalTreatment() { return medicalTreatment; }

    public void setMedicalTreatment(MedicalTreatmentTO medicalTreatment) { this.medicalTreatment = medicalTreatment; }

    public DoctorTO getDoctor() { return doctor; }

    public void setDoctor(DoctorTO doctor) { this.doctor = doctor; }
}
