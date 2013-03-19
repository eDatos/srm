package org.siemac.metamac.srm.rest.internal.v1_0.dsd.utils;

import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.Asserts;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;

public class DataStructuresAsserts extends Asserts {

    public static void assertEqualsResource(DataStructureDefinitionVersionMetamac expected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/datastructures/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/"
                + maintainableArtefact.getCode() + "/" + maintainableArtefact.getVersionLogic();

        assertEqualsResource(expected.getMaintainableArtefact(), RestInternalConstants.KIND_DATA_STRUCTURE, expectedSelfLink, actual);
    }
}