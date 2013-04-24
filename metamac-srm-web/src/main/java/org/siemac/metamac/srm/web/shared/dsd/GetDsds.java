package org.siemac.metamac.srm.web.shared.dsd;

import java.util.List;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.web.shared.criteria.DataStructureDefinitionWebCriteria;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetDsds {

    @In(1)
    int                                          firstResult;

    @In(2)
    int                                          maxResults;

    @In(3)
    DataStructureDefinitionWebCriteria           criteria;

    @Out(1)
    List<DataStructureDefinitionMetamacBasicDto> dsdDtos;

    @Out(2)
    int                                          firstResultOut;

    @Out(3)
    int                                          totalResults;
}
