package org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils;

import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.Asserts;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;

public class OrganisationsAsserts extends Asserts {

    public static void assertEqualsResource(OrganisationSchemeVersionMetamac expected, String kindExpected, String itemSchemeSubpathExpected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/srm/v1.0/" + itemSchemeSubpathExpected + "/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/"
                + maintainableArtefact.getCode() + "/" + maintainableArtefact.getVersionLogic();

        Asserts.assertEqualsResource(expected, kindExpected, expectedSelfLink, actual);
    }

    public static void assertEqualsResource(OrganisationMetamac expected, String kindExpected, String itemSchemeSubpathExpected, String itemsSubpathExpected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getItemSchemeVersion().getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/srm/v1.0/" + itemSchemeSubpathExpected + "/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/"
                + maintainableArtefact.getCode() + "/" + maintainableArtefact.getVersionLogic() + "/" + itemsSubpathExpected + "/" + expected.getNameableArtefact().getCode();
        assertEqualsResource(expected, kindExpected, expectedSelfLink, actual);
    }
}