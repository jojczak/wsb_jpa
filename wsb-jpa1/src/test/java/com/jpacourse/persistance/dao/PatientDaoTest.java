package com.jpacourse.persistance.dao;

import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.MedicalTreatmentEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import com.jpacourse.persistance.enums.Specialization;
import com.jpacourse.persistance.enums.TreatmentType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        PatientEntity patient = createPatient("Anna", "Kowalska", "123456789", 65.0);
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

    @Test
    public void shouldFindPatientsByLastName() {
        PatientEntity patient1 = createPatient("Jan", "Kowalski", "123456789", 75.0);
        PatientEntity patient2 = createPatient("Anna", "Kowalski", "987654321", 62.0);
        PatientEntity patient3 = createPatient("Maria", "Nowak", "456789123", 58.0);

        entityManager.persist(patient1);
        entityManager.persist(patient2);
        entityManager.persist(patient3);
        entityManager.flush();

        List<PatientEntity> foundPatients = patientDao.findByLastName("Kowalski");

        assertThat(foundPatients).isNotNull();
        assertThat(foundPatients).hasSize(2);
        assertThat(foundPatients)
                .extracting(PatientEntity::getLastName)
                .containsOnly("Kowalski");

        List<PatientEntity> foundPatientsLowerCase = patientDao.findByLastName("kowalski");
        assertThat(foundPatientsLowerCase).hasSize(2);
    }

    @Test
    public void shouldFindPatientsWithMoreVisitsThan() {
        PatientEntity patient1 = createPatient("Jan", "Kowalski", "P001", 70.0);
        PatientEntity patient2 = createPatient("Anna", "Nowak", "P002", 65.0);
        PatientEntity patient3 = createPatient("Marek", "Wiśniewski", "P003", 80.0);

        DoctorEntity doctor = createDoctor();
        MedicalTreatmentEntity treatment = createMedicalTreatment();

        entityManager.persist(patient1);
        entityManager.persist(patient2);
        entityManager.persist(patient3);
        entityManager.persist(doctor);
        entityManager.persist(treatment);

        addVisitsForPatient(patient1, doctor, treatment, 3);
        addVisitsForPatient(patient2, doctor, treatment, 1);
        addVisitsForPatient(patient3, doctor, treatment, 4);

        entityManager.flush();
        entityManager.clear();

        List<PatientEntity> patientsWithMoreThan2Visits = patientDao.findPatientsWithMoreVisitsThan(2);

        assertThat(patientsWithMoreThan2Visits).hasSize(2);
        assertThat(patientsWithMoreThan2Visits)
                .extracting(PatientEntity::getPatientNumber)
                .containsExactlyInAnyOrder("P001", "P003");
    }

    @Test
    public void testFetchingStrategies() {
        PatientEntity patient = createPatient("Jan", "Testowy", "TEST001", 70.0);
        DoctorEntity doctor = createDoctor();
        MedicalTreatmentEntity treatment = createMedicalTreatment();

        entityManager.persist(patient);
        entityManager.persist(doctor);
        entityManager.persist(treatment);

        for (int i = 0; i < 3; i++) {
            patientDao.addVisitToPatient(
                    patient.getId(),
                    doctor.getId(),
                    treatment.getId(),
                    LocalDateTime.now().plusDays(i),
                    "Wizyta testowa " + (i + 1)
            );
        }

        entityManager.flush();
        entityManager.clear();

        System.out.println("test strategii pobierania");
        PatientEntity loadedPatient = patientDao.findOne(patient.getId());
        System.out.println("dostęp do wizyt");
        assertThat(loadedPatient.getVisits()).hasSize(3);
    }

    private PatientEntity createPatient(String firstName, String lastName, String patientNumber, Double weight) {
        PatientEntity patient = new PatientEntity();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setPatientNumber(patientNumber);
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setTelephoneNumber("123456789");
        patient.setWeight(weight);
        return patient;
    }

    private DoctorEntity createDoctor() {
        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Adam");
        doctor.setLastName("Lekarski");
        doctor.setTelephoneNumber("987654321");
        doctor.setDoctorNumber("DOC123");
        doctor.setSpecialization(Specialization.SURGEON);
        return doctor;
    }

    private MedicalTreatmentEntity createMedicalTreatment() {
        MedicalTreatmentEntity treatment = new MedicalTreatmentEntity();
        treatment.setDescription("Badanie kontrolne");
        treatment.setType(TreatmentType.EKG);
        return treatment;
    }

    private void addVisitsForPatient(PatientEntity patient, DoctorEntity doctor,
                                     MedicalTreatmentEntity treatment, int numberOfVisits) {
        for (int i = 0; i < numberOfVisits; i++) {
            LocalDateTime visitTime = LocalDateTime.now().plusDays(i);
            patientDao.addVisitToPatient(patient.getId(), doctor.getId(), treatment.getId(),
                    visitTime, "Wizyta " + (i + 1));
        }
    }

}