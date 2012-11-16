package org.siemac.metamac.srm.rest.internal.v1_0.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ORDER_BY_CONCEPT_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ORDER_BY_CONCEPT_SCHEME_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.QUERY_CONCEPT_ID_LIKE_1_NAME_LIKE_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.QUERY_CONCEPT_SCHEME_ID_LIKE_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.mockito.ArgumentCaptor;
import org.siemac.metamac.rest.common.test.utils.MetamacRestAsserts;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concepts;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;

public class SrmRestAsserts extends MetamacRestAsserts {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void assertFindConceptSchemes(ConceptsMetamacService conceptsService, String agencyID, String resourceID, String limit, String offset, String query, String orderBy,
            ConceptSchemes conceptSchemesActual) throws Exception {

        assertNotNull(conceptSchemesActual);
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEMES, conceptSchemesActual.getKind());

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(conceptsService).findConceptSchemesByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        MetamacRestAsserts.assertEqualsConditionalCriteria(buildFindConceptSchemesExpectedConditionalCriterias(agencyID, resourceID, query, orderBy), conditions.getValue());
        MetamacRestAsserts.assertEqualsPagingParameter(buildExpectedPagingParameter(offset, limit), pagingParameter.getValue());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void assertFindConcepts(ConceptsMetamacService conceptsService, String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy,
            Concepts conceptsActual) throws Exception {

        assertNotNull(conceptsActual);
        assertEquals(RestInternalConstants.KIND_CONCEPTS, conceptsActual.getKind());

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(conceptsService).findConceptsByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        MetamacRestAsserts.assertEqualsConditionalCriteria(buildFindConceptsExpectedConditionalCriterias(agencyID, resourceID, version, query, orderBy), conditions.getValue());
        MetamacRestAsserts.assertEqualsPagingParameter(buildExpectedPagingParameter(offset, limit), pagingParameter.getValue());
    }

    public static void assertEqualsResource(ConceptSchemeVersionMetamac source, Resource target) {
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEME, target.getKind());
        assertEquals(source.getMaintainableArtefact().getCode(), target.getId());
        assertEquals(source.getMaintainableArtefact().getUrn(), target.getUrn());
        assertEquals("http://data.istac.es/apis/srm/v1.0/conceptschemes/" + source.getMaintainableArtefact().getMaintainer().getIdAsMaintainer() + "/" + source.getMaintainableArtefact().getCode()
                + "/" + source.getMaintainableArtefact().getVersionLogic(), target.getSelfLink());
        assertEqualsInternationalString(source.getMaintainableArtefact().getName(), target.getTitle());
    }

    public static void assertEqualsResource(ConceptMetamac source, Resource target) {
        assertEquals(RestInternalConstants.KIND_CONCEPT, target.getKind());
        assertEquals(source.getNameableArtefact().getCode(), target.getId());
        assertEquals(source.getNameableArtefact().getUrn(), target.getUrn());
        assertEquals("http://data.istac.es/apis/srm/v1.0/conceptschemes/" + source.getItemSchemeVersion().getMaintainableArtefact().getMaintainer().getIdAsMaintainer() + "/"
                + source.getItemSchemeVersion().getMaintainableArtefact().getCode() + "/" + source.getItemSchemeVersion().getMaintainableArtefact().getVersionLogic() + "/concepts/"
                + source.getNameableArtefact().getCode(), target.getSelfLink());
        assertEqualsInternationalString(source.getNameableArtefact().getName(), target.getTitle());
    }

    private static List<ConditionalCriteria> buildFindConceptSchemesExpectedConditionalCriterias(String agencyID, String resourceID, String query, String orderBy) {
        List<ConditionalCriteria> expected = new ArrayList<ConditionalCriteria>();
        expected.addAll(buildFindConceptSchemesExpectedOrder(orderBy));
        expected.addAll(buildFindConceptSchemesExpectedQuery(query));
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

    private static List<ConditionalCriteria> buildFindConceptsExpectedConditionalCriterias(String agencyID, String resourceID, String version, String query, String orderBy) {
        List<ConditionalCriteria> expected = new ArrayList<ConditionalCriteria>();
        expected.addAll(buildFindConceptsExpectedOrderBy(orderBy));
        expected.addAll(buildFindConceptsExpectedQuery(query));
        expected.add(ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).distinctRoot().buildSingle());
        expected.add(ConditionalCriteriaBuilder
                .criteriaFor(ConceptMetamac.class)
                .withProperty(
                        new LeafProperty<ConceptMetamac>(ConceptMetamacProperties.itemSchemeVersion().getName(), ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().procStatus().getName(),
                                true, ConceptMetamac.class)).in(ProcStatusEnum.INTERNALLY_PUBLISHED, ProcStatusEnum.EXTERNALLY_PUBLISHED).buildSingle());
        if (agencyID != null && !RestInternalConstants.WILDCARD.equals(agencyID)) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().maintainer().idAsMaintainer())
                    .eq(agencyID).buildSingle());
        }
        if (resourceID != null && !RestInternalConstants.WILDCARD.equals(resourceID)) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().code()).eq(resourceID)
                    .buildSingle());
        }
        if (version != null && !RestInternalConstants.WILDCARD.equals(version)) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().versionLogic()).eq(version)
                    .buildSingle());
        }
        return expected;
    }

    private static List<ConditionalCriteria> buildFindConceptSchemesExpectedOrder(String orderBy) {
        if (orderBy == null) {
            return ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().code()).ascending().build();
        }
        if (ORDER_BY_CONCEPT_SCHEME_ID_DESC.equals(orderBy)) {
            return ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().code()).descending().build();
        }
        fail();
        return null;
    }

    private static List<ConditionalCriteria> buildFindConceptsExpectedOrderBy(String orderBy) {
        if (orderBy == null) {
            return ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).orderBy(ConceptMetamacProperties.nameableArtefact().code()).ascending().build();
        }
        if (ORDER_BY_CONCEPT_ID_DESC.equals(orderBy)) {
            return ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).orderBy(ConceptMetamacProperties.nameableArtefact().code()).descending().build();
        }
        fail();
        return null;
    }

    private static List<ConditionalCriteria> buildFindConceptSchemesExpectedQuery(String query) {
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

    private static List<ConditionalCriteria> buildFindConceptsExpectedQuery(String query) {
        if (query == null) {
            return new ArrayList<ConditionalCriteria>();
        }
        if (QUERY_CONCEPT_ID_LIKE_1_NAME_LIKE_2.equals(query)) {
            return ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.nameableArtefact().code()).like("%1%")
                    .withProperty(ConceptMetamacProperties.nameableArtefact().name().texts().label()).like("%2%").build();
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

    private static void assertEqualsInternationalString(org.siemac.metamac.core.common.ent.domain.InternationalString expecteds, InternationalString actuals) {
        assertEqualsNullability(expecteds, actuals);
        if (expecteds == null) {
            return;
        }
        assertEquals(expecteds.getTexts().size(), actuals.getTexts().size());
        for (org.siemac.metamac.core.common.ent.domain.LocalisedString expected : expecteds.getTexts()) {
            boolean existsItem = false;
            for (LocalisedString actual : actuals.getTexts()) {
                if (expected.getLocale().equals(actual.getLang())) {
                    assertEquals(expected.getLabel(), actual.getValue());
                    existsItem = true;
                }
            }
            assertTrue(existsItem);
        }
    }
}