package org.siemac.metamac.srm.rest.internal.v1_0.mapper.dsd;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.common.query.domain.OperationTypeEnum;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteria;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaBase;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaConjunction;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaDisjunction;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructureCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructureCriteriaPropertyRestriction;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacProperties;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.base.BaseRest2DoMapperV10Impl;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponent;

@Component
public class DataStructuresRest2DoMapperImpl extends BaseRest2DoMapperV10Impl implements DataStructuresRest2DoMapper {

    private RestCriteria2SculptorCriteria<DataStructureDefinitionVersionMetamac> conceptSchemeCriteriaMapper = null;

    public DataStructuresRest2DoMapperImpl() {
        conceptSchemeCriteriaMapper = new RestCriteria2SculptorCriteria<>(DataStructureDefinitionVersionMetamac.class, DataStructureCriteriaPropertyOrder.class,
                DataStructureCriteriaPropertyRestriction.class, new DataStructureDefinitionCriteriaCallback());
    }

    @Override
    public RestCriteria2SculptorCriteria<DataStructureDefinitionVersionMetamac> getDataStructureDefinitionCriteriaMapper() {
        return conceptSchemeCriteriaMapper;
    }

    private class DataStructureDefinitionCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) {
            DataStructureCriteriaPropertyRestriction propertyNameCriteria = DataStructureCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return buildSculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, DataStructureDefinitionVersionMetamacProperties.maintainableArtefact());
                case NAME:
                    return buildSculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().name().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case DESCRIPTION:
                    return buildSculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().description().texts().label(), PropertyTypeEnum.STRING,
                            propertyRestriction);
                case VALID_FROM:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().validFrom(),
                            DataStructureDefinitionVersionMetamac.class, false);
                case VALID_TO:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().validTo(),
                            DataStructureDefinitionVersionMetamac.class, false);
                case PROC_STATUS:
                    return buildSculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().procStatus(), PropertyTypeEnum.PROC_STATUS, propertyRestriction);
                case INTERNAL_PUBLICATION_DATE:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().internalPublicationDate(),
                            DataStructureDefinitionVersionMetamac.class, true);
                case INTERNAL_PUBLICATION_USER:
                    return buildSculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().internalPublicationUser(), PropertyTypeEnum.STRING, propertyRestriction);
                case EXTERNAL_PUBLICATION_DATE:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().externalPublicationDate(),
                            DataStructureDefinitionVersionMetamac.class, true);
                case EXTERNAL_PUBLICATION_USER:
                    return buildSculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().externalPublicationUser(), PropertyTypeEnum.STRING, propertyRestriction);
                case LATEST:
                    return buildSculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().latestFinal(), PropertyTypeEnum.BOOLEAN, propertyRestriction);
                case STATISTICAL_OPERATION_URN:
                    return buildSculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.statisticalOperation().urn(), PropertyTypeEnum.STRING, propertyRestriction);
                case DIMENSION_CONCEPT_URN: {
                    SculptorPropertyCriteriaDisjunction propertyCriteria1 = buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, DataStructureDefinitionVersionMetamacProperties
                            .grouping().components().cptIdRef().nameableArtefact());
                    SculptorPropertyCriteria propertyCriteria2 = new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.grouping().components().componentType(),
                            TypeComponent.DIMENSION_COMPONENT, OperationTypeEnum.EQ);
                    return new SculptorPropertyCriteriaConjunction(propertyCriteria1, propertyCriteria2);
                }
                case ATTRIBUTE_CONCEPT_URN: {
                    SculptorPropertyCriteriaDisjunction propertyCriteria1 = buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, DataStructureDefinitionVersionMetamacProperties
                            .grouping().components().cptIdRef().nameableArtefact());
                    SculptorPropertyCriteria propertyCriteria2 = new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.grouping().components().componentType(),
                            TypeComponent.DATA_ATTRIBUTE, OperationTypeEnum.EQ);
                    return new SculptorPropertyCriteriaConjunction(propertyCriteria1, propertyCriteria2);
                }
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) {
            DataStructureCriteriaPropertyOrder propertyNameCriteria = DataStructureCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().code();
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() {
            return DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().code();
        }
    }
}