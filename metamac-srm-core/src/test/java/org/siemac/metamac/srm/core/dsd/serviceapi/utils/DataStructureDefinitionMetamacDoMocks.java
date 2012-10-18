package org.siemac.metamac.srm.core.dsd.serviceapi.utils;

import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;

import com.arte.statistic.sdmx.srm.core.structure.serviceapi.utils.DataStructureDefinitionDoMocks;

public class DataStructureDefinitionMetamacDoMocks extends DataStructureDefinitionDoMocks {

    public static DataStructureDefinitionVersionMetamac mockDataStructureDefinitionVersionMetamac(OrganisationMetamac maintainer) {

        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = new DataStructureDefinitionVersionMetamac();

        mockDataStructureDefinition(dataStructureDefinitionVersionMetamac, maintainer);

        return dataStructureDefinitionVersionMetamac;
    }

}
