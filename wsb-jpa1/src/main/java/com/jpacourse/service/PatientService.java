package com.jpacourse.service;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.dto.VisitTO;

import java.util.List;

public interface PatientService {
    PatientTO findByID(final Long id);
    void deletePatient(final Long id);
    List<PatientTO> findByLastName(String lastName);
    List<VisitTO> findVisitsByPatientId(Long patientId);
    List<PatientTO> findPatientsWithMoreVisitsThan(int numberOfVisits);
    List<PatientTO> findPatientsWithWeightGreaterThan(Double weight);
}
