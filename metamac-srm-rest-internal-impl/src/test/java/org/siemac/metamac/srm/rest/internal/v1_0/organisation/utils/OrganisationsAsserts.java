package org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Agency;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencyScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnit;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnitScheme;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.Asserts;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.AgencyType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.OrganisationUnitType;

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

    public static void assertEqualsAgencyScheme(OrganisationSchemeVersionMetamac source, AgencyScheme target) {
        assertEquals(RestInternalConstants.KIND_AGENCY_SCHEME, target.getKind());
        String parentLink = "http://data.istac.es/apis/srm/v1.0/agencyschemes";
        String selfLink = parentLink + "/" + source.getMaintainableArtefact().getMaintainer().getIdAsMaintainer() + "/" + source.getMaintainableArtefact().getCode() + "/"
                + source.getMaintainableArtefact().getVersionLogic();
        assertEquals(RestInternalConstants.KIND_AGENCY_SCHEME, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(RestInternalConstants.KIND_AGENCY_SCHEMES, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertEquals(source.getMaintainableArtefact().getReplaceToVersion(), target.getReplaceToVersion());
        assertEquals(BigInteger.ONE, target.getChildLinks().getTotal());
        assertEquals(RestInternalConstants.KIND_AGENCIES, target.getChildLinks().getChildLinks().get(0).getKind());
        assertEquals(selfLink + "/agencies", target.getChildLinks().getChildLinks().get(0).getHref());

        // Items (SDMX type)
        assertEquals(source.getItems().size(), target.getAgencies().size());
        for (int i = 0; i < source.getItems().size(); i++) {
            assertTrue(target.getAgencies().get(i) instanceof AgencyType);
            assertFalse(target.getAgencies().get(i) instanceof Agency);

            assertEqualsAgencySdmx((OrganisationMetamac) source.getItems().get(i), target.getAgencies().get(i));
        }
    }

    public static void assertEqualsOrganisationUnitScheme(OrganisationSchemeVersionMetamac source, OrganisationUnitScheme target) {
        assertEquals(RestInternalConstants.KIND_ORGANISATION_UNIT_SCHEME, target.getKind());
        String parentLink = "http://data.istac.es/apis/srm/v1.0/organisationunitschemes";
        String selfLink = parentLink + "/" + source.getMaintainableArtefact().getMaintainer().getIdAsMaintainer() + "/" + source.getMaintainableArtefact().getCode() + "/"
                + source.getMaintainableArtefact().getVersionLogic();
        assertEquals(RestInternalConstants.KIND_ORGANISATION_UNIT_SCHEME, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(RestInternalConstants.KIND_ORGANISATION_UNIT_SCHEMES, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertEquals(source.getMaintainableArtefact().getReplaceToVersion(), target.getReplaceToVersion());
        assertEquals(BigInteger.ONE, target.getChildLinks().getTotal());
        assertEquals(RestInternalConstants.KIND_ORGANISATION_UNITS, target.getChildLinks().getChildLinks().get(0).getKind());
        assertEquals(selfLink + "/organisationunits", target.getChildLinks().getChildLinks().get(0).getHref());

        // Items (SDMX type)
        assertEquals(source.getItems().size(), target.getOrganisationUnits().size());
        for (int i = 0; i < source.getItems().size(); i++) {
            assertTrue(target.getOrganisationUnits().get(i) instanceof OrganisationUnitType);
            assertFalse(target.getOrganisationUnits().get(i) instanceof OrganisationUnit);

            assertEqualsOrganisationUnitSdmx((OrganisationMetamac) source.getItems().get(i), target.getOrganisationUnits().get(i));
        }
    }

    public static void assertEqualsAgency(OrganisationMetamac source, Agency target) {
        assertEquals(RestInternalConstants.KIND_AGENCY, target.getKind());
        String parentLink = "http://data.istac.es/apis/srm/v1.0/agencyschemes" + "/" + source.getItemSchemeVersion().getMaintainableArtefact().getMaintainer().getIdAsMaintainer() + "/"
                + source.getItemSchemeVersion().getMaintainableArtefact().getCode() + "/" + source.getItemSchemeVersion().getMaintainableArtefact().getVersionLogic() + "/agencies";
        String selfLink = parentLink + "/" + source.getNameableArtefact().getCode();
        assertEquals(RestInternalConstants.KIND_AGENCY, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(RestInternalConstants.KIND_AGENCIES, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertNull(target.getChildLinks());

        // Sdmx
        assertEqualsAgencySdmx(source, target);
    }

    public static void assertEqualsOrganisationUnit(OrganisationMetamac source, OrganisationUnit target) {
        assertEquals(RestInternalConstants.KIND_ORGANISATION_UNIT, target.getKind());
        String parentLink = "http://data.istac.es/apis/srm/v1.0/organisationunitschemes" + "/" + source.getItemSchemeVersion().getMaintainableArtefact().getMaintainer().getIdAsMaintainer() + "/"
                + source.getItemSchemeVersion().getMaintainableArtefact().getCode() + "/" + source.getItemSchemeVersion().getMaintainableArtefact().getVersionLogic() + "/organisationunits";
        String selfLink = parentLink + "/" + source.getNameableArtefact().getCode();
        assertEquals(RestInternalConstants.KIND_ORGANISATION_UNIT, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(RestInternalConstants.KIND_ORGANISATION_UNITS, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertNull(target.getChildLinks());

        // Sdmx
        assertEqualsOrganisationUnitSdmx(source, target);
    }

    public static void assertEqualsAgencySdmx(OrganisationMetamac source, AgencyType target) {
        // Only test some metadata because SDMX metadata is tested in SDMX project
        // Test something...
        assertEquals(source.getNameableArtefact().getCode(), target.getId());
        assertEquals(source.getNameableArtefact().getUrn(), target.getUrn());
    }

    public static void assertEqualsOrganisationUnitSdmx(OrganisationMetamac source, OrganisationUnitType target) {
        // Only test some metadata because SDMX metadata is tested in SDMX project
        // Test something...
        assertEquals(source.getNameableArtefact().getCode(), target.getId());
        assertEquals(source.getNameableArtefact().getUrn(), target.getUrn());
    }
}