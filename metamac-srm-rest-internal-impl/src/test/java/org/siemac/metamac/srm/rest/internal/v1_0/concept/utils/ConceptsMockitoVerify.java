package org.siemac.metamac.srm.rest.internal.v1_0.concept.utils;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.mockito.ArgumentCaptor;
import org.siemac.metamac.rest.common.test.utils.MetamacRestAsserts;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.MockitoVerify;

public class ConceptsMockitoVerify extends MockitoVerify {

    public static void verifyRetrieveConceptScheme(ConceptsMetamacService conceptsService, String agencyID, String resourceID, String version) throws Exception {
        verifyFindConceptSchemes(conceptsService, agencyID, resourceID, version, null, null, buildExpectedPagingParameterRetrieveOne(), RestOperationEnum.RETRIEVE);
    }

    public static void verifyFindConceptSchemes(ConceptsMetamacService conceptsService, String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy)
            throws Exception {
        verifyFindConceptSchemes(conceptsService, agencyID, resourceID, version, query, orderBy, buildExpectedPagingParameter(offset, limit), RestOperationEnum.FIND);
    }

    public static void verifyRetrieveConcept(ConceptsMetamacService conceptsService, String agencyID, String resourceID, String version, String itemID) throws Exception {
        verifyFindConcepts(conceptsService, agencyID, resourceID, version, itemID, null, null, buildExpectedPagingParameterRetrieveOne(), RestOperationEnum.RETRIEVE);
    }

    public static void verifyFindConcepts(ConceptsMetamacService conceptsService, String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy)
            throws Exception {
        verifyFindConcepts(conceptsService, agencyID, resourceID, version, null, query, orderBy, buildExpectedPagingParameter(offset, limit), RestOperationEnum.FIND);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void verifyFindConceptSchemes(ConceptsMetamacService conceptsService, String agencyID, String resourceID, String version, String query, String orderBy,
            PagingParameter pagingParameterExpected, RestOperationEnum restOperation) throws Exception {

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(conceptsService).findConceptSchemesByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        List<ConditionalCriteria> conditionalCriteriaExpected = buildExpectedConditionalCriteriaToFindItemSchemes(agencyID, resourceID, version, query, orderBy, ConceptSchemeVersionMetamac.class,
                restOperation);
        MetamacRestAsserts.assertEqualsConditionalCriteria(conditionalCriteriaExpected, conditions.getValue());

        MetamacRestAsserts.assertEqualsPagingParameter(pagingParameterExpected, pagingParameter.getValue());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void verifyFindConcepts(ConceptsMetamacService conceptsService, String agencyID, String resourceID, String version, String itemID, String query, String orderBy,
            PagingParameter pagingParameterExpected, RestOperationEnum restOperation) throws Exception {

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(conceptsService).findConceptsByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        List<ConditionalCriteria> conditionalCriteriaExpected = buildExpectedConditionalCriteriaToFindItems(agencyID, resourceID, version, itemID, query, orderBy, ConceptMetamac.class,
                ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact(), ConceptMetamacProperties.nameableArtefact(), restOperation);
        MetamacRestAsserts.assertEqualsConditionalCriteria(conditionalCriteriaExpected, conditions.getValue());

        MetamacRestAsserts.assertEqualsPagingParameter(pagingParameterExpected, pagingParameter.getValue());
    }
}