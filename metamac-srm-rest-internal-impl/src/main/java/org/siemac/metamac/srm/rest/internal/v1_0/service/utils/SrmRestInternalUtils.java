package org.siemac.metamac.srm.rest.internal.v1_0.service.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemProperties;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionProperties;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefactProperties.MaintainableArtefactProperty;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefactProperties.NameableArtefactProperty;
import com.arte.statistic.sdmx.srm.core.constants.SdmxAlias;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationProperties;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationSchemeVersionProperties;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class SrmRestInternalUtils {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<ConditionalCriteria> buildConditionalCriteriaItemSchemes(String agencyID, String resourceID, String version, List<ConditionalCriteria> conditionalCriteriaQuery, Class entity)
            throws MetamacException {

        List<ConditionalCriteria> conditionalCriteria = new ArrayList<ConditionalCriteria>();
        if (CollectionUtils.isNotEmpty(conditionalCriteriaQuery)) {
            conditionalCriteria.addAll(conditionalCriteriaQuery); // adds distinct root and order
        } else {
            // init
            conditionalCriteria.addAll(ConditionalCriteriaBuilder.criteriaFor(entity).distinctRoot().build());
        }
        addConditionalCriteriaItemSchemePublished(conditionalCriteria, entity, ItemSchemeVersionProperties.maintainableArtefact());
        addConditionalCriteriaByAgency(conditionalCriteria, agencyID, entity, ItemSchemeVersionProperties.maintainableArtefact());
        addConditionalCriteriaByItemCodeSchemeCode(conditionalCriteria, resourceID, entity, ItemSchemeVersionProperties.maintainableArtefact());
        addConditionalCriteriaByItemCodeSchemeVersion(conditionalCriteria, version, entity, ItemSchemeVersionProperties.maintainableArtefact());

        return conditionalCriteria;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<ConditionalCriteria> buildConditionalCriteriaItems(String agencyID, String resourceID, String version, String itemID, List<ConditionalCriteria> conditionalCriteriaQuery,
            Class entity) throws MetamacException {

        List<ConditionalCriteria> conditionalCriteria = new ArrayList<ConditionalCriteria>();
        if (CollectionUtils.isNotEmpty(conditionalCriteriaQuery)) {
            conditionalCriteria.addAll(conditionalCriteriaQuery); // adds distinct root and order
        } else {
            // init
            conditionalCriteria.addAll(ConditionalCriteriaBuilder.criteriaFor(entity).distinctRoot().build());
        }
        addConditionalCriteriaItemSchemePublished(conditionalCriteria, entity, ItemProperties.itemSchemeVersion().maintainableArtefact());
        addConditionalCriteriaByAgency(conditionalCriteria, agencyID, entity, ItemProperties.itemSchemeVersion().maintainableArtefact());
        addConditionalCriteriaByItemCodeSchemeCode(conditionalCriteria, resourceID, entity, ItemProperties.itemSchemeVersion().maintainableArtefact());
        addConditionalCriteriaByItemCodeSchemeVersion(conditionalCriteria, version, entity, ItemProperties.itemSchemeVersion().maintainableArtefact());
        addConditionalCriteriaByItemCode(conditionalCriteria, itemID, entity, ItemProperties.nameableArtefact());

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

    public static Boolean uriMustBeSelfLink(MaintainableArtefact maintainableArtefact) {
        return !maintainableArtefact.getIsImported();
    }

    /**
     * Internally or externally published
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addConditionalCriteriaItemSchemePublished(List<ConditionalCriteria> conditionalCriteria, Class entity, MaintainableArtefactProperty maintainableArtefactProperty) {
        conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).withProperty(maintainableArtefactProperty.finalLogicClient()).eq(Boolean.TRUE).buildSingle());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addConditionalCriteriaByAgency(List<ConditionalCriteria> conditionalCriteria, String agencyID, Class entity, MaintainableArtefactProperty maintainableArtefactProperty) {
        if (SdmxAlias.SDMX_MAINTAINER.equals(agencyID)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).withProperty(maintainableArtefactProperty.maintainer()).isNull().buildSingle());
        } else if (agencyID != null && !RestInternalConstants.WILDCARD.equals(agencyID)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).withProperty(maintainableArtefactProperty.maintainer().idAsMaintainer()).eq(agencyID).buildSingle());
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addConditionalCriteriaByItemCodeSchemeCode(List<ConditionalCriteria> conditionalCriteria, String code, Class entity, MaintainableArtefactProperty maintainableArtefactProperty) {
        if (code != null && !RestInternalConstants.WILDCARD.equals(code)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).lbrace().withProperty(maintainableArtefactProperty.code()).eq(code).or()
                    .withProperty(maintainableArtefactProperty.codeFull()).eq(code).rbrace().buildSingle());
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addConditionalCriteriaByItemCodeSchemeVersion(List<ConditionalCriteria> conditionalCriteria, String version, Class entity,
            MaintainableArtefactProperty maintainableArtefactProperty) {
        if (RestInternalConstants.LATEST.equals(version)) {

            // AgencyScheme, DataProviderScheme and DataConsumerScheme never are versioned, so they are always with same version
            if (OrganisationSchemeVersionMetamac.class.equals(entity)) {
                conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).lbrace().withProperty(OrganisationSchemeVersionProperties.organisationSchemeType())
                        .in(OrganisationSchemeTypeEnum.AGENCY_SCHEME, OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME, OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME).or()
                        .withProperty(maintainableArtefactProperty.latestFinal()).eq(Boolean.TRUE).rbrace().buildSingle());
            } else if (OrganisationMetamac.class.equals(entity)) {
                conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).lbrace().withProperty(OrganisationProperties.organisationType())
                        .in(OrganisationTypeEnum.AGENCY, OrganisationTypeEnum.DATA_CONSUMER, OrganisationTypeEnum.DATA_PROVIDER).or().withProperty(maintainableArtefactProperty.latestFinal())
                        .eq(Boolean.TRUE).rbrace().buildSingle());
            } else {
                conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).withProperty(maintainableArtefactProperty.latestFinal()).eq(Boolean.TRUE).buildSingle());
            }
        } else if (version != null && !RestInternalConstants.WILDCARD.equals(version)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).withProperty(maintainableArtefactProperty.versionLogic()).eq(version).buildSingle());
        }
    }
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addConditionalCriteriaByItemCode(List<ConditionalCriteria> conditionalCriteria, String code, Class entity, NameableArtefactProperty nameableArtefactProperty) {
        if (code != null && !RestInternalConstants.WILDCARD.equals(code)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).lbrace().withProperty(nameableArtefactProperty.code()).eq(code).or()
                    .withProperty(nameableArtefactProperty.codeFull()).eq(code).rbrace().buildSingle());
        }
    }
}