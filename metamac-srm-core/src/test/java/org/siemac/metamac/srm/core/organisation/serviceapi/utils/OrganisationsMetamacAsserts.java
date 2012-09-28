package org.siemac.metamac.srm.core.organisation.serviceapi.utils;

import org.siemac.metamac.srm.core.base.utils.BaseAsserts;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
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

    public static void assertEqualsOrganisationScheme(OrganisationSchemeVersionMetamac entity, OrganisationSchemeMetamacDto dto) {
        OrganisationsAsserts.assertEqualsOrganisationScheme(entity, dto);
    }

    // ORGANISATIONS

    // public static void assertEqualsOrganisation(OrganisationMetamac expected, OrganisationMetamac actual) {
    // assertEqualsInternationalString(expected.getPluralName(), actual.getPluralName());
    // assertEqualsInternationalString(expected.getAcronym(), actual.getAcronym());
    // assertEqualsInternationalString(expected.getDescriptionSource(), actual.getDescriptionSource());
    // assertEqualsInternationalString(expected.getContext(), actual.getContext());
    // assertEqualsInternationalString(expected.getDocMethod(), actual.getDocMethod());
    // assertEquals(expected.getSdmxRelatedArtefact(), actual.getSdmxRelatedArtefact());
    // assertEqualsOrganisationType(expected.getType(), actual.getType());
    // assertEqualsInternationalString(expected.getDerivation(), actual.getDerivation());
    // assertEqualsInternationalString(expected.getLegalActs(), actual.getLegalActs());
    // assertEqualsOrganisationExtends(expected.getOrganisationExtends(), actual.getOrganisationExtends());
    // OrganisationsAsserts.assertEqualsOrganisation(expected, actual);
    // }

    // public static void assertEqualsOrganisationDto(OrganisationMetamacDto expected, OrganisationMetamacDto actual) {
    // assertEqualsInternationalStringDto(expected.getPluralName(), actual.getPluralName());
    // assertEqualsInternationalStringDto(expected.getAcronym(), actual.getAcronym());
    // assertEqualsInternationalStringDto(expected.getDescriptionSource(), actual.getDescriptionSource());
    // assertEqualsInternationalStringDto(expected.getContext(), actual.getContext());
    // assertEqualsInternationalStringDto(expected.getDocMethod(), actual.getDocMethod());
    // assertEquals(expected.getSdmxRelatedArtefact(), actual.getSdmxRelatedArtefact());
    // assertEqualsOrganisationType(expected.getType(), actual.getType());
    // assertEqualsInternationalStringDto(expected.getDerivation(), actual.getDerivation());
    // assertEqualsInternationalStringDto(expected.getLegalActs(), actual.getLegalActs());
    // assertEquals(expected.getOrganisationExtendsUrn(), actual.getOrganisationExtendsUrn());
    //
    // OrganisationsAsserts.assertEqualsOrganisationDto(expected, actual);
    // }

}
