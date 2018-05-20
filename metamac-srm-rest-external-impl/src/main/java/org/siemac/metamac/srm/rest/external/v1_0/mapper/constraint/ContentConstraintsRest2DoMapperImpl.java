package org.siemac.metamac.srm.rest.external.v1_0.mapper.constraint;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteria;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaBase;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaDisjunction;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ContentConstraintCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ContentConstraintCriteriaPropertyRestriction;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.base.BaseRest2DoMapperV10Impl;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.category.domain.CategorisationProperties;
import com.arte.statistic.sdmx.srm.core.common.domain.ExternalItemProperties.ExternalItemProperty;
import com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint;
import com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraintProperties;

@Component
public class ContentConstraintsRest2DoMapperImpl extends BaseRest2DoMapperV10Impl implements ContentConstraintsRest2DoMapper {

    private RestCriteria2SculptorCriteria<ContentConstraint> contentConstraintCriteriaMapper = null;

    public ContentConstraintsRest2DoMapperImpl() {
        super();
        contentConstraintCriteriaMapper = new RestCriteria2SculptorCriteria<>(ContentConstraint.class, ContentConstraintCriteriaPropertyOrder.class,
                ContentConstraintCriteriaPropertyRestriction.class, new ContentConstraintCriteriaCallback());
    }

    @Override
    public RestCriteria2SculptorCriteria<ContentConstraint> getContentConstraintCriteriaMapper() {
        return contentConstraintCriteriaMapper;
    }

    private class ContentConstraintCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) {
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
        public Property retrievePropertyOrder(MetamacRestOrder order) {
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
        public Property retrievePropertyOrderDefault() {
            return ContentConstraintProperties.maintainableArtefact().code();
        }

    }

}