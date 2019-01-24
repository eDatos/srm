package org.siemac.metamac.srm.rest.external.v1_0.service.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionProperties;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefactProperties.MaintainableArtefactProperty;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefactProperties.NameableArtefactProperty;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersionProperties;
import com.arte.statistic.sdmx.srm.core.constants.SdmxAlias;
import com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraintProperties;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationProperties;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationSchemeVersionProperties;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class SrmRestInternalUtils {

    private SrmRestInternalUtils() {
    }

    public static boolean hasField(String fields, String field) {
        return fields != null && fields.contains(field);
    }

    @SuppressWarnings({"rawtypes"})
    public static List<ConditionalCriteria> buildConditionalCriteriaItemSchemes(String agencyID, String resourceID, String version, List<ConditionalCriteria> conditionalCriteriaQuery, Class entity)
            {
        return buildConditionalCriteriaMaintainableArtefacts(agencyID, resourceID, version, conditionalCriteriaQuery, entity, ItemSchemeVersionProperties.maintainableArtefact());
    }

    @SuppressWarnings({"rawtypes"})
    public static List<ConditionalCriteria> buildConditionalCriteriaItems(String agencyID, String resourceID, String version, String itemID,
            MaintainableArtefactProperty itemSchemeVersionMaintainableArtefactProperty, NameableArtefactProperty itemNameableArtefactProperty, List<ConditionalCriteria> conditionalCriteriaQuery,
            Class entity) {
        return buildConditionalCriteriaNameableArtefacts(agencyID, resourceID, version, itemID, conditionalCriteriaQuery, entity, itemSchemeVersionMaintainableArtefactProperty,
                itemNameableArtefactProperty);
    }

    @SuppressWarnings({"rawtypes"})
    public static List<ConditionalCriteria> buildConditionalCriteriaStructures(String agencyID, String resourceID, String version, List<ConditionalCriteria> conditionalCriteriaQuery, Class entity)
            {
        return buildConditionalCriteriaMaintainableArtefacts(agencyID, resourceID, version, conditionalCriteriaQuery, entity, StructureVersionProperties.maintainableArtefact());
    }

    @SuppressWarnings({"rawtypes"})
    public static List<ConditionalCriteria> buildConditionalCriteriaContentConstraints(String agencyID, String resourceID, String version, List<ConditionalCriteria> conditionalCriteriaQuery,
            Class entity) {
        return buildConditionalCriteriaMaintainableArtefacts(agencyID, resourceID, version, conditionalCriteriaQuery, entity, ContentConstraintProperties.maintainableArtefact());
    }

    public static OrganisationSchemeTypeEnum toOrganisationSchemeType(OrganisationTypeEnum type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case AGENCY:
                return OrganisationSchemeTypeEnum.AGENCY_SCHEME;
            case ORGANISATION_UNIT:
                return OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME;
            case DATA_CONSUMER:
                return OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME;
            case DATA_PROVIDER:
                return OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME;
            default:
                throw new IllegalArgumentException("OrganisationTypeEnum unsuported: " + type);
        }
    }

    public static OrganisationTypeEnum toOrganisationType(OrganisationSchemeTypeEnum type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case AGENCY_SCHEME:
                return OrganisationTypeEnum.AGENCY;
            case ORGANISATION_UNIT_SCHEME:
                return OrganisationTypeEnum.ORGANISATION_UNIT;
            case DATA_CONSUMER_SCHEME:
                return OrganisationTypeEnum.DATA_CONSUMER;
            case DATA_PROVIDER_SCHEME:
                return OrganisationTypeEnum.DATA_PROVIDER;
            default:
                throw new IllegalArgumentException("OrganisationSchemeTypeEnum unsuported: " + type);
        }
    }

    public static Boolean uriMustBeSelfLink(MaintainableArtefact maintainableArtefact) {
        return !maintainableArtefact.getIsImported();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static List<ConditionalCriteria> buildConditionalCriteriaMaintainableArtefacts(String agencyID, String resourceID, String version, List<ConditionalCriteria> conditionalCriteriaQuery,
            Class entity, MaintainableArtefactProperty maintainableArtefactProperty) {

        List<ConditionalCriteria> conditionalCriteria = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(conditionalCriteriaQuery)) {
            conditionalCriteria.addAll(conditionalCriteriaQuery);
        } else {
            // init
            conditionalCriteria.addAll(ConditionalCriteriaBuilder.criteriaFor(entity).distinctRoot().build());
        }
        addConditionalCriteriaByPublished(conditionalCriteria, entity, maintainableArtefactProperty); // API external restricctions
        addConditionalCriteriaByMaintainableArtefactPublished(conditionalCriteria, entity, maintainableArtefactProperty);
        addConditionalCriteriaByMaintainableArtefactAgency(conditionalCriteria, agencyID, entity, maintainableArtefactProperty);
        addConditionalCriteriaByMaintainableArtefactCode(conditionalCriteria, resourceID, entity, maintainableArtefactProperty);
        addConditionalCriteriaByMaintainableArtefactVersion(conditionalCriteria, version, entity, maintainableArtefactProperty);

        return conditionalCriteria;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static List<ConditionalCriteria> buildConditionalCriteriaNameableArtefacts(String agencyID, String resourceID, String version, String itemID,
            List<ConditionalCriteria> conditionalCriteriaQuery, Class entity, MaintainableArtefactProperty maintainableArtefactProperty, NameableArtefactProperty nameableArtefactProperty)
            {

        List<ConditionalCriteria> conditionalCriteria = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(conditionalCriteriaQuery)) {
            conditionalCriteria.addAll(conditionalCriteriaQuery);
        } else {
            // init
            conditionalCriteria.addAll(ConditionalCriteriaBuilder.criteriaFor(entity).distinctRoot().build());
        }
        addConditionalCriteriaByPublished(conditionalCriteria, entity, maintainableArtefactProperty); // API external restricctions
        addConditionalCriteriaByMaintainableArtefactPublished(conditionalCriteria, entity, maintainableArtefactProperty);
        addConditionalCriteriaByMaintainableArtefactAgency(conditionalCriteria, agencyID, entity, maintainableArtefactProperty);
        addConditionalCriteriaByMaintainableArtefactCode(conditionalCriteria, resourceID, entity, maintainableArtefactProperty);
        addConditionalCriteriaByMaintainableArtefactVersion(conditionalCriteria, version, entity, maintainableArtefactProperty);
        addConditionalCriteriaByItemCode(conditionalCriteria, itemID, entity, nameableArtefactProperty);

        return conditionalCriteria;
    }

    /**
     * Internally or externally published
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addConditionalCriteriaByMaintainableArtefactPublished(List<ConditionalCriteria> conditionalCriteria, Class entity, MaintainableArtefactProperty maintainableArtefactProperty) {
        conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).withProperty(maintainableArtefactProperty.finalLogicClient()).eq(Boolean.TRUE).buildSingle());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addConditionalCriteriaByMaintainableArtefactAgency(List<ConditionalCriteria> conditionalCriteria, String agencyID, Class entity,
            MaintainableArtefactProperty maintainableArtefactProperty) {
        if (SdmxAlias.SDMX_MAINTAINER.equals(agencyID)) {
            conditionalCriteria.addAll(ConditionalCriteriaBuilder.criteriaFor(entity).lbrace().withProperty(maintainableArtefactProperty.maintainer().idAsMaintainer()).eq(agencyID).or()
                    .withProperty(maintainableArtefactProperty.maintainer()).isNull().rbrace().build());
        } else if (agencyID != null && !SrmRestConstants.WILDCARD_ALL.equals(agencyID)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).withProperty(maintainableArtefactProperty.maintainer().idAsMaintainer()).eq(agencyID).buildSingle());
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addConditionalCriteriaByMaintainableArtefactCode(List<ConditionalCriteria> conditionalCriteria, String code, Class entity,
            MaintainableArtefactProperty maintainableArtefactProperty) {
        if (code != null && !SrmRestConstants.WILDCARD_ALL.equals(code)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).lbrace().withProperty(maintainableArtefactProperty.code()).eq(code).or()
                    .withProperty(maintainableArtefactProperty.codeFull()).eq(code).rbrace().buildSingle());
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addConditionalCriteriaByMaintainableArtefactVersion(List<ConditionalCriteria> conditionalCriteria, String version, Class entity,
            MaintainableArtefactProperty maintainableArtefactProperty) {
        if (SrmRestConstants.WILDCARD_LATEST.equals(version)) {

            // AgencyScheme, DataProviderScheme and DataConsumerScheme never are versioned, so they are always with same version
            if (OrganisationSchemeVersionMetamac.class.equals(entity)) {
                conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).lbrace().withProperty(OrganisationSchemeVersionProperties.organisationSchemeType())
                        .in(OrganisationSchemeTypeEnum.AGENCY_SCHEME, OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME, OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME).or()
                        .withProperty(maintainableArtefactProperty.latestPublic()).eq(Boolean.TRUE).rbrace().buildSingle());
            } else if (OrganisationMetamac.class.equals(entity)) {
                conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).lbrace().withProperty(OrganisationProperties.organisationType())
                        .in(OrganisationTypeEnum.AGENCY, OrganisationTypeEnum.DATA_CONSUMER, OrganisationTypeEnum.DATA_PROVIDER).or().withProperty(maintainableArtefactProperty.latestPublic())
                        .eq(Boolean.TRUE).rbrace().buildSingle());
            } else {
                conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).withProperty(maintainableArtefactProperty.latestPublic()).eq(Boolean.TRUE).buildSingle());
            }
        } else if (version != null && !SrmRestConstants.WILDCARD_ALL.equals(version)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).withProperty(maintainableArtefactProperty.versionLogic()).eq(version).buildSingle());
        }
    }
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addConditionalCriteriaByItemCode(List<ConditionalCriteria> conditionalCriteria, String code, Class entity, NameableArtefactProperty nameableArtefactProperty) {
        if (code != null && !SrmRestConstants.WILDCARD_ALL.equals(code)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).lbrace().withProperty(nameableArtefactProperty.code()).eq(code).or()
                    .withProperty(nameableArtefactProperty.codeFull()).eq(code).rbrace().buildSingle());
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addConditionalCriteriaByPublished(List<ConditionalCriteria> conditionalCriteria, Class entity, MaintainableArtefactProperty maintainableArtefactProperty) {
        conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).withProperty(maintainableArtefactProperty.publicLogic()).eq(Boolean.TRUE).buildSingle());
    }

    private static final String SPACE = " ";
    private static final String PLUS  = "+";

    public static Set<String> parseFieldsParameter(String fieldsParam) {
        Set<String> showFields = new HashSet<>();
        if (fieldsParam != null) {
            Set<String> validFields = new HashSet<>();
            validFields.add(SrmRestConstants.FIELD_INCLUDE_OPENNES);
            validFields.add(SrmRestConstants.FIELD_INCLUDE_ORDER);
            validFields.add(SrmRestConstants.FIELD_INCLUDE_VARIABLE_ELEMENT);
            validFields.add(SrmRestConstants.FIELD_INCLUDE_DESCRIPTION);
            List<String> fieldList = Arrays.asList(fieldsParam.split(","));
            List<String> parsedFieldList = new ArrayList<>();
            for (String field : fieldList) {
                if (field.startsWith(SPACE)) {
                    parsedFieldList.add(field.replaceFirst(SPACE, PLUS));
                } else {
                    parsedFieldList.add(field);
                }
            }
            for (String value : validFields) {
                if (parsedFieldList.contains(value)) {
                    showFields.add(value);
                }
            }
        }
        return showFields;
    }

}