package org.siemac.metamac.srm.rest.internal.v1_0.service.utils;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemProperties;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionProperties;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefactProperties.MaintainableArtefactProperty;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefactProperties.NameableArtefactProperty;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class SrmRestInternalUtils {

    // TODO latest
    @SuppressWarnings("rawtypes")
    public static List<ConditionalCriteria> buildConditionalCriteriaItemSchemes(String agencyID, String resourceID, String version, List<ConditionalCriteria> conditionalCriteriaQuery, Class entity)
            throws MetamacException {

        List<ConditionalCriteria> conditionalCriteria = new ArrayList<ConditionalCriteria>();
        if (conditionalCriteriaQuery != null) {
            conditionalCriteria.addAll(conditionalCriteriaQuery);
        }
        addConditionalCriteriaItemSchemePublished(conditionalCriteria, entity, ItemSchemeVersionProperties.maintainableArtefact());
        addConditionalCriteriaByAgency(conditionalCriteria, agencyID, entity, ItemSchemeVersionProperties.maintainableArtefact());
        addConditionalCriteriaByItemSchemeCode(conditionalCriteria, resourceID, entity, ItemSchemeVersionProperties.maintainableArtefact());
        addConditionalCriteriaByItemSchemeVersion(conditionalCriteria, version, entity, ItemSchemeVersionProperties.maintainableArtefact());

        return conditionalCriteria;
    }

    // TODO latest
    @SuppressWarnings("rawtypes")
    public static List<ConditionalCriteria> buildConditionalCriteriaItems(String agencyID, String resourceID, String version, String conceptID, List<ConditionalCriteria> conditionalCriteriaQuery,
            Class entity) throws MetamacException {

        List<ConditionalCriteria> conditionalCriteria = new ArrayList<ConditionalCriteria>();
        if (conditionalCriteriaQuery != null) {
            conditionalCriteria.addAll(conditionalCriteriaQuery);
        }
        addConditionalCriteriaItemSchemePublished(conditionalCriteria, entity, ItemProperties.itemSchemeVersion().maintainableArtefact());
        addConditionalCriteriaByAgency(conditionalCriteria, agencyID, entity, ItemProperties.itemSchemeVersion().maintainableArtefact());
        addConditionalCriteriaByItemSchemeCode(conditionalCriteria, resourceID, entity, ItemProperties.itemSchemeVersion().maintainableArtefact());
        addConditionalCriteriaByItemSchemeVersion(conditionalCriteria, version, entity, ItemProperties.itemSchemeVersion().maintainableArtefact());
        addConditionalCriteriaByItem(conditionalCriteria, conceptID, entity, ItemProperties.nameableArtefact());

        return conditionalCriteria;
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

    /**
     * Internally or externally published
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addConditionalCriteriaItemSchemePublished(List<ConditionalCriteria> conditionalCriteria, Class entity, MaintainableArtefactProperty maintainableArtefactProperty) {
        conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).withProperty(maintainableArtefactProperty.finalLogic()).eq(Boolean.TRUE).buildSingle());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addConditionalCriteriaByAgency(List<ConditionalCriteria> conditionalCriteria, String agencyID, Class entity, MaintainableArtefactProperty maintainableArtefactProperty) {
        if (agencyID != null && !RestInternalConstants.WILDCARD.equals(agencyID)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).withProperty(maintainableArtefactProperty.maintainer().idAsMaintainer()).eq(agencyID).buildSingle());
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addConditionalCriteriaByItemSchemeCode(List<ConditionalCriteria> conditionalCriteria, String code, Class entity, MaintainableArtefactProperty maintainableArtefactProperty) {
        if (code != null && !RestInternalConstants.WILDCARD.equals(code)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).withProperty(maintainableArtefactProperty.code()).eq(code).buildSingle());
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addConditionalCriteriaByItemSchemeVersion(List<ConditionalCriteria> conditionalCriteria, String version, Class entity, MaintainableArtefactProperty maintainableArtefactProperty) {
        if (version != null && !RestInternalConstants.WILDCARD.equals(version)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).withProperty(maintainableArtefactProperty.versionLogic()).eq(version).buildSingle());
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addConditionalCriteriaByItem(List<ConditionalCriteria> conditionalCriteria, String code, Class entity, NameableArtefactProperty nameableArtefactProperty) {
        if (code != null && !RestInternalConstants.WILDCARD.equals(code)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).withProperty(nameableArtefactProperty.code()).eq(code).buildSingle());
        }
    }
}