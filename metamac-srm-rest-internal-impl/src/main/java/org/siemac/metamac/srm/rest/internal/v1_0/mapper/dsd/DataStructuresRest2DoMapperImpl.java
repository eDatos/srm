package org.siemac.metamac.srm.rest.internal.v1_0.mapper.dsd;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.common.query.domain.SculptorPropertyCriteria;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructureCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructureCriteriaPropertyRestriction;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacProperties;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.base.BaseRest2DoMapperV10Impl;
import org.springframework.stereotype.Component;

@Component
public class DataStructuresRest2DoMapperImpl extends BaseRest2DoMapperV10Impl implements DataStructuresRest2DoMapper {

    private RestCriteria2SculptorCriteria<DataStructureDefinitionVersionMetamac> conceptSchemeCriteriaMapper = null;

    public DataStructuresRest2DoMapperImpl() {
        conceptSchemeCriteriaMapper = new RestCriteria2SculptorCriteria<DataStructureDefinitionVersionMetamac>(DataStructureDefinitionVersionMetamac.class, DataStructureCriteriaPropertyOrder.class,
                DataStructureCriteriaPropertyRestriction.class, new DataStructureDefinitionCriteriaCallback());
    }

    @Override
    public RestCriteria2SculptorCriteria<DataStructureDefinitionVersionMetamac> getDataStructureDefinitionCriteriaMapper() {
        return conceptSchemeCriteriaMapper;
    }

    private class DataStructureDefinitionCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            DataStructureCriteriaPropertyRestriction propertyNameCriteria = DataStructureCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().code(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().urnProvider(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().description().texts().label(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case VALID_FROM:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().validFrom(),
                            DataStructureDefinitionVersionMetamac.class);
                case VALID_TO:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().validTo(),
                            DataStructureDefinitionVersionMetamac.class);
                case PROC_STATUS:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().procStatus(),
                            propertyRestrictionValueToProcStatusEnum(propertyRestriction.getValue()), propertyRestriction.getOperationType());
                case INTERNAL_PUBLICATION_DATE:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().internalPublicationDate(),
                            DataStructureDefinitionVersionMetamac.class);
                case INTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().internalPublicationUser(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case EXTERNAL_PUBLICATION_DATE:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().externalPublicationDate(),
                            DataStructureDefinitionVersionMetamac.class);
                case EXTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().externalPublicationUser(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case LATEST:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().latestFinal(), Boolean.valueOf(propertyRestriction.getValue()),
                            propertyRestriction.getOperationType());
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            DataStructureCriteriaPropertyOrder propertyNameCriteria = DataStructureCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().code();
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().code();
        }
    }
}