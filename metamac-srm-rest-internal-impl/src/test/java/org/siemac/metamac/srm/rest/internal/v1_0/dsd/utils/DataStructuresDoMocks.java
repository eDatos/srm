package org.siemac.metamac.srm.rest.internal.v1_0.dsd.utils;

import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.serviceapi.utils.DataStructureDefinitionMetamacDoMocks;

import com.arte.statistic.sdmx.srm.core.base.domain.Structure;

public class DataStructuresDoMocks {

    public static DataStructureDefinitionVersionMetamac mockDataStructure(String agencyID, String resourceID, String version) {
        return DataStructureDefinitionMetamacDoMocks.mockDataStructureDefinitionVersionMetamacFixedValues(agencyID, resourceID, version);
    }

    public static DataStructureDefinitionVersionMetamac mockDataStructureWithComponents(String agencyID, String resourceID, String version) {
        DataStructureDefinitionVersionMetamac target = DataStructureDefinitionMetamacDoMocks.mockDataStructureDefinitionVersionMetamacFixedValuesWithComponents(agencyID, resourceID, version);
        target.setStructure(new Structure());
        return target;
    }
}