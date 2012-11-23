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
import org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencySchemes;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.MockitoVerify;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public class OrganisationsMockitoVerify extends MockitoVerify {

    public static void verifyFindAgencySchemes(OrganisationsMetamacService organisationsService, String agencyID, String resourceID, String limit, String offset, String query, String orderBy,
            AgencySchemes actual) throws Exception {
        verifyFindOrganisationSchemes(organisationsService, agencyID, resourceID, limit, offset, query, orderBy, RestInternalConstants.KIND_AGENCY_SCHEMES, OrganisationSchemeTypeEnum.AGENCY_SCHEME,
                actual);
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

    // TODO
    // @SuppressWarnings({"rawtypes", "unchecked"})
    // public static void verifyFindOrganisations(OrganisationsMetamacService organisationsService, String agencyID, String resourceID, String version, String limit, String offset, String query,
    // String orderBy,
    // Concepts conceptsActual) throws Exception {
    //
    // assertNotNull(conceptsActual);
    // assertEquals(RestInternalConstants.KIND_CONCEPTS, conceptsActual.getKind());
    //
    // // Verify
    // ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
    // ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
    // verify(organisationsService).findOrganisationsByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());
    //
    // // Validate
    // List<ConditionalCriteria> conditionalCriteriaExpected = buildExpectedConditionalCriteriaToFindItems(agencyID, resourceID, version, query, orderBy, ConceptMetamac.class);
    // MetamacRestAsserts.assertEqualsConditionalCriteria(conditionalCriteriaExpected, conditions.getValue());
    //
    // PagingParameter pagingParameterExpected = buildExpectedPagingParameter(offset, limit);
    // MetamacRestAsserts.assertEqualsPagingParameter(pagingParameterExpected, pagingParameter.getValue());
    // }

    private static List<ConditionalCriteria> buildExpectedConditionalCriteriaToFindOrganisationSchemes(String agencyID, String resourceID, String query, String orderBy, OrganisationSchemeTypeEnum type) {
        List<ConditionalCriteria> expected = buildExpectedConditionalCriteriaToFindItemSchemes(agencyID, resourceID, query, orderBy, OrganisationSchemeVersionMetamac.class);
        if (type != null) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(OrganisationSchemeVersionMetamac.class).withProperty(OrganisationSchemeVersionMetamacProperties.organisationSchemeType()).eq(type)
                    .buildSingle());
        }
        return expected;
    }
}