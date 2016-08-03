package org.siemac.metamac.srm.rest.external.v1_0.mapper.concept;

import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;

public interface ConceptsRest2DoMapper {

    public RestCriteria2SculptorCriteria<ConceptSchemeVersionMetamac> getConceptSchemeCriteriaMapper();
    public RestCriteria2SculptorCriteria<ConceptMetamac> getConceptCriteriaMapper();
}
