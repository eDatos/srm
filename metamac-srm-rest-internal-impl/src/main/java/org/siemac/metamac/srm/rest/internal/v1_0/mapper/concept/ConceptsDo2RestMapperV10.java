package org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptTypes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concepts;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;

public interface ConceptsDo2RestMapperV10 {

    public org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemes toConceptSchemes(PagedResult<ConceptSchemeVersionMetamac> sources, String agencyID, String resourceID, String query,
            String orderBy, Integer limit);
    public org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme toConceptScheme(ConceptSchemeVersionMetamac source);
    public void toConceptScheme(ConceptSchemeVersionMetamac source, org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme target);

    public Concepts toConcepts(PagedResult<ConceptMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit);
    public Concept toConcept(ConceptMetamac source);
    public void toConcept(com.arte.statistic.sdmx.srm.core.concept.domain.Concept source, com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.ConceptType target);

    public ConceptTypes toConceptTypes(List<ConceptType> sources);
}
