package org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.mockito.ArgumentCaptor;
import org.siemac.metamac.rest.common.test.utils.MetamacRestAsserts;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacProperties;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.MockitoVerify;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class OrganisationsMockitoVerify extends MockitoVerify {

    public static void verifyFindOrganisationSchemes(OrganisationsMetamacService organisationsService, String agencyID, String resourceID, String version, String limit, String offset, String query,
            String orderBy, OrganisationSchemeTypeEnum type) throws Exception {
        verifyFindOrganisationSchemes(organisationsService, agencyID, resourceID, version, query, orderBy, buildExpectedPagingParameter(offset, limit), type);
    }

    public static void verifyRetrieveOrganisationScheme(OrganisationsMetamacService organisationsService, String agencyID, String resourceID, String version, OrganisationSchemeTypeEnum type)
            throws Exception {
        verifyFindOrganisationSchemes(organisationsService, agencyID, resourceID, version, null, null, PagingParameter.pageAccess(1, 1), type);
    }

    public static void verifyFindOrganisations(OrganisationsMetamacService organisationsService, String agencyID, String resourceID, String version, String limit, String offset, String query,
            String orderBy, OrganisationTypeEnum type) throws Exception {
        verifyFindOrganisations(organisationsService, agencyID, resourceID, version, null, query, orderBy, buildExpectedPagingParameter(offset, limit), type);
    }

    public static void verifyRetrieveOrganisation(OrganisationsMetamacService organisationsService, String agencyID, String resourceID, String version, String itemID, OrganisationTypeEnum type)
            throws Exception {
        verifyFindOrganisations(organisationsService, agencyID, resourceID, version, itemID, null, null, PagingParameter.pageAccess(1, 1), type);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void verifyFindOrganisations(OrganisationsMetamacService organisationsService, String agencyID, String resourceID, String version, String itemID, String query, String orderBy,
            PagingParameter pagingParameterExpected, OrganisationTypeEnum type) throws Exception {

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(organisationsService).findOrganisationsByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        List<ConditionalCriteria> conditionalCriteriaExpected = buildExpectedConditionalCriteriaToFindOrganisations(agencyID, resourceID, version, itemID, query, orderBy, type);
        MetamacRestAsserts.assertEqualsConditionalCriteria(conditionalCriteriaExpected, conditions.getValue());

        MetamacRestAsserts.assertEqualsPagingParameter(pagingParameterExpected, pagingParameter.getValue());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void verifyFindOrganisationSchemes(OrganisationsMetamacService organisationsService, String agencyID, String resourceID, String version, String query, String orderBy,
            PagingParameter pagingParameterExpected, OrganisationSchemeTypeEnum type) throws Exception {

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(organisationsService).findOrganisationSchemesByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        List<ConditionalCriteria> conditionalCriteriaExpected = buildExpectedConditionalCriteriaToFindOrganisationSchemes(agencyID, resourceID, version, query, orderBy, type);
        MetamacRestAsserts.assertEqualsConditionalCriteria(conditionalCriteriaExpected, conditions.getValue());

        MetamacRestAsserts.assertEqualsPagingParameter(pagingParameterExpected, pagingParameter.getValue());
    }

    private static List<ConditionalCriteria> buildExpectedConditionalCriteriaToFindOrganisationSchemes(String agencyID, String resourceID, String version, String query, String orderBy,
            OrganisationSchemeTypeEnum type) {
        List<ConditionalCriteria> expected = buildExpectedConditionalCriteriaToFindItemSchemes(agencyID, resourceID, version, query, orderBy, OrganisationSchemeVersionMetamac.class);
        if (type != null) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(OrganisationSchemeVersionMetamac.class).withProperty(OrganisationSchemeVersionMetamacProperties.organisationSchemeType()).eq(type)
                    .buildSingle());
        }
        return expected;
    }

    private static List<ConditionalCriteria> buildExpectedConditionalCriteriaToFindOrganisations(String agencyID, String resourceID, String version, String itemID, String query, String orderBy,
            OrganisationTypeEnum type) {
        List<ConditionalCriteria> expected = buildExpectedConditionalCriteriaToFindItems(agencyID, resourceID, version, itemID, query, orderBy, OrganisationMetamac.class);
        if (type != null) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(OrganisationMetamac.class).withProperty(OrganisationMetamacProperties.organisationType()).eq(type).buildSingle());
        }
        return expected;
    }
}