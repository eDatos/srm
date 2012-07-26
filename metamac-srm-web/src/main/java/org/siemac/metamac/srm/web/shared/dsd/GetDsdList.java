package org.siemac.metamac.srm.web.shared.dsd;

import java.util.List;

import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetDsdList {

    @In(1)
    int                              firstResult;

    @In(2)
    int                              maxResults;

    @Out(1)
    List<DataStructureDefinitionDto> dsdDtos;

    @Out(2)
    int                              firstResultOut;

    @Out(3)
    int                              totalResults;

}
