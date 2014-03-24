package org.siemac.metamac.srm.rest.external.v1_0.mapper.dsd;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;

public interface DataStructuresDo2RestMapperV10 {

    public org.siemac.metamac.rest.structural_resources.v1_0.domain.DataStructures toDataStructures(PagedResult<DataStructureDefinitionVersionMetamac> sources, String agencyID, String resourceID,
            String query, String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources.v1_0.domain.DataStructure toDataStructure(DataStructureDefinitionVersionMetamac source);
}
