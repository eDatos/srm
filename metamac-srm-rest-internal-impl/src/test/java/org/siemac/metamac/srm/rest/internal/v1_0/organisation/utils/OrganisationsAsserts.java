package org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils;

import org.siemac.metamac.rest.common_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.rest.internal.v1_0.service.utils.SrmRestInternalUtils;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.Asserts;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;

public class OrganisationsAsserts extends Asserts {

    public static void assertEqualsResource(OrganisationSchemeVersionMetamac expected, String kindExpected, String itemSchemeSubpathExpected, ResourceInternal actual) {
        MaintainableArtefact maintainableArtefact = expected.getMaintainableArtefact();
        String agency = maintainableArtefact.getMaintainer().getIdAsMaintainer();
        String code = maintainableArtefact.getCode();
        String version = maintainableArtefact.getVersionLogic();
        String expectedSelfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/" + itemSchemeSubpathExpected + "/" + agency + "/" + code + "/" + version;
        String expectedManagementLink = "http://localhost:8080/metamac-srm-web/#structuralResources/organisationSchemes/organisationScheme;type=" + expected.getOrganisationSchemeType().getName()
                + ";id=" + agency + ":" + code + "(" + version + ")";
        Asserts.assertEqualsResource(expected, kindExpected, expectedSelfLink, expectedManagementLink, actual);
    }

    public static void assertEqualsResource(OrganisationMetamac expected, String kindExpected, String itemSchemeSubpathExpected, String itemsSubpathExpected, ResourceInternal actual) {
        MaintainableArtefact maintainableArtefact = expected.getItemSchemeVersion().getMaintainableArtefact();
        String agency = maintainableArtefact.getMaintainer().getIdAsMaintainer();
        String codeItemScheme = maintainableArtefact.getCode();
        String version = maintainableArtefact.getVersionLogic();
        String code = expected.getNameableArtefact().getCode();
        String expectedSelfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/" + itemSchemeSubpathExpected + "/" + agency + "/" + codeItemScheme + "/" + version + "/"
                + itemsSubpathExpected + "/" + code;
        String expectedManagementLink = "http://localhost:8080/metamac-srm-web/#structuralResources/organisationSchemes/organisationScheme;type="
                + SrmRestInternalUtils.toOrganisationSchemeType(expected.getOrganisationType()).getName() + ";id=" + agency + ":" + codeItemScheme + "(" + version + ")/organisation;id=" + code;
        Asserts.assertEqualsResource(expected, kindExpected, expectedSelfLink, expectedManagementLink, actual);
    }
}