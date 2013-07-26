package org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.rest.internal.v1_0.service.utils.SrmRestInternalUtils;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.Asserts;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationResultExtensionPoint;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

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

    public static void assertEqualsResource(ItemSchemeVersion itemSchemeVersion, OrganisationMetamac expected, ItemResult expectedItemResult, String kindExpected, String itemSchemeSubpathExpected,
            String itemsSubpathExpected, ResourceInternal actual) {
        MaintainableArtefact maintainableArtefact = itemSchemeVersion.getMaintainableArtefact();
        String agency = maintainableArtefact.getMaintainer().getIdAsMaintainer();
        String codeItemScheme = maintainableArtefact.getCode();
        String version = maintainableArtefact.getVersionLogic();
        String code = null;
        OrganisationTypeEnum organisationTypeExpected = null;
        if (expected != null) {
            code = expected.getNameableArtefact().getCode();
            organisationTypeExpected = expected.getOrganisationType();
        } else if (expectedItemResult != null) {
            code = expectedItemResult.getCode();
            organisationTypeExpected = ((OrganisationResultExtensionPoint) expectedItemResult.getExtensionPoint()).getOrganisationType();
        }
        String expectedSelfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/" + itemSchemeSubpathExpected + "/" + agency + "/" + codeItemScheme + "/" + version + "/"
                + itemsSubpathExpected + "/" + code;
        String expectedManagementLink = "http://localhost:8080/metamac-srm-web/#structuralResources/organisationSchemes/organisationScheme;type="
                + SrmRestInternalUtils.toOrganisationSchemeType(organisationTypeExpected).getName() + ";id=" + agency + ":" + codeItemScheme + "(" + version + ")/organisation;id=" + code;

        if (expected != null) {
            Asserts.assertEqualsResource(expected, kindExpected, expectedSelfLink, expectedManagementLink, actual);
        } else if (expectedItemResult != null) {
            Asserts.assertEqualsResource(expectedItemResult, kindExpected, expectedSelfLink, expectedManagementLink, actual);
        }
        if (OrganisationTypeEnum.AGENCY.equals(organisationTypeExpected)) {
            assertNotNull(actual.getNestedId());
            if (expected != null) {
                assertEquals(expected.getIdAsMaintainer(), actual.getNestedId());
            } else if (expectedItemResult != null) {
                assertEquals(((OrganisationResultExtensionPoint) expectedItemResult.getExtensionPoint()).getIdAsMaintainer(), actual.getNestedId());
            }
        }
    }
}