package org.siemac.metamac.srm.web.shared.dsd;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataAttributeDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class DeleteAttributesForDsd {

    @In(1)
    String                 dsdUrn;

    @In(2)
    List<DataAttributeDto> dataAttributeDtos;
}
