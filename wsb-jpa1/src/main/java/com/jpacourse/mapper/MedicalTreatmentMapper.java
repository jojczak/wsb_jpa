package com.jpacourse.mapper;

import com.jpacourse.dto.MedicalTreatmentTO;
import com.jpacourse.persistance.entity.MedicalTreatmentEntity;

public final class MedicalTreatmentMapper {

    public static MedicalTreatmentTO mapToTO(final MedicalTreatmentEntity entity) {
        MedicalTreatmentTO to = new MedicalTreatmentTO();
        to.setId(entity.getId());
        to.setDescription(entity.getDescription());
        to.setTreatmentType(entity.getType());
        return to;
    }
}
