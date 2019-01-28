package org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptTypes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ItemResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.RoleConcepts;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;

import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;

public interface ConceptsDo2RestMapperV10 {

    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptSchemes toConceptSchemes(PagedResult<ConceptSchemeVersionMetamac> sources, String agencyID, String resourceID,
            String query, String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptScheme toConceptScheme(ConceptSchemeVersionMetamac source);
    public ResourceInternal toResource(ConceptSchemeVersion source);

    public Concepts toConcepts(PagedResult<ConceptMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit, Set<String> fields);
    public Concepts toConcepts(List<ItemResult> sources, ConceptSchemeVersionMetamac conceptSchemeVersion, Set<String> fields);
    public Concept toConcept(ConceptMetamac source);
    public ItemResourceInternal toResource(com.arte.statistic.sdmx.srm.core.concept.domain.Concept source);
    @SuppressWarnings("rawtypes")
    public RoleConcepts toRoleConcepts(Collection sources);

    public ConceptTypes toConceptTypes(List<ConceptType> sources);

    public ResourceLink toConceptSchemeSelfLink(String agencyID, String resourceID, String version);
    public String toConceptSchemeManagementApplicationLink(String conceptSchemeUrn);
}
