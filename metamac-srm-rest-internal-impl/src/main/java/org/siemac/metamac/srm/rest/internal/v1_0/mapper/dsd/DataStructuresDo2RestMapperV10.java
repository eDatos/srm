package org.siemac.metamac.srm.rest.internal.v1_0.mapper.dsd;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;

public interface DataStructuresDo2RestMapperV10 {

    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructures toDataStructures(PagedResult<DataStructureDefinitionVersionMetamac> sources, String agencyID,
            String resourceID, String query, String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure toDataStructure(DataStructureDefinitionVersionMetamac source);

    public ResourceLink toDataStructureSelfLink(String agencyID, String resourceID, String version);
    public String toDataStructureManagementApplicationLink(String dsdUrn);
}
