package org.siemac.metamac.srm.web.shared.dsd;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class DeleteDimensionListForDsd {

    @In(1)
    Long                        idDsd;

    @In(2)
    List<DimensionComponentDto> dimensionComponentDtos;

    @In(3)
    TypeComponentList           typeComponentList;

}
