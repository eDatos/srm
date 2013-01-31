package org.siemac.metamac.srm.web.shared.concept;

import java.util.List;

import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetConceptsCanBeExtended {

    @In(1)
    int                      firstResult;

    @In(2)
    int                      maxResults;

    @In(3)
    ConceptWebCriteria       criteria;

    @Out(1)
    List<RelatedResourceDto> conceptList;

    @Out(2)
    Integer                  firstResultOut;

    @Out(3)
    Integer                  totalResults;
}
