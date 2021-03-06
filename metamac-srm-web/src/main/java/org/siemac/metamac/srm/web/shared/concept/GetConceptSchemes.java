package org.siemac.metamac.srm.web.shared.concept;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetConceptSchemes {

    @In(1)
    int                                firstResult;

    @In(2)
    int                                maxResults;

    @In(3)
    ConceptSchemeWebCriteria           criteria;

    @Out(1)
    List<ConceptSchemeMetamacBasicDto> conceptSchemeList;

    @Out(2)
    Integer                            firstResultOut;

    @Out(3)
    Integer                            totalResults;
}
