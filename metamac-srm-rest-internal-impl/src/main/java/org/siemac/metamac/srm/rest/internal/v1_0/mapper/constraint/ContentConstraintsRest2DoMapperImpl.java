package org.siemac.metamac.srm.rest.internal.v1_0.mapper.constraint;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteria;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaBase;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaDisjunction;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ContentConstraintCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ContentConstraintCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.KeyParts;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Keys;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Regions;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.base.BaseRest2DoMapperV10Impl;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorisationProperties;
import com.arte.statistic.sdmx.srm.core.common.domain.ExternalItemProperties.ExternalItemProperty;
import com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint;
import com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraintProperties;
import com.arte.statistic.sdmx.srm.core.constraint.domain.KeyPart;
import com.arte.statistic.sdmx.srm.core.constraint.domain.KeyValue;
import com.arte.statistic.sdmx.srm.core.constraint.domain.RegionValue;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.ContentConstraintTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RegionValueTypeEnum;

@Component
public class ContentConstraintsRest2DoMapperImpl extends BaseRest2DoMapperV10Impl implements ContentConstraintsRest2DoMapper {

    private RestCriteria2SculptorCriteria<ContentConstraint> contentConstraintCriteriaMapper = null;

    public ContentConstraintsRest2DoMapperImpl() {
        super();
        contentConstraintCriteriaMapper = new RestCriteria2SculptorCriteria<ContentConstraint>(ContentConstraint.class, ContentConstraintCriteriaPropertyOrder.class,
                ContentConstraintCriteriaPropertyRestriction.class, new ContentConstraintCriteriaCallback());
    }

    @Override
    public RestCriteria2SculptorCriteria<ContentConstraint> getContentConstraintCriteriaMapper() {
        return contentConstraintCriteriaMapper;
    }

    private class ContentConstraintCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            ContentConstraintCriteriaPropertyRestriction propertyNameCriteria = ContentConstraintCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());

            switch (propertyNameCriteria) {
                case ID:
                    return buildSculptorPropertyCriteria(ContentConstraintProperties.maintainableArtefact().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, ContentConstraintProperties.maintainableArtefact());
                case NAME:
                    return buildSculptorPropertyCriteria(ContentConstraintProperties.maintainableArtefact().name().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case ARTEFACT_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForArtefactConstrainedUrnProperty(propertyRestriction, ContentConstraintProperties.constraintAttachment());
                case LATEST:
                    return buildSculptorPropertyCriteria(ContentConstraintProperties.maintainableArtefact().latestFinal(), PropertyTypeEnum.BOOLEAN, propertyRestriction);
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }
        }

        private SculptorPropertyCriteriaBase buildSculptorPropertyCriteriaDisjunctionForArtefactConstrainedUrnProperty(MetamacRestQueryPropertyRestriction propertyRestriction,
                ExternalItemProperty<ContentConstraint> constraintAttachment) {

            SculptorPropertyCriteria propertyCriteria1Urn = buildSculptorPropertyCriteria(constraintAttachment.urn(), PropertyTypeEnum.STRING, propertyRestriction);
            SculptorPropertyCriteria propertyCriteria2UrnProvider = buildSculptorPropertyCriteria(constraintAttachment.urnProvider(), PropertyTypeEnum.STRING, propertyRestriction);
            return new SculptorPropertyCriteriaDisjunction(propertyCriteria1Urn, propertyCriteria2UrnProvider);
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            ContentConstraintCriteriaPropertyOrder propertyNameCriteria = ContentConstraintCriteriaPropertyOrder.fromValue(order.getPropertyName());

            switch (propertyNameCriteria) {
                case ID:
                    return CategorisationProperties.maintainableArtefact().code();
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }

        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return ContentConstraintProperties.maintainableArtefact().code();
        }

    }

    @Override
    public ContentConstraint contentConstraintRestToEntity(ServiceContext ctx, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ContentConstraint source) throws MetamacException {
        if (source == null) {
            return null;
        }

        ContentConstraint target = new ContentConstraint();
        target.setMaintainableArtefact(new MaintainableArtefact());

        target.setType(ContentConstraintTypeEnum.valueOf(source.getType().name()));
        target.setConstraintAttachment(resourceInternalRestStatisticalResourceToExternalItemDo(source.getConstraintAttachment(), target.getConstraintAttachment()));
        target.setMaintainableArtefact(maintainableArtefactRestToEntity(source, target.getMaintainableArtefact()));

        // Regions
        Regions regionsSource = source.getRegions();
        if (regionsSource != null) {
            for (org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Region regionSource : regionsSource.getRegions()) {
                RegionValue regionValueEntity = contentConstraintRestToEntity(ctx, regionSource);
                if (regionValueEntity != null) {
                    target.addRegion(regionValueEntity);
                }
            }
        }

        return target;
    }

    private RegionValue contentConstraintRestToEntity(ServiceContext ctx, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Region source) throws MetamacException {
        if (source == null) {
            return null;
        }

        RegionValue target = new RegionValue();

        target.setRegionValueTypeEnum(RegionValueTypeEnum.valueOf(source.getRegionValueType().name()));
        target.setCode(source.getCode());

        // Keys
        Keys sourceKeys = source.getKeys();
        if (sourceKeys != null) {
            for (org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Key keySource : sourceKeys.getKeies()) {
                KeyValue keyRestEntity = keyRestToEntity(ctx, keySource);
                if (keyRestEntity != null) {
                    target.addKey(keyRestEntity);
                }
            }
        }

        return target;
    }

    private KeyValue keyRestToEntity(ServiceContext ctx, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Key source) throws MetamacException {
        if (source == null) {
            return null;
        }

        KeyValue target = new KeyValue();

        target.setIncluded(source.isIncluded());

        // Parts
        KeyParts keyParts = source.getKeyParts();
        if (keyParts != null) {
            for (org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.KeyPart keyPartSoruce : keyParts.getKeyParts()) {
                KeyPart keyPartEntity = keyPartToEntity(ctx, keyPartSoruce);
                if (keyPartEntity != null) {
                    target.addPart(keyPartEntity);
                }
            }
        }
        target.getParts();

        return target;

    }
    private KeyPart keyPartToEntity(ServiceContext ctx, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.KeyPart source) throws MetamacException {
        if (source == null) {
            return null;
        }

        KeyPart target = new KeyPart();

        target.setIdentifier(source.getIdentifier());
        target.setValue(source.getValue());
        target.setCascadeValues(source.isCascadeValues());
        target.setPosition(source.getPosition());

        target.setBeforePeriod(source.getBeforePeriod());
        target.setBeforePeriodInclusive(source.isBeforePeriodInclusive());

        target.setAfterPeriod(source.getAfterPeriod());
        target.setAfterPeriodInclusive(source.isAfterPeriodInclusive());

        target.setStartPeriod(source.getStartPeriod());
        target.setStartPeriodInclusive(source.isStartPeriodInclusive());

        target.setEndPeriod(source.getEndPeriod());
        target.setEndPeriodInclusive(source.isEndPeriodInclusive());

        return target;
    }
}