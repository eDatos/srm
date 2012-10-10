package org.siemac.metamac.srm.core.organisation.serviceapi.utils;

import org.siemac.metamac.srm.core.base.utils.BaseAsserts;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.arte.statistic.sdmx.srm.core.organisation.serviceapi.utils.OrganisationsAsserts;

public class OrganisationsMetamacAsserts extends OrganisationsAsserts {

    // ORGANISATION SCHEMES

    public static void assertEqualsOrganisationScheme(OrganisationSchemeVersionMetamac expected, OrganisationSchemeVersionMetamac actual) {
        BaseAsserts.assertEqualsLifeCycle(expected.getLifecycleMetadata(), actual.getLifecycleMetadata());
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

    public static void assertEqualsOrganisation(OrganisationMetamac expected, OrganisationMetamacDto actual) {
        assertEqualsOrganisation(expected, actual, MapperEnum.DO2DTO);
    }

    public static void assertEqualsOrganisation(OrganisationMetamacDto expected, OrganisationMetamac actual) {
        assertEqualsOrganisation(actual, expected, MapperEnum.DTO2DO);
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
