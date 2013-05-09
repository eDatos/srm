package org.siemac.metamac.srm.core.organisation.serviceapi.utils;

import static org.junit.Assert.assertEquals;

import org.siemac.metamac.srm.core.base.utils.BaseAsserts;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.arte.statistic.sdmx.srm.core.organisation.serviceapi.utils.OrganisationsAsserts;

public class OrganisationsMetamacAsserts extends OrganisationsAsserts {

    // ORGANISATION SCHEMES

    public static void assertEqualsOrganisationScheme(OrganisationSchemeVersionMetamac expected, OrganisationSchemeVersionMetamac actual) {
        BaseAsserts.assertEqualsLifeCycle(expected.getLifeCycleMetadata(), actual.getLifeCycleMetadata());
        assertEqualsOrganisationSchemeWithoutLifeCycleMetadata(expected, actual);
    }

    public static void assertEqualsOrganisationSchemeWithoutLifeCycleMetadata(OrganisationSchemeVersionMetamac expected, OrganisationSchemeVersionMetamac actual) {
        OrganisationsAsserts.assertEqualsOrganisationScheme(expected, actual);
    }

    public static void assertEqualsOrganisationSchemeMetamacDto(OrganisationSchemeMetamacDto expected, OrganisationSchemeMetamacDto actual) {
        BaseAsserts.assertEqualsLifeCycleDto(expected.getLifeCycle(), actual.getLifeCycle());
        OrganisationsAsserts.assertEqualsOrganisationSchemeDto(expected, actual);
    }

    public static void assertEqualsOrganisationScheme(OrganisationSchemeVersionMetamac expected, OrganisationSchemeMetamacDto actual) {
        assertEqualsOrganisationScheme(expected, actual, MapperEnum.DO2DTO);
    }

    public static void assertEqualsOrganisationScheme(OrganisationSchemeMetamacDto expected, OrganisationSchemeVersionMetamac actual) {
        assertEqualsOrganisationScheme(actual, expected, MapperEnum.DTO2DO);
    }

    public static void assertEqualsOrganisationScheme(OrganisationSchemeVersionMetamac expected, OrganisationSchemeMetamacBasicDto actual) {
        assertEquals(expected.getOrganisationSchemeType(), actual.getType());
        BaseAsserts.assertEqualsItemSchemeBasicDto(expected, expected.getLifeCycleMetadata(), actual);
    }

    // ORGANISATION

    public static void assertEqualsOrganisation(OrganisationMetamac expected, OrganisationMetamac actual) {
        OrganisationsAsserts.assertEqualsOrganisation(expected, actual);
    }

    public static void assertEqualsOrganisation(OrganisationMetamac expected, OrganisationMetamacDto actual) {
        assertEqualsOrganisation(expected, actual, MapperEnum.DO2DTO);
    }

    public static void assertEqualsOrganisationMetamacDto(OrganisationMetamacDto expected, OrganisationMetamacDto actual) {
        // note: mo metadata in SRM module
        OrganisationsAsserts.assertEqualsOrganisationDto(expected, actual);
    }

    public static void assertEqualsOrganisation(OrganisationMetamacDto expected, OrganisationMetamac actual) {
        assertEqualsOrganisation(actual, expected, MapperEnum.DTO2DO);
    }

    public static void assertEqualsOrganisation(OrganisationMetamac expected, OrganisationMetamacBasicDto actual) {
        assertEquals(expected.getOrganisationType(), actual.getType());
        BaseAsserts.assertEqualsItemBasicDto(expected, actual);
    }

    private static void assertEqualsOrganisationScheme(OrganisationSchemeVersionMetamac entity, OrganisationSchemeMetamacDto dto, MapperEnum mapperEnum) {
        // Metamac: no metadata

        // Sdmx
        OrganisationsAsserts.assertEqualsOrganisationScheme(entity, dto, mapperEnum);
    }

    private static void assertEqualsOrganisation(OrganisationMetamac entity, OrganisationMetamacDto dto, MapperEnum mapperEnum) {
        // Metamac: no metadata

        // Sdmx
        OrganisationsAsserts.assertEqualsOrganisation(entity, dto, mapperEnum);
    }
}
