package org.siemac.metamac.srm.rest.internal.v1_0.concept.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.mockito.ArgumentCaptor;
import org.siemac.metamac.rest.common.test.utils.MetamacRestAsserts;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concepts;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.MockitoVerify;

public class ConceptsMockitoVerify extends MockitoVerify {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void verifyFindConceptSchemes(ConceptsMetamacService conceptsService, String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy,
            ConceptSchemes conceptSchemesActual) throws Exception {

        assertNotNull(conceptSchemesActual);
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEMES, conceptSchemesActual.getKind());

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(conceptsService).findConceptSchemesByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        List<ConditionalCriteria> conditionalCriteriaExpected = buildExpectedConditionalCriteriaToFindItemSchemes(agencyID, resourceID, version, query, orderBy, ConceptSchemeVersionMetamac.class);
        MetamacRestAsserts.assertEqualsConditionalCriteria(conditionalCriteriaExpected, conditions.getValue());

        PagingParameter pagingParameterExpected = buildExpectedPagingParameter(offset, limit);
        MetamacRestAsserts.assertEqualsPagingParameter(pagingParameterExpected, pagingParameter.getValue());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void verifyFindConcepts(ConceptsMetamacService conceptsService, String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy,
            Concepts conceptsActual) throws Exception {

        assertNotNull(conceptsActual);
        assertEquals(RestInternalConstants.KIND_CONCEPTS, conceptsActual.getKind());

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(conceptsService).findConceptsByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        List<ConditionalCriteria> conditionalCriteriaExpected = buildExpectedConditionalCriteriaToFindItems(agencyID, resourceID, version, query, orderBy, ConceptMetamac.class);
        MetamacRestAsserts.assertEqualsConditionalCriteria(conditionalCriteriaExpected, conditions.getValue());

        PagingParameter pagingParameterExpected = buildExpectedPagingParameter(offset, limit);
        MetamacRestAsserts.assertEqualsPagingParameter(pagingParameterExpected, pagingParameter.getValue());
    }
}