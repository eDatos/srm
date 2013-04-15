package org.siemac.metamac.srm.web.shared.dsd;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.SpecialDimensionTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDimensionComponent;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetDefaultDimensionForDsd {

    @In(1)
    String                   dsdUrn;

    @In(2)
    TypeDimensionComponent   dimensionType;

    @In(3)
    SpecialDimensionTypeEnum specialDimensionType;

    @Out(1)
    DimensionComponentDto    dimensionComponentDto;
}
