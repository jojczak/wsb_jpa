package com.jpacourse.mapper;

import com.jpacourse.dto.AddressTO;
import com.jpacourse.dto.DoctorTO;
import com.jpacourse.persistance.entity.DoctorEntity;

import java.util.List;

public final class DoctorMapper {

    public static DoctorTO mapToTO(DoctorEntity entity) {
        if (entity == null) {
            return null;
        }
        DoctorTO to = new DoctorTO();
        to.setId(entity.getId());
        to.setFirstName(entity.getFirstName());
        to.setLastName(entity.getLastName());
        to.setEmail(entity.getEmail());
        to.setDoctorNumber(entity.getDoctorNumber());
        to.setSpecialization(entity.getSpecialization());

        List<AddressTO> addressTOs = entity.getAddresses()
                .stream()
                .map(AddressMapper::mapToTO)
                .toList();

        to.setAddresses(addressTOs);

        return to;
    }
}
