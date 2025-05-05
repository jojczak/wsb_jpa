package com.jpacourse.persistance.dao.impl;

import com.jpacourse.persistance.dao.PatientDao;
import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.MedicalTreatmentEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class PatientDaoImpl extends AbstractDao<PatientEntity, Long> implements PatientDao {


    @Override
    public void addVisitToPatient(Long patientId, Long doctorId, Long medicalTreatmentId, LocalDateTime visitTime, String description) {
        PatientEntity patient = findOne(patientId);
        DoctorEntity doctor = entityManager.find(DoctorEntity.class, doctorId);
        MedicalTreatmentEntity medicalTreatment = entityManager.find(MedicalTreatmentEntity.class, medicalTreatmentId);

        if (patient == null || doctor == null || medicalTreatment == null) {
            throw new IllegalArgumentException("Patient or Doctor or medical treatment is null");
        }

        VisitEntity visit = new VisitEntity();
        visit.setTime(visitTime);
        visit.setDescription(description);
        visit.setPatient(patient);
        visit.setDoctor(doctor);
        visit.setMedicalTreatment(medicalTreatment);

        patient.getVisits().add(visit);

        update(patient);
    }
}
