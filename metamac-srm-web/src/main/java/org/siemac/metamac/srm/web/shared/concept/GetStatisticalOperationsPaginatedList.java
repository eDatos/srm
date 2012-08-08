package org.siemac.metamac.srm.web.shared.concept;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetStatisticalOperationsPaginatedList {

    @In(1)
    int                   firstResult;

    @In(2)
    int                   maxResults;

    @In(3)
    String                operation;

    @Out(1)
    List<ExternalItemDto> operations;

    @Out(2)
    int                   firstResultOut;

    @Out(3)
    int                   totalResults;

}
