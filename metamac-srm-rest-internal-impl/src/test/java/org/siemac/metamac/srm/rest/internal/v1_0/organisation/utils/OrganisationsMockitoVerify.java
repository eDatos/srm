package org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.mockito.ArgumentCaptor;
import org.siemac.metamac.rest.common.test.utils.MetamacRestAsserts;
import org.siemac.metamac.rest.common.v1_0.domain.ListBase;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Agencies;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencySchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationSchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnitSchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnits;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Organisations;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacProperties;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.MockitoVerify;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class OrganisationsMockitoVerify extends MockitoVerify {

    public static void verifyFindOrganisationSchemesNoType(OrganisationsMetamacService organisationsService, String agencyID, String resourceID, String limit, String offset, String query,
            String orderBy, OrganisationSchemes actual) throws Exception {
        verifyFindOrganisationSchemes(organisationsService, agencyID, resourceID, limit, offset, query, orderBy, RestInternalConstants.KIND_ORGANISATION_SCHEMES, null, actual);
    }

    public static void verifyFindAgencySchemes(OrganisationsMetamacService organisationsService, String agencyID, String resourceID, String limit, String offset, String query, String orderBy,
            AgencySchemes actual) throws Exception {
        verifyFindOrganisationSchemes(organisationsService, agencyID, resourceID, limit, offset, query, orderBy, RestInternalConstants.KIND_AGENCY_SCHEMES, OrganisationSchemeTypeEnum.AGENCY_SCHEME,
                actual);
    }

    public static void verifyFindOrganisationUnitSchemes(OrganisationsMetamacService organisationsService, String agencyID, String resourceID, String limit, String offset, String query,
            String orderBy, OrganisationUnitSchemes actual) throws Exception {
        verifyFindOrganisationSchemes(organisationsService, agencyID, resourceID, limit, offset, query, orderBy, RestInternalConstants.KIND_ORGANISATION_UNIT_SCHEMES,
                OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME, actual);
    }

    public static void verifyFindOrganisationsNoType(OrganisationsMetamacService organisationsService, String agencyID, String resourceID, String version, String limit, String offset, String query,
            String orderBy, Organisations actual) throws Exception {
        verifyFindOrganisations(organisationsService, agencyID, resourceID, version, limit, offset, query, orderBy, RestInternalConstants.KIND_ORGANISATIONS, null, actual);
    }
    
    public static void verifyFindAgencies(OrganisationsMetamacService organisationsService, String agencyID, String resourceID, String version, String limit, String offset, String query,
            String orderBy, Agencies actual) throws Exception {
        verifyFindOrganisations(organisationsService, agencyID, resourceID, version, limit, offset, query, orderBy, RestInternalConstants.KIND_AGENCIES, OrganisationTypeEnum.AGENCY, actual);
    }

    public static void verifyFindOrganisationUnits(OrganisationsMetamacService organisationsService, String agencyID, String resourceID, String version, String limit, String offset, String query,
            String orderBy, OrganisationUnits actual) throws Exception {
        verifyFindOrganisations(organisationsService, agencyID, resourceID, version, limit, offset, query, orderBy, RestInternalConstants.KIND_ORGANISATION_UNITS,
                OrganisationTypeEnum.ORGANISATION_UNIT, actual);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void verifyFindOrganisations(OrganisationsMetamacService organisationsService, String agencyID, String resourceID, String version, String limit, String offset, String query,
            String orderBy, String kindExpected, OrganisationTypeEnum type, ListBase actual) throws Exception {

        assertNotNull(actual);
        assertEquals(kindExpected, actual.getKind());

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(organisationsService).findOrganisationsByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        List<ConditionalCriteria> conditionalCriteriaExpected = buildExpectedConditionalCriteriaToFindOrganisations(agencyID, resourceID, version, query, orderBy, type);
        MetamacRestAsserts.assertEqualsConditionalCriteria(conditionalCriteriaExpected, conditions.getValue());

        PagingParameter pagingParameterExpected = buildExpectedPagingParameter(offset, limit);
        MetamacRestAsserts.assertEqualsPagingParameter(pagingParameterExpected, pagingParameter.getValue());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void verifyFindOrganisationSchemes(OrganisationsMetamacService organisationsService, String agencyID, String resourceID, String limit, String offset, String query, String orderBy,
            String kindExpected, OrganisationSchemeTypeEnum type, ListBase actual) throws Exception {

        assertNotNull(actual);
        assertEquals(kindExpected, actual.getKind());

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(organisationsService).findOrganisationSchemesByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        List<ConditionalCriteria> conditionalCriteriaExpected = buildExpectedConditionalCriteriaToFindOrganisationSchemes(agencyID, resourceID, query, orderBy, type);
        MetamacRestAsserts.assertEqualsConditionalCriteria(conditionalCriteriaExpected, conditions.getValue());

        PagingParameter pagingParameterExpected = buildExpectedPagingParameter(offset, limit);
        MetamacRestAsserts.assertEqualsPagingParameter(pagingParameterExpected, pagingParameter.getValue());
    }

    private static List<ConditionalCriteria> buildExpectedConditionalCriteriaToFindOrganisationSchemes(String agencyID, String resourceID, String query, String orderBy, OrganisationSchemeTypeEnum type) {
        List<ConditionalCriteria> expected = buildExpectedConditionalCriteriaToFindItemSchemes(agencyID, resourceID, query, orderBy, OrganisationSchemeVersionMetamac.class);
        if (type != null) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(OrganisationSchemeVersionMetamac.class).withProperty(OrganisationSchemeVersionMetamacProperties.organisationSchemeType()).eq(type)
                    .buildSingle());
        }
        return expected;
    }

    private static List<ConditionalCriteria> buildExpectedConditionalCriteriaToFindOrganisations(String agencyID, String resourceID, String version, String query, String orderBy,
            OrganisationTypeEnum type) {
        List<ConditionalCriteria> expected = buildExpectedConditionalCriteriaToFindItems(agencyID, resourceID, version, query, orderBy, OrganisationMetamac.class);
        if (type != null) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(OrganisationMetamac.class).withProperty(OrganisationMetamacProperties.organisationType()).eq(type).buildSingle());
        }
        return expected;
    }
}