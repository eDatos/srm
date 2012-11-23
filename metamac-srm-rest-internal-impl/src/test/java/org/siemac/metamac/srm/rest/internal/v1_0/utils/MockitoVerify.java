package org.siemac.metamac.srm.rest.internal.v1_0.utils;

import static org.junit.Assert.fail;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.rest.common.test.utils.MetamacRestAsserts;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemProperties;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionProperties;

public class MockitoVerify extends MetamacRestAsserts {

    public static PagingParameter buildExpectedPagingParameter(String offset, String limit) {
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

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<ConditionalCriteria> buildExpectedConditionalCriteriaToFindItemSchemes(String agencyID, String resourceID, String query, String orderBy, Class entityClass) {
        List<ConditionalCriteria> expected = new ArrayList<ConditionalCriteria>();
        expected.addAll(buildFindItemSchemesExpectedOrder(orderBy, entityClass));
        expected.addAll(buildFindItemSchemesExpectedQuery(query, entityClass));
        expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).distinctRoot().buildSingle());
        expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(ItemSchemeVersionProperties.maintainableArtefact().finalLogic()).eq(Boolean.TRUE).buildSingle());
        if (agencyID != null && !RestInternalConstants.WILDCARD.equals(agencyID)) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(ItemSchemeVersionProperties.maintainableArtefact().maintainer().idAsMaintainer()).eq(agencyID).buildSingle());
        }
        if (resourceID != null && !RestInternalConstants.WILDCARD.equals(resourceID)) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(ItemSchemeVersionProperties.maintainableArtefact().code()).eq(resourceID).buildSingle());
        }
        return expected;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<ConditionalCriteria> buildExpectedConditionalCriteriaToFindItems(String agencyID, String resourceID, String version, String query, String orderBy, Class entityClass) {
        List<ConditionalCriteria> expected = new ArrayList<ConditionalCriteria>();
        expected.addAll(buildFindItemsExpectedOrderBy(orderBy, entityClass));
        expected.addAll(buildFindItemsExpectedQuery(query, entityClass));
        expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).distinctRoot().buildSingle());
        expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(ItemProperties.itemSchemeVersion().maintainableArtefact().finalLogic()).eq(Boolean.TRUE).buildSingle());
        if (agencyID != null && !RestInternalConstants.WILDCARD.equals(agencyID)) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(ItemProperties.itemSchemeVersion().maintainableArtefact().maintainer().idAsMaintainer())
                    .eq(agencyID).buildSingle());
        }
        if (resourceID != null && !RestInternalConstants.WILDCARD.equals(resourceID)) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(ItemProperties.itemSchemeVersion().maintainableArtefact().code()).eq(resourceID).buildSingle());
        }
        if (version != null && !RestInternalConstants.WILDCARD.equals(version)) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(ItemProperties.itemSchemeVersion().maintainableArtefact().versionLogic()).eq(version).buildSingle());
        }
        return expected;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static List<ConditionalCriteria> buildFindItemSchemesExpectedOrder(String orderBy, Class entityClass) {
        if (orderBy == null) {
            return ConditionalCriteriaBuilder.criteriaFor(entityClass).orderBy(ItemSchemeVersionProperties.maintainableArtefact().code()).ascending().build();
        }
        if (ORDER_BY_ID_DESC.equals(orderBy)) {
            return ConditionalCriteriaBuilder.criteriaFor(entityClass).orderBy(ItemSchemeVersionProperties.maintainableArtefact().code()).descending().build();
        }
        fail();
        return null;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static List<ConditionalCriteria> buildFindItemsExpectedOrderBy(String orderBy, Class entityClass) {
        if (orderBy == null) {
            return ConditionalCriteriaBuilder.criteriaFor(entityClass).orderBy(ItemProperties.nameableArtefact().code()).ascending().build();
        }
        if (ORDER_BY_ID_DESC.equals(orderBy)) {
            return ConditionalCriteriaBuilder.criteriaFor(entityClass).orderBy(ItemProperties.nameableArtefact().code()).descending().build();
        }
        fail();
        return null;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static List<ConditionalCriteria> buildFindItemSchemesExpectedQuery(String query, Class entityClass) {
        if (query == null) {
            return new ArrayList<ConditionalCriteria>();
        }
        if (QUERY_ID_LIKE_1.equals(query)) {
            return ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(ItemSchemeVersionProperties.maintainableArtefact().code()).like("%1%").build();
        } else if (QUERY_ID_LIKE_1_NAME_LIKE_2.equals(query)) {
            return ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(ItemSchemeVersionProperties.maintainableArtefact().code()).like("%1%")
                    .withProperty(ItemSchemeVersionProperties.maintainableArtefact().name().texts().label()).like("%2%").build();
        }
        fail();
        return null;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static List<ConditionalCriteria> buildFindItemsExpectedQuery(String query, Class entityClass) {
        if (query == null) {
            return new ArrayList<ConditionalCriteria>();
        }
        if (QUERY_ID_LIKE_1_NAME_LIKE_2.equals(query)) {
            return ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(ItemProperties.nameableArtefact().code()).like("%1%")
                    .withProperty(ItemProperties.nameableArtefact().name().texts().label()).like("%2%").build();
        }
        fail();
        return null;
    }
}