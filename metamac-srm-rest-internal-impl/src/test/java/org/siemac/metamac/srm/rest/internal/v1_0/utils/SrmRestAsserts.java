package org.siemac.metamac.srm.rest.internal.v1_0.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.siemac.metamac.srm.rest.internal.v1_0.service.SrmRestInternalFacadeV10Test.QUERY_CONCEPT_SCHEME_ID_LIKE_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.service.SrmRestInternalFacadeV10Test.QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.mockito.ArgumentCaptor;
import org.siemac.metamac.rest.common.test.utils.MetamacRestAsserts;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
public class SrmRestAsserts {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void assertFindConceptSchemes(ConceptsMetamacService conceptsService, String agencyID, String resourceID, String limit, String offset, String query, String orderBy, ConceptSchemes conceptSchemesActual) throws Exception {

        assertNotNull(conceptSchemesActual);
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEMES, conceptSchemesActual.getKind());

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(conceptsService).findConceptSchemesByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        MetamacRestAsserts.assertEqualsConditionalCriteria(buildExpectedConditionalCriteria(agencyID, resourceID, query, orderBy), conditions.getValue());
        MetamacRestAsserts.assertEqualsPagingParameter(buildExpectedPagingParameter(offset, limit), pagingParameter.getValue());
    }
    

    private static List<ConditionalCriteria> buildExpectedConditionalCriteria(String agencyID, String resourceID, String query, String orderBy) {
        List<ConditionalCriteria> expected = new ArrayList<ConditionalCriteria>();
        expected.addAll(buildExpectedConditionalCriteriaOrderBy(orderBy));
        expected.addAll(buildExpectedConditionalCriteriaQuery(query));
        expected.add(ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).distinctRoot().buildSingle());
        expected.add(ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().procStatus())
                .in(ProcStatusEnum.INTERNALLY_PUBLISHED, ProcStatusEnum.EXTERNALLY_PUBLISHED).buildSingle());
        if (agencyID != null && !RestInternalConstants.WILDCARD.equals(agencyID)) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                    .withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().maintainer().idAsMaintainer()).eq(agencyID).buildSingle());
        }
        if (resourceID != null && !RestInternalConstants.WILDCARD.equals(resourceID)) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().code()).eq(resourceID)
                    .buildSingle());
        }
        return expected;
    }

    private static List<ConditionalCriteria> buildExpectedConditionalCriteriaOrderBy(String orderBy) {
        if (orderBy == null) {
            return ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().code()).ascending().build();
        } else {
            fail();
            return null;
        }
    }

    private static List<ConditionalCriteria> buildExpectedConditionalCriteriaQuery(String query) {
        if (query == null) {
            return new ArrayList<ConditionalCriteria>();
        }
        if (QUERY_CONCEPT_SCHEME_ID_LIKE_1.equals(query)) {
            return ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().code()).like("%1%").build();
        } else if (QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2.equals(query)) {
            return ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().code()).like("%1%")
                    .withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().name().texts().label()).like("%2%").build();
        }
        fail();
        return null;
    }

    private static PagingParameter buildExpectedPagingParameter(String offset, String limit) {
        Integer startRow = null;
        if (offset == null) {
            startRow = Integer.valueOf(0);
        } else {
            startRow = Integer.valueOf(offset);
        }
        Integer maximumResultSize = null;
        if (limit == null) {
            maximumResultSize = Integer.valueOf(25);
        } else {
            maximumResultSize = Integer.valueOf(limit);
        }
        if (maximumResultSize > Integer.valueOf(1000)) {
            maximumResultSize = Integer.valueOf(1000);
        }
        int endRow = startRow + maximumResultSize;
        return PagingParameter.rowAccess(startRow, endRow, false);
    }
}