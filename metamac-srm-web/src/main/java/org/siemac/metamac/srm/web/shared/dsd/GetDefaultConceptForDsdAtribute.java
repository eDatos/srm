package org.siemac.metamac.srm.web.shared.dsd;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.SpecialAttributeTypeEnum;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetDefaultConceptForDsdAtribute {

    @In(1)
    String                   dsdUrn;

    @In(2)
    SpecialAttributeTypeEnum specialAttributeTypeEnum;

    @Out(1)
    RelatedResourceDto       concept;
}
