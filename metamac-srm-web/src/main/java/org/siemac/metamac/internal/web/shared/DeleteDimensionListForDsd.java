package org.siemac.metamac.internal.web.shared;

import java.util.List;

import org.siemac.metamac.domain_dto.DimensionComponentDto;
import org.siemac.metamac.domain_enum.domain.TypeComponentList;

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
