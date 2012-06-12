package org.siemac.metamac.srm.web.shared;

import java.util.List;

import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetConceptSchemePaginatedList {

    @In(1)
    int                    maxResults;
    @In(2)
    int                    firstResult;

    @Out(1)
    List<ConceptSchemeDto> conceptSchemeList;

    @Out(2)
    Integer                pageNumber;

    @Out(3)
    Integer                totalResults;

}
