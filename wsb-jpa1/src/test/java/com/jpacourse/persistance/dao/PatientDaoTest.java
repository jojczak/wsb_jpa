package com.jpacourse.persistance.dao;

import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.MedicalTreatmentEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import com.jpacourse.persistance.enums.Specialization;
import com.jpacourse.persistance.enums.TreatmentType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class PatientDaoTest {

    @Autowired
    private PatientDao patientDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void shouldAddVisitToPatient() {
        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Anna");
        patient.setLastName("Kowalska");
        patient.setDateOfBirth(LocalDate.now());
        patient.setPatientNumber("123456789");
        patient.setTelephoneNumber("123456789");
        entityManager.persist(patient);

        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Jan");
        doctor.setLastName("Nowak");
        doctor.setTelephoneNumber("123456789");
        doctor.setDoctorNumber("12");
        doctor.setSpecialization(Specialization.SURGEON);
        entityManager.persist(doctor);

        MedicalTreatmentEntity medicalTreatment = new MedicalTreatmentEntity();
        medicalTreatment.setDescription("Medical treatment");
        medicalTreatment.setDescription("Opis treatment");
        medicalTreatment.setType(TreatmentType.EKG);
        entityManager.persist(medicalTreatment);
        entityManager.flush();

        String description = "Kontrola ciśnienia";
        LocalDateTime date = LocalDateTime.of(2025, 4, 14, 10, 30);

        patientDao.addVisitToPatient(patient.getId(), doctor.getId(), medicalTreatment.getId(), date, description);

        PatientEntity updated = entityManager.find(PatientEntity.class, patient.getId());
        List<VisitEntity> visits = updated.getVisits();

        assertEquals(1, visits.size());
        VisitEntity visit = visits.get(0);
        assertEquals(description, visit.getDescription());
        assertEquals(date, visit.getTime());
        assertEquals(doctor.getId(), visit.getDoctor().getId());
    }
}