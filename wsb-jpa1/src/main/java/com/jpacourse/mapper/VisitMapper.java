package com.jpacourse.mapper;

import com.jpacourse.dto.VisitTO;
import com.jpacourse.persistance.entity.VisitEntity;

public final class VisitMapper {

    public static VisitTO mapToTO(VisitEntity entity) {
        VisitTO to = new VisitTO();
        to.setId(entity.getId());
        to.setDescription(entity.getDescription());
        to.setTime(entity.getTime());
        to.setMedicalTreatment(MedicalTreatmentMapper.mapToTO(entity.getMedicalTreatment()));
        to.setDoctor(DoctorMapper.mapToTO(entity.getDoctor()));

        return to;
    }
}
