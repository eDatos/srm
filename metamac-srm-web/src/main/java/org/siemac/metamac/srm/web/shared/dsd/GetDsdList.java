package org.siemac.metamac.srm.web.shared.dsd;

import java.util.List;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetDsdList {

    @In(1)
    int                                     firstResult;

    @In(2)
    int                                     maxResults;

    @In(3)
    String                                  dsd;

    @Out(1)
    List<DataStructureDefinitionMetamacDto> dsdDtos;

    @Out(2)
    int                                     firstResultOut;

    @Out(3)
    int                                     totalResults;

}
