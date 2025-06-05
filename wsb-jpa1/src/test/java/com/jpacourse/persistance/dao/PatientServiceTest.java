package com.jpacourse.persistance.dao;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.dto.VisitTO;
import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.MedicalTreatmentEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import com.jpacourse.persistance.enums.Specialization;
import com.jpacourse.persistance.enums.TreatmentType;
import com.jpacourse.service.PatientService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class PatientServiceTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private VisitDao visitDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Test
    public void testShouldDeletePatientAndCascadeVisitsButNotDoctors() {
        DoctorEntity doctor = createDoctor();
        entityManager.persist(doctor);

        PatientEntity patient = createPatient();
        entityManager.persist(patient);

        MedicalTreatmentEntity treatment = createMedicalTreatment();
        entityManager.persist(treatment);

        VisitEntity visit = new VisitEntity();
        visit.setTime(LocalDateTime.now());
        visit.setPatient(patient);
        visit.setDoctor(doctor);
        visit.setMedicalTreatment(treatment);

        entityManager.persist(visit);

        List<VisitEntity> visits = new ArrayList<>();
        visits.add(visit);
        patient.setVisits(visits);

        entityManager.flush();

        Long patientId = patient.getId();
        Long visitId = visit.getId();
        Long doctorId = doctor.getId();

        // when
        patientService.deletePatient(patientId);

        // then
        assertThat(patientDao.findOne(patientId)).isNull();
        assertThat(entityManager.find(VisitEntity.class, visitId)).isNull();
        assertThat(doctorDao.findOne(doctorId)).isNotNull();
    }

    @Transactional
    @Test
    public void testShouldReturnPatientByIdWithCorrectTOFields() {
        // given
        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Tomasz");
        patient.setLastName("Zieliński");
        patient.setTelephoneNumber("987654321");
        patient.setEmail("tomasz@example.com");
        patient.setPatientNumber("PN456");
        patient.setDateOfBirth(LocalDate.of(1985, 5, 5));
        patient.setWeight(65.0);
        patient = patientDao.save(patient);

        // when
        PatientTO patientTO = patientService.findByID(patient.getId());

        // then
        assertThat(patientTO).isNotNull();
        assertThat(patientTO.getId()).isEqualTo(patient.getId());
        assertThat(patientTO.getFirstName()).isEqualTo("Tomasz");
        assertThat(patientTO.getLastName()).isEqualTo("Zieliński");
        assertThat(patientTO.getTelephoneNumber()).isEqualTo("987654321");
        assertThat(patientTO.getEmail()).isEqualTo("tomasz@example.com");
        assertThat(patientTO.getPatientNumber()).isEqualTo("PN456");
        assertThat(patientTO.getDateOfBirth()).isEqualTo(LocalDate.of(1985, 5, 5));
    }

    @Test
    public void shouldReturnPatientsVisitsAsTransferObjects() {
        PatientEntity patient = createPatient();
        DoctorEntity doctor = createDoctor();
        MedicalTreatmentEntity treatment = createMedicalTreatment();

        entityManager.persist(patient);
        entityManager.persist(doctor);
        entityManager.persist(treatment);

        LocalDateTime firstVisitTime = LocalDateTime.now().minusDays(1);
        LocalDateTime secondVisitTime = LocalDateTime.now().plusDays(1);

        patientDao.addVisitToPatient(patient.getId(), doctor.getId(), treatment.getId(),
                firstVisitTime, "Wizyta kontrolna");
        patientDao.addVisitToPatient(patient.getId(), doctor.getId(), treatment.getId(),
                secondVisitTime, "Badanie okresowe");

        entityManager.flush();
        entityManager.clear();

        List<VisitTO> visits = patientService.findVisitsByPatientId(patient.getId());

        assertThat(visits).isNotNull();
        assertThat(visits).hasSize(2);

        assertThat(visits)
                .extracting(VisitTO::getDescription)
                .containsExactlyInAnyOrder("Wizyta kontrolna", "Badanie okresowe");

        VisitTO firstVisit = visits.get(0);
        assertThat(firstVisit.getDoctor()).isNotNull();
        assertThat(firstVisit.getDoctor().getSpecialization()).isEqualTo(Specialization.SURGEON);
        assertThat(firstVisit.getMedicalTreatment()).isNotNull();
        assertThat(firstVisit.getMedicalTreatment().getTreatmentType()).isEqualTo(TreatmentType.EKG);
    }

    private PatientEntity createPatient() {
        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Jan");
        patient.setLastName("Testowy");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setPatientNumber("TEST123");
        patient.setTelephoneNumber("123456789");
        patient.setWeight(65.0);
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

}