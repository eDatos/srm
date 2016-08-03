package org.siemac.metamac.srm.web.shared.concept;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.srm.web.shared.criteria.StatisticalOperationWebCriteria;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetStatisticalOperations {

    @In(1)
    int                             firstResult;

    @In(2)
    int                             maxResults;

    @In(3)
    StatisticalOperationWebCriteria criteria;

    @Out(1)
    List<ExternalItemDto>           operations;

    @Out(2)
    int                             firstResultOut;

    @Out(3)
    int                             totalResults;
}
