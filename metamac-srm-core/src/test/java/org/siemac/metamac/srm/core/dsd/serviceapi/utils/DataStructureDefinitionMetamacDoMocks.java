package org.siemac.metamac.srm.core.dsd.serviceapi.utils;

import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;

import com.arte.statistic.sdmx.srm.core.structure.serviceapi.utils.DataStructureDefinitionDoMocks;

public class DataStructureDefinitionMetamacDoMocks extends DataStructureDefinitionDoMocks {

    public static DataStructureDefinitionVersionMetamac mockDataStructureDefinitionVersionMetamac() {

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = new DataStructureDefinitionVersionMetamac();

        mockDataStructureDefinition(dataStructureDefinitionVersionMetamac);

        return dataStructureDefinitionVersionMetamac;
    }

}
