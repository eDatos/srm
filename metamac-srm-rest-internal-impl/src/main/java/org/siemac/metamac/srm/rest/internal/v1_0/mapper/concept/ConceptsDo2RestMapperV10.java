package org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;

public interface ConceptsDo2RestMapperV10 {

    public org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemes toConceptSchemes(PagedResult<ConceptSchemeVersionMetamac> sources, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit);
    public org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme toConceptScheme(ConceptSchemeVersionMetamac source);
    public void toConceptScheme(ConceptSchemeVersionMetamac source, org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme target);
    public void toConcept(ConceptMetamac source, org.siemac.metamac.rest.srm_internal.v1_0.domain.Concept target);
}
