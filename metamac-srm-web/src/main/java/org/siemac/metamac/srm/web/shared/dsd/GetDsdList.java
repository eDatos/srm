package org.siemac.metamac.srm.web.shared.dsd;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetDsdList {

    @In(1)
    int                              firstResult;

    @In(2)
    int                              maxResults;

    @In(3)
    String                           dsd;

    @Out(1)
    List<DataStructureDefinitionDto> dsdDtos;

    @Out(2)
    int                              firstResultOut;

    @Out(3)
    int                              totalResults;

}
