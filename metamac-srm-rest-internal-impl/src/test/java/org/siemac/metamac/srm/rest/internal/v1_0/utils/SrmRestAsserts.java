package org.siemac.metamac.srm.rest.internal.v1_0.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.QUERY_ID_LIKE_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.mockito.ArgumentCaptor;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.rest.common.test.utils.MetamacRestAsserts;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Category;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.CategoryScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Urns;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CategoryType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.ConceptType;

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

    public static void assertEqualsResource(ConceptSchemeVersionMetamac expected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/srm/v1.0/conceptschemes/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/" + maintainableArtefact.getCode() + "/"
                + maintainableArtefact.getVersionLogic();

        assertEqualsResource(expected, RestInternalConstants.KIND_CONCEPT_SCHEME, expectedSelfLink, actual);
    }

    public static void assertEqualsResource(CategorySchemeVersionMetamac expected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/srm/v1.0/categoryschemes/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/" + maintainableArtefact.getCode() + "/"
                + maintainableArtefact.getVersionLogic();

        assertEqualsResource(expected, RestInternalConstants.KIND_CATEGORY_SCHEME, expectedSelfLink, actual);
    }

    public static void assertEqualsResource(ConceptMetamac expected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getItemSchemeVersion().getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/srm/v1.0/conceptschemes/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/" + maintainableArtefact.getCode() + "/"
                + maintainableArtefact.getVersionLogic() + "/concepts/" + expected.getNameableArtefact().getCode();
        assertEqualsResource(expected, RestInternalConstants.KIND_CONCEPT, expectedSelfLink, actual);
    }

    public static void assertEqualsResource(CategoryMetamac expected, Resource actual) {
        MaintainableArtefact maintainableArtefact = expected.getItemSchemeVersion().getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/srm/v1.0/categoryschemes/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/" + maintainableArtefact.getCode() + "/"
                + maintainableArtefact.getVersionLogic() + "/categories/" + expected.getNameableArtefact().getCode();
        assertEqualsResource(expected, RestInternalConstants.KIND_CATEGORY, expectedSelfLink, actual);
    }

    public static void assertEqualsConceptScheme(ConceptSchemeVersionMetamac source, ConceptScheme target) {
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEME, target.getKind());
        String parentLink = "http://data.istac.es/apis/srm/v1.0/conceptschemes";
        String selfLink = parentLink + "/" + source.getMaintainableArtefact().getMaintainer().getIdAsMaintainer() + "/" + source.getMaintainableArtefact().getCode() + "/"
                + source.getMaintainableArtefact().getVersionLogic();
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEME, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEMES, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertEquals(source.getType().toString(), target.getType().toString());
        MetamacAsserts.assertEqualsNullability(source.getRelatedOperation(), target.getRelatedOperation());
        if (source.getRelatedOperation() != null) {
            assertEquals(source.getRelatedOperation().getUrn(), target.getRelatedOperation().getUrn());
        }
        assertEquals(source.getMaintainableArtefact().getReplaceToVersion(), target.getReplaceToVersion());
        assertEquals(BigInteger.ONE, target.getChildLinks().getTotal());
        assertEquals(RestInternalConstants.KIND_CONCEPTS, target.getChildLinks().getChildLinks().get(0).getKind());
        assertEquals(selfLink + "/concepts", target.getChildLinks().getChildLinks().get(0).getHref());

        // Concepts (SDMX type)
        assertEquals(source.getItems().size(), target.getConcepts().size());
        for (int i = 0; i < source.getItems().size(); i++) {
            assertTrue(target.getConcepts().get(i) instanceof ConceptType);
            assertFalse(target.getConcepts().get(i) instanceof Concept);

            assertEqualsConceptSdmx((ConceptMetamac) source.getItems().get(i), target.getConcepts().get(i));
        }
    }
    public static void assertEqualsConceptSdmx(ConceptMetamac source, ConceptType target) {
        // Only test some metadata because SDMX metadata is tested in SDMX project
        // Test something...
        assertEquals(source.getNameableArtefact().getCode(), target.getId());
        assertEquals(source.getNameableArtefact().getUrn(), target.getUrn());
        assertEqualsNullability(source.getParent(), target.getParent());
        if (source.getParent() != null) {
            assertEquals(source.getParent().getNameableArtefact().getCode(), target.getParent().getRef().getId());
        }
    }

    public static void assertEqualsConcept(ConceptMetamac source, Concept target) {

        assertEquals(RestInternalConstants.KIND_CONCEPT, target.getKind());
        String parentLink = "http://data.istac.es/apis/srm/v1.0/conceptschemes" + "/" + source.getItemSchemeVersion().getMaintainableArtefact().getMaintainer().getIdAsMaintainer() + "/"
                + source.getItemSchemeVersion().getMaintainableArtefact().getCode() + "/" + source.getItemSchemeVersion().getMaintainableArtefact().getVersionLogic() + "/concepts";
        String selfLink = parentLink + "/" + source.getNameableArtefact().getCode();
        assertEquals(RestInternalConstants.KIND_CONCEPT, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(RestInternalConstants.KIND_CONCEPTS, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertNull(target.getChildLinks());

        assertEqualsInternationalStringNotNull(source.getPluralName(), target.getPluralName());
        assertEqualsInternationalStringNotNull(source.getAcronym(), target.getAcronym());
        assertEqualsInternationalStringNotNull(source.getDescriptionSource(), target.getDescriptionSource());
        assertEqualsInternationalStringNotNull(source.getContext(), target.getContext());
        assertEqualsInternationalStringNotNull(source.getDocMethod(), target.getDocMethod());
        assertEqualsInternationalStringNotNull(source.getDerivation(), target.getDerivation());
        assertEqualsInternationalStringNotNull(source.getLegalActs(), target.getLegalActs());

        assertEquals(source.getType().getIdentifier(), target.getType().getId());
        assertEqualsInternationalStringNotNull(source.getType().getDescription(), target.getType().getTitle());

        assertEqualsUrnsNotNull(source.getRoleConcepts(), target.getRoles());
        assertEqualsUrnsNotNull(source.getRelatedConcepts(), target.getRelatedConcepts());
        assertEquals(source.getConceptExtends().getNameableArtefact().getUrn(), target.getExtends());

        // Sdmx
        assertEqualsConceptSdmx(source, target);
    }

    public static void assertEqualsCategoryScheme(CategorySchemeVersionMetamac source, CategoryScheme target) {
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEME, target.getKind());
        String parentLink = "http://data.istac.es/apis/srm/v1.0/categoryschemes";
        String selfLink = parentLink + "/" + source.getMaintainableArtefact().getMaintainer().getIdAsMaintainer() + "/" + source.getMaintainableArtefact().getCode() + "/"
                + source.getMaintainableArtefact().getVersionLogic();
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEME, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(RestInternalConstants.KIND_CATEGORY_SCHEMES, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertEquals(source.getMaintainableArtefact().getReplaceToVersion(), target.getReplaceToVersion());
        assertEquals(BigInteger.ONE, target.getChildLinks().getTotal());
        assertEquals(RestInternalConstants.KIND_CATEGORIES, target.getChildLinks().getChildLinks().get(0).getKind());
        assertEquals(selfLink + "/categories", target.getChildLinks().getChildLinks().get(0).getHref());

        // Categories (SDMX type)
        assertEqualsCategoriesSdmxHierarchy(source.getItemsFirstLevel(), target.getCategories());  // IMPORTANT! first level, because categories are printed in hierarchy
    }

    public static void assertEqualsCategorySdmx(CategoryMetamac source, CategoryType target) {
        // Only test some metadata because SDMX metadata is tested in SDMX project
        // Test something...
        assertEquals(source.getNameableArtefact().getCode(), target.getId());
        assertEquals(source.getNameableArtefact().getUrn(), target.getUrn());
        // TODO category parent
        // assertEqualsNullability(source.getParent(), target.getParent());
        // if (source.getParent() != null) {
        // assertEquals(source.getParent().getNameableArtefact().getCode(), target.getParent().getRef().getId());
        // }
    }

    public static void assertEqualsCategory(CategoryMetamac source, Category target) {

        assertEquals(RestInternalConstants.KIND_CATEGORY, target.getKind());
        String parentLink = "http://data.istac.es/apis/srm/v1.0/categoryschemes" + "/" + source.getItemSchemeVersion().getMaintainableArtefact().getMaintainer().getIdAsMaintainer() + "/"
                + source.getItemSchemeVersion().getMaintainableArtefact().getCode() + "/" + source.getItemSchemeVersion().getMaintainableArtefact().getVersionLogic() + "/categories";
        String selfLink = parentLink + "/" + source.getNameableArtefact().getCode();
        assertEquals(RestInternalConstants.KIND_CATEGORY, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(RestInternalConstants.KIND_CATEGORIES, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertNull(target.getChildLinks());

        // Sdmx
        assertEqualsCategorySdmx(source, target);
    }

    public static void assertEqualsInternationalStringNotNull(org.siemac.metamac.core.common.ent.domain.InternationalString expecteds,
            org.siemac.metamac.rest.common.v1_0.domain.InternationalString actuals) {
        assertNotNull(expecteds);
        assertEqualsInternationalString(expecteds, actuals);
    }

    public static void assertEqualsInternationalString(org.siemac.metamac.core.common.ent.domain.InternationalString expecteds, org.siemac.metamac.rest.common.v1_0.domain.InternationalString actuals) {
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

    public static void assertEqualsUrnsNotNull(List<ConceptMetamac> expecteds, Urns actuals) {
        assertTrue(expecteds.size() > 0);
        assertEquals(expecteds.size(), actuals.getTotal().intValue());
        for (int i = 0; i < expecteds.size(); i++) {
            assertEquals(expecteds.get(i).getNameableArtefact().getUrn(), actuals.getUrns().get(i));
        }
    }

    private static List<ConditionalCriteria> buildFindConceptSchemesExpectedConditionalCriterias(String agencyID, String resourceID, String query, String orderBy) {
        List<ConditionalCriteria> expected = new ArrayList<ConditionalCriteria>();
        expected.addAll(buildFindConceptSchemesExpectedOrder(orderBy));
        expected.addAll(buildFindConceptSchemesExpectedQuery(query));
        expected.add(ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).distinctRoot().buildSingle());
        expected.add(ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().finalLogic()).eq(Boolean.TRUE)
                .buildSingle());
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
        expected.add(ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().finalLogic()).eq(Boolean.TRUE)
                .buildSingle());
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
        if (ORDER_BY_ID_DESC.equals(orderBy)) {
            return ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().code()).descending().build();
        }
        fail();
        return null;
    }

    private static List<ConditionalCriteria> buildFindConceptsExpectedOrderBy(String orderBy) {
        if (orderBy == null) {
            return ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).orderBy(ConceptMetamacProperties.nameableArtefact().code()).ascending().build();
        }
        if (ORDER_BY_ID_DESC.equals(orderBy)) {
            return ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).orderBy(ConceptMetamacProperties.nameableArtefact().code()).descending().build();
        }
        fail();
        return null;
    }

    private static List<ConditionalCriteria> buildFindConceptSchemesExpectedQuery(String query) {
        if (query == null) {
            return new ArrayList<ConditionalCriteria>();
        }
        if (QUERY_ID_LIKE_1.equals(query)) {
            return ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().code()).like("%1%").build();
        } else if (QUERY_ID_LIKE_1_NAME_LIKE_2.equals(query)) {
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
        if (QUERY_ID_LIKE_1_NAME_LIKE_2.equals(query)) {
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

    private static void assertEqualsResource(ItemSchemeVersion expected, String expectedKind, String expectedSelfLink, Resource actual) {
        assertEquals(expectedKind, actual.getKind());
        assertEquals(expected.getMaintainableArtefact().getCode(), actual.getId());
        assertEquals(expected.getMaintainableArtefact().getUrn(), actual.getUrn());
        assertEquals(expectedKind, actual.getSelfLink().getKind());
        assertEquals(expectedSelfLink, actual.getSelfLink().getHref());
        assertEqualsInternationalString(expected.getMaintainableArtefact().getName(), actual.getTitle());
    }

    private static void assertEqualsResource(Item expected, String expectedKind, String expectedSelfLink, Resource actual) {
        assertEquals(expectedKind, actual.getKind());
        assertEquals(expected.getNameableArtefact().getCode(), actual.getId());
        assertEquals(expected.getNameableArtefact().getUrn(), actual.getUrn());
        assertEquals(expectedKind, actual.getSelfLink().getKind());
        assertEquals(expectedSelfLink, actual.getSelfLink().getHref());
        assertEqualsInternationalString(expected.getNameableArtefact().getName(), actual.getTitle());
    }
    
    @SuppressWarnings("rawtypes")
    private static void assertEqualsCategoriesSdmxHierarchy(List expecteds, List<CategoryType> actuals) {
        assertEquals(expecteds.size(), actuals.size());
        for (int i = 0; i < expecteds.size(); i++) {
            CategoryType actual = actuals.get(i);
            assertTrue(actual instanceof CategoryType);
            assertFalse(actual instanceof Category);
            CategoryMetamac expected = (CategoryMetamac)expecteds.get(i);
            
            assertEqualsCategorySdmx(expected, actual);
            assertEqualsCategoriesSdmxHierarchy(expected.getChildren(), actual.getCategories());
        }
    }
}