package org.siemac.metamac.srm.rest.internal.v1_0.mapper.dsd;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;

import com.arte.statistic.sdmx.srm.core.structure.domain.DataAttribute;

public interface DataStructuresDo2RestMapperV10 {

    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructures toDataStructures(PagedResult<DataStructureDefinitionVersionMetamac> sources, String agencyID,
            String resourceID, String query, String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure toDataStructure(DataStructureDefinitionVersionMetamac source);
    public void toDataStructure(DataStructureDefinitionVersionMetamac source, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure target);
    public void toAttribute(DataAttribute source, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Attribute target);
}
