package org.siemac.metamac.srm.rest.external.v1_0.dsd.utils;

import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.external.v1_0.utils.Asserts;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;

public class DataStructuresAsserts extends Asserts {

    public static void assertEqualsResource(DataStructureDefinitionVersionMetamac expected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getMaintainableArtefact();
        String agency = maintainableArtefact.getMaintainer().getIdAsMaintainer();
        String code = maintainableArtefact.getCode();
        String version = maintainableArtefact.getVersionLogic();
        String expectedSelfLink = "http://data.istac.es/apis/structural-resources/v1.0/datastructures/" + agency + "/" + code + "/" + version;
        String expectedManagementLink = "http://localhost:8080/metamac-srm-web/#structuralResources/dsds/dsd;id=" + agency + ":" + code + "(" + version + ")";
        assertEqualsResource(expected.getMaintainableArtefact(), SrmRestConstants.KIND_DATA_STRUCTURE, expectedSelfLink, expectedManagementLink, actual, expected.getMaintainableArtefact()
                .getIsImported());
    }
}