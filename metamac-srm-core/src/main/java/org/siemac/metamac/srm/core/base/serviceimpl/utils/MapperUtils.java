package org.siemac.metamac.srm.core.base.serviceimpl.utils;

import org.siemac.metamac.core.common.exception.MetamacException;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.RepresentationDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;

public class MapperUtils extends com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.MapperUtils {

    public static boolean isCoreRepresentationExtends(RepresentationDto representationDto) throws MetamacException {
        if (representationDto == null) {
            return false;
        }

        if (RepresentationTypeEnum.ENUMERATION.equals(representationDto.getRepresentationType())) {
            if (representationDto.getEnumeration() != null && RelatedResourceTypeEnum.CONCEPT_SCHEME.equals(representationDto.getEnumeration().getType())) {
                return true;
            }
        }
        return false;
    }
}
