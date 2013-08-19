package org.siemac.metamac.srm.rest.internal.v1_0.utils;

import static org.junit.Assert.fail;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_LATEST;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;

import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefactProperties.IdentifiableArtefactProperty;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionProperties;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefactProperties.MaintainableArtefactProperty;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefactProperties.NameableArtefactProperty;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersionProperties;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationProperties;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationSchemeVersionProperties;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class MockitoVerify {

    public static enum RestOperationEnum {
        RETRIEVE, FIND
    }

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

    public static PagingParameter buildExpectedPagingParameterRetrieveOne() {
        return PagingParameter.pageAccess(1, 1);
    }

    @SuppressWarnings({"rawtypes"})
    public static List<ConditionalCriteria> buildExpectedConditionalCriteriaToFindStructures(String agencyID, String resourceID, String version, String query, String orderBy, Class entityClass,
            RestOperationEnum restOperation) {
        return buildExpectedConditionalCriteriaToFindMaintainableArtefacts(agencyID, resourceID, version, query, orderBy, entityClass, StructureVersionProperties.maintainableArtefact(), restOperation);
    }

    @SuppressWarnings({"rawtypes"})
    public static List<ConditionalCriteria> buildExpectedConditionalCriteriaToFindItemSchemes(String agencyID, String resourceID, String version, String query, String orderBy, Class entityClass,
            RestOperationEnum restOperation) {
        return buildExpectedConditionalCriteriaToFindMaintainableArtefacts(agencyID, resourceID, version, query, orderBy, entityClass, ItemSchemeVersionProperties.maintainableArtefact(),
                restOperation);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<ConditionalCriteria> buildExpectedConditionalCriteriaToFindItems(String agencyID, String resourceID, String version, String itemID, String query, String orderBy,
            Class entityClass, MaintainableArtefactProperty itemSchemeVersionMaintainableArtefactProperty, NameableArtefactProperty itemNameableArtefactProperty, RestOperationEnum restOperation) {
        List<ConditionalCriteria> expected = new ArrayList<ConditionalCriteria>();
        if (RestOperationEnum.FIND.equals(restOperation)) {
            expected.addAll(buildFindItemsExpectedOrderBy(orderBy, entityClass, itemNameableArtefactProperty));
            expected.addAll(buildFindItemsExpectedQuery(query, entityClass, itemNameableArtefactProperty));
        }
        expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).distinctRoot().buildSingle());
        expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(itemSchemeVersionMaintainableArtefactProperty.finalLogicClient()).eq(Boolean.TRUE).buildSingle());
        if (agencyID != null && !RestInternalConstants.WILDCARD_ALL.equals(agencyID)) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(itemSchemeVersionMaintainableArtefactProperty.maintainer().idAsMaintainer()).eq(agencyID).buildSingle());
        }
        if (resourceID != null && !RestInternalConstants.WILDCARD_ALL.equals(resourceID)) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).lbrace().withProperty(itemSchemeVersionMaintainableArtefactProperty.code()).eq(resourceID).or()
                    .withProperty(itemSchemeVersionMaintainableArtefactProperty.codeFull()).eq(resourceID).rbrace().buildSingle());
        }
        if (RestInternalConstants.WILDCARD_LATEST.equals(version)) {
            if (OrganisationMetamac.class.equals(entityClass)) {
                expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).lbrace().withProperty(OrganisationProperties.organisationType())
                        .in(OrganisationTypeEnum.AGENCY, OrganisationTypeEnum.DATA_CONSUMER, OrganisationTypeEnum.DATA_PROVIDER).or()
                        .withProperty(ItemSchemeVersionProperties.maintainableArtefact().latestFinal()).eq(Boolean.TRUE).rbrace().buildSingle());
            } else {
                expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(itemSchemeVersionMaintainableArtefactProperty.latestFinal()).eq(Boolean.TRUE).buildSingle());
            }
        } else if (version != null && !RestInternalConstants.WILDCARD_ALL.equals(version)) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(itemSchemeVersionMaintainableArtefactProperty.versionLogic()).eq(version).buildSingle());
        }
        if (itemID != null) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).lbrace().withProperty(itemNameableArtefactProperty.code()).eq(itemID).or()
                    .withProperty(itemNameableArtefactProperty.codeFull()).eq(itemID).rbrace().buildSingle());
        }
        return expected;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<ConditionalCriteria> buildExpectedConditionalCriteriaToFindMaintainableArtefacts(String agencyID, String resourceID, String version, String query, String orderBy,
            Class entityClass, MaintainableArtefactProperty maintainableArtefactProperty, RestOperationEnum restOperation) {
        List<ConditionalCriteria> expected = new ArrayList<ConditionalCriteria>();
        if (RestOperationEnum.FIND.equals(restOperation)) {
            expected.addAll(buildFindItemSchemesExpectedOrder(orderBy, entityClass));
            expected.addAll(buildFindItemSchemesExpectedQuery(query, entityClass));
        }
        expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).distinctRoot().buildSingle());
        expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(maintainableArtefactProperty.finalLogicClient()).eq(Boolean.TRUE).buildSingle());
        if (agencyID != null && !RestInternalConstants.WILDCARD_ALL.equals(agencyID)) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(maintainableArtefactProperty.maintainer().idAsMaintainer()).eq(agencyID).buildSingle());
        }
        if (resourceID != null && !RestInternalConstants.WILDCARD_ALL.equals(resourceID)) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).lbrace().withProperty(maintainableArtefactProperty.code()).eq(resourceID).or()
                    .withProperty(maintainableArtefactProperty.codeFull()).eq(resourceID).rbrace().buildSingle());
        }
        if (RestInternalConstants.WILDCARD_LATEST.equals(version)) {
            // AgencyScheme, DataProviderScheme and DataConsumerScheme never are versioned, so they are always with same version
            if (OrganisationSchemeVersionMetamac.class.equals(entityClass)) {
                expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).lbrace().withProperty(OrganisationSchemeVersionProperties.organisationSchemeType())
                        .in(OrganisationSchemeTypeEnum.AGENCY_SCHEME, OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME, OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME).or()
                        .withProperty(maintainableArtefactProperty.latestFinal()).eq(Boolean.TRUE).rbrace().buildSingle());
            } else {
                expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(maintainableArtefactProperty.latestFinal()).eq(Boolean.TRUE).buildSingle());
            }
        } else if (version != null && !RestInternalConstants.WILDCARD_ALL.equals(version)) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(maintainableArtefactProperty.versionLogic()).eq(version).buildSingle());
        }
        return expected;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<ConditionalCriteria> buildFindExpectedOrder(String orderBy, Class entityClass, NameableArtefactProperty nameableArtefactProperty) {
        if (orderBy == null) {
            return ConditionalCriteriaBuilder.criteriaFor(entityClass).orderBy(nameableArtefactProperty.code()).ascending().build();
        }
        if (ORDER_BY_ID_DESC.equals(orderBy)) {
            return ConditionalCriteriaBuilder.criteriaFor(entityClass).orderBy(nameableArtefactProperty.code()).descending().build();
        }
        fail();
        return null;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<ConditionalCriteria> buildFindExpectedOrder(String orderBy, Class entityClass, IdentifiableArtefactProperty identifiableArtefactProperty) {
        if (orderBy == null) {
            return ConditionalCriteriaBuilder.criteriaFor(entityClass).orderBy(identifiableArtefactProperty.code()).ascending().build();
        }
        if (ORDER_BY_ID_DESC.equals(orderBy)) {
            return ConditionalCriteriaBuilder.criteriaFor(entityClass).orderBy(identifiableArtefactProperty.code()).descending().build();
        }
        fail();
        return null;
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
    private static List<ConditionalCriteria> buildFindItemsExpectedOrderBy(String orderBy, Class entityClass, NameableArtefactProperty itemNameableArtefactProperty) {
        if (orderBy == null) {
            if (entityClass.equals(CategoryMetamac.class)) {
                return ConditionalCriteriaBuilder.criteriaFor(entityClass).orderBy(itemNameableArtefactProperty.codeFull()).ascending().build();
            } else {
                return ConditionalCriteriaBuilder.criteriaFor(entityClass).orderBy(itemNameableArtefactProperty.code()).ascending().build();
            }
        }
        if (ORDER_BY_ID_DESC.equals(orderBy)) {
            if (entityClass.equals(CategoryMetamac.class)) {
                return ConditionalCriteriaBuilder.criteriaFor(entityClass).orderBy(itemNameableArtefactProperty.codeFull()).descending().build();
            } else {
                return ConditionalCriteriaBuilder.criteriaFor(entityClass).orderBy(itemNameableArtefactProperty.code()).descending().build();
            }
        }
        fail();
        return null;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<ConditionalCriteria> buildFindExpectedQuery(String query, Class entityClass, NameableArtefactProperty nameableArtefactProperty) {
        if (query == null) {
            return new ArrayList<ConditionalCriteria>();
        }
        if (QUERY_ID_LIKE_1.equals(query)) {
            return ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(nameableArtefactProperty.code()).like("%1%").build();
        } else if (QUERY_ID_LIKE_1_NAME_LIKE_2.equals(query)) {
            return ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(nameableArtefactProperty.code()).like("%1%").withProperty(nameableArtefactProperty.name().texts().label())
                    .like("%2%").build();
        }
        fail();
        return null;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<ConditionalCriteria> buildFindExpectedQuery(String query, Class entityClass, IdentifiableArtefactProperty identifiableArtefactProperty) {
        if (query == null) {
            return new ArrayList<ConditionalCriteria>();
        }
        if (QUERY_ID_LIKE_1.equals(query)) {
            return ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(identifiableArtefactProperty.code()).like("%1%").build();
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
        } else if (QUERY_LATEST.equals(query)) {
            return ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(ItemSchemeVersionProperties.maintainableArtefact().latestFinal()).eq(Boolean.TRUE).build();
        }
        fail();
        return null;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static List<ConditionalCriteria> buildFindItemsExpectedQuery(String query, Class entityClass, NameableArtefactProperty itemNameableArtefactProperty) {
        if (query == null) {
            return new ArrayList<ConditionalCriteria>();
        }
        if (QUERY_ID_LIKE_1_NAME_LIKE_2.equals(query)) {
            if (entityClass.equals(CategoryMetamac.class)) {
                return ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(itemNameableArtefactProperty.codeFull()).like("%1%")
                        .withProperty(itemNameableArtefactProperty.name().texts().label()).like("%2%").build();
            } else {
                return ConditionalCriteriaBuilder.criteriaFor(entityClass).withProperty(itemNameableArtefactProperty.code()).like("%1%")
                        .withProperty(itemNameableArtefactProperty.name().texts().label()).like("%2%").build();
            }
        }
        fail();
        return null;
    }
}