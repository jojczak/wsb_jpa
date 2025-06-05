package com.jpacourse.service.impl;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.dto.VisitTO;
import com.jpacourse.mapper.PatientMapper;
import com.jpacourse.mapper.VisitMapper;
import com.jpacourse.persistance.dao.PatientDao;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import com.jpacourse.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientDao patientDao;

    @Autowired
    public PatientServiceImpl(PatientDao patientDao) {
        this.patientDao = patientDao;
    }

    @Override
    public PatientTO findByID(Long id) {
        final PatientEntity entity = patientDao.findOne(id);
        return PatientMapper.mapToTO(entity);
    }

    @Override
    public void deletePatient(Long id) {
        patientDao.delete(id);
    }

    @Override
    public List<PatientTO> findByLastName(String lastName) {
        List<PatientEntity> entities = patientDao.findByLastName(lastName);
        return entities.stream()
                .map(PatientMapper::mapToTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VisitTO> findVisitsByPatientId(Long patientId) {
        List<VisitEntity> visits = patientDao.findVisitsByPatientId(patientId);
        return visits.stream()
                .map(VisitMapper::mapToTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientTO> findPatientsWithMoreVisitsThan(int numberOfVisits) {
        if (numberOfVisits < 0) {
            throw new IllegalArgumentException("Liczba wizyt nie może być ujemna");
        }
        List<PatientEntity> patients = patientDao.findPatientsWithMoreVisitsThan(numberOfVisits);
        return patients.stream()
                .map(PatientMapper::mapToTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientTO> findPatientsWithWeightGreaterThan(Double weight) {
        if (weight == null) {
            throw new IllegalArgumentException("Waga nie może być null");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("Waga nie może być ujemna");
        }
        List<PatientEntity> patients = patientDao.findPatientsWithWeightGreaterThan(weight);
        return patients.stream()
                .map(PatientMapper::mapToTO)
                .collect(Collectors.toList());
    }
}
