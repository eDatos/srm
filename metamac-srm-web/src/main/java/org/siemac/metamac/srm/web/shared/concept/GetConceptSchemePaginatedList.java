package org.siemac.metamac.srm.web.shared.concept;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetConceptSchemePaginatedList {

    @In(1)
    int                           firstResult;

    @In(2)
    int                           maxResults;

    @In(3)
    String                        criteria;

    @Out(1)
    List<ConceptSchemeMetamacDto> conceptSchemeList;

    @Out(2)
    Integer                       pageNumber;

    @Out(3)
    Integer                       totalResults;
}
