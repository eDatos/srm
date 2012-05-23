package org.siemac.metamac.internal.web.shared;

import java.util.List;

import org.siemac.metamac.domain.srm.dto.DimensionComponentDto;
import org.siemac.metamac.domain.srm.enume.domain.TypeComponentList;

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
