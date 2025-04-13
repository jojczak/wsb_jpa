package com.jpacourse.mapper;

import com.jpacourse.dto.AddressTO;
import com.jpacourse.dto.PatientTO;
import com.jpacourse.persistance.entity.PatientEntity;

import java.util.List;

public class PatientMapper {
    public static PatientTO mapToTO(PatientEntity entity) {
        if (entity == null) {
            return null;
        }
        final PatientTO patientTO = new PatientTO();
        patientTO.setId(entity.getId());
        patientTO.setFirstName(entity.getFirstName());
        patientTO.setLastName(entity.getLastName());
        patientTO.setTelephoneNumber(entity.getTelephoneNumber());
        patientTO.setEmail(entity.getEmail());
        patientTO.setPatientNumber(entity.getPatientNumber());
        patientTO.setDateOfBirth(entity.getDateOfBirth());

        List<AddressTO> addressTOs = entity.getAddresses()
                .stream()
                .map(AddressMapper::mapToTO)
                .toList();

        patientTO.setAddresses(addressTOs);

        return patientTO;
    }
}
