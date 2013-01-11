package org.siemac.metamac.srm.core.dsd.serviceapi.utils;

import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.srm.core.structure.serviceapi.utils.DataStructureDefinitionAsserts;

public class DataStructureDefinitionsMetamacAsserts extends DataStructureDefinitionAsserts {

    public static void assertEqualsDataStructureDefinition(DataStructureDefinitionVersion expected, DataStructureDefinitionVersion actual) {

        // Metamac

        // Sdmx
        DataStructureDefinitionAsserts.assertEqualsDataStructureDefinition(expected, actual);
    }

}
