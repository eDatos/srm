package org.siemac.metamac.srm.web.shared.concept;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.concept.ConceptSchemeDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetConceptSchemePaginatedList {

    @In(1)
    int                    firstResult;

    @In(2)
    int                    maxResults;

    @In(3)
    String                 conceptScheme;

    @Out(1)
    List<ConceptSchemeDto> conceptSchemeList;

    @Out(2)
    Integer                pageNumber;

    @Out(3)
    Integer                totalResults;

}
