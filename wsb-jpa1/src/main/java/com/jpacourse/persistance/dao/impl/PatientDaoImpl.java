package com.jpacourse.persistance.dao.impl;

import com.jpacourse.persistance.dao.PatientDao;
import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.MedicalTreatmentEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<PatientEntity> findByLastName(String lastName) {
        return findAll().stream()
                .filter(patient -> patient.getLastName().toLowerCase().equals(lastName.toLowerCase()))
                .sorted(Comparator.comparing(PatientEntity::getFirstName))
                .collect(Collectors.toList());
    }

    @Override
    public List<VisitEntity> findVisitsByPatientId(Long patientId) {
        PatientEntity patient = findOne(patientId);
        if (patient == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(patient.getVisits());
    }

    @Override
    public List<PatientEntity> findPatientsWithMoreVisitsThan(int numberOfVisits) {
        return findAll().stream()
                .filter(patient -> patient.getVisits().size() > numberOfVisits)
                .sorted(Comparator.comparing(PatientEntity::getLastName)
                        .thenComparing(PatientEntity::getFirstName))
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientEntity> findPatientsWithWeightGreaterThan(Double weight) {
        return findAll().stream()
                .filter(patient -> patient.getWeight() > weight)
                .sorted(Comparator.comparing(PatientEntity::getLastName)
                        .thenComparing(PatientEntity::getFirstName))
                .collect(Collectors.toList());
    }
}
