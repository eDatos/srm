package org.siemac.metamac.srm.core.criteria.mapper;

import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.core.common.constants.CoreCommonConstants;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.SculptorPropertyCriteria;
import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria;
import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.criteria.ConceptMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.ConceptMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.ConceptSchemeVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.ConceptSchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.DataStructureDefinitionVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.DataStructureDefinitionVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.OrganisationMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.OrganisationMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.OrganisationSchemeVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.OrganisationSchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacProperties;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacProperties;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacProperties;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionType;

@Component("metamacCriteria2SculptorCriteriaMapperSrm")
public class MetamacCriteria2SculptorCriteriaMapperImpl implements MetamacCriteria2SculptorCriteriaMapper {

    // @Autowired
    // @Qualifier("metamacCriteria2SculptorCriteriaMapperSdmxSrm")
    // private com.arte.statistic.sdmx.srm.core.mapper.metamaccriteria2sculptorcriteriamapper metamacCriteria2SculptorCriteriaMapperSdmxSrm;

    private MetamacCriteria2SculptorCriteria<DataStructureDefinitionVersionMetamac> dataStructureMetamacCriteriaMapper      = null;

    private MetamacCriteria2SculptorCriteria<ConceptSchemeVersionMetamac>           conceptSchemeMetamacCriteriaMapper      = null;

    private MetamacCriteria2SculptorCriteria<ConceptMetamac>                        conceptMetamacCriteriaMapper            = null;

    private MetamacCriteria2SculptorCriteria<OrganisationSchemeVersionMetamac>      organisationSchemeMetamacCriteriaMapper = null;

    private MetamacCriteria2SculptorCriteria<OrganisationMetamac>                   organisationMetamacCriteriaMapper       = null;

    /**************************************************************************
     * Constructor
     **************************************************************************/

    public MetamacCriteria2SculptorCriteriaMapperImpl() throws MetamacException {
        dataStructureMetamacCriteriaMapper = new MetamacCriteria2SculptorCriteria<DataStructureDefinitionVersionMetamac>(DataStructureDefinitionVersionMetamac.class,
                DataStructureDefinitionVersionMetamacCriteriaOrderEnum.class, DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.class,
                new DataStructureDefinitionVersionMetamacCriteriaCallback());

        conceptSchemeMetamacCriteriaMapper = new MetamacCriteria2SculptorCriteria<ConceptSchemeVersionMetamac>(ConceptSchemeVersionMetamac.class, ConceptSchemeVersionMetamacCriteriaOrderEnum.class,
                ConceptSchemeVersionMetamacCriteriaPropertyEnum.class, new ConceptSchemeVersionMetamacCriteriaCallback());

        conceptMetamacCriteriaMapper = new MetamacCriteria2SculptorCriteria<ConceptMetamac>(ConceptMetamac.class, ConceptMetamacCriteriaOrderEnum.class, ConceptMetamacCriteriaPropertyEnum.class,
                new ConceptMetamacCriteriaCallback());

        organisationSchemeMetamacCriteriaMapper = new MetamacCriteria2SculptorCriteria<OrganisationSchemeVersionMetamac>(OrganisationSchemeVersionMetamac.class,
                OrganisationSchemeVersionMetamacCriteriaOrderEnum.class, OrganisationSchemeVersionMetamacCriteriaPropertyEnum.class, new OrganisationSchemeVersionMetamacCriteriaCallback());

        organisationMetamacCriteriaMapper = new MetamacCriteria2SculptorCriteria<OrganisationMetamac>(OrganisationMetamac.class, OrganisationMetamacCriteriaOrderEnum.class,
                OrganisationMetamacCriteriaPropertyEnum.class, new OrganisationMetamacCriteriaCallback());
    }

    /**************************************************************************
     * Mappings
     **************************************************************************/

    @Override
    public MetamacCriteria2SculptorCriteria<DataStructureDefinitionVersionMetamac> getDataStructureDefinitionCriteriaMapper() {
        return dataStructureMetamacCriteriaMapper;
    }

    @Override
    public MetamacCriteria2SculptorCriteria<ConceptSchemeVersionMetamac> getConceptSchemeMetamacCriteriaMapper() {
        return conceptSchemeMetamacCriteriaMapper;
    }

    @Override
    public MetamacCriteria2SculptorCriteria<ConceptMetamac> getConceptMetamacCriteriaMapper() {
        return conceptMetamacCriteriaMapper;
    }

    @Override
    public MetamacCriteria2SculptorCriteria<OrganisationSchemeVersionMetamac> getOrganisationSchemeMetamacCriteriaMapper() {
        return organisationSchemeMetamacCriteriaMapper;
    }

    @Override
    public MetamacCriteria2SculptorCriteria<OrganisationMetamac> getOrganisationMetamacCriteriaMapper() {
        return organisationMetamacCriteriaMapper;
    }

    /**************************************************************************
     * CallBacks classes
     *************************************************************************/

    private class DataStructureDefinitionVersionMetamacCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            DataStructureDefinitionVersionMetamacCriteriaPropertyEnum propertyEnum = DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyEnum) {
                case CODE:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().code(), propertyRestriction.getStringValue());
                case URN:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().urn(), propertyRestriction.getStringValue());
                case PROC_STATUS:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.lifecycleMetadata().procStatus(), propertyRestriction.getEnumValue());
                case NAME:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getStringValue());
                case IS_LAST_VERSION:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().isLastVersion(), propertyRestriction.getBooleanValue());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @Override
        public Property<DataStructureDefinitionVersionMetamac> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            DataStructureDefinitionVersionMetamacCriteriaOrderEnum propertyOrderEnum = DataStructureDefinitionVersionMetamacCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().code();
                case URN:
                    return DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().urn();
                case PROC_STATUS:
                    return DataStructureDefinitionVersionMetamacProperties.lifecycleMetadata().procStatus();
                case NAME:
                    return DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().name().texts().label();
                case LAST_UPDATED:
                    return new LeafProperty<DataStructureDefinitionVersionMetamac>(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().lastUpdated().getName(),
                            CoreCommonConstants.CRITERIA_DATETIME_COLUMN_DATETIME, true, DataStructureDefinitionVersionMetamac.class);
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<DataStructureDefinitionVersionMetamac> retrievePropertyOrderDefault() throws MetamacException {
            return DataStructureDefinitionVersionMetamacProperties.id();
        }
    }

    private class ConceptSchemeVersionMetamacCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            ConceptSchemeVersionMetamacCriteriaPropertyEnum propertyEnum = ConceptSchemeVersionMetamacCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyEnum) {
                case CODE:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().code(), propertyRestriction.getStringValue());
                case URN:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn(), propertyRestriction.getStringValue());
                case PROC_STATUS:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.lifecycleMetadata().procStatus(), propertyRestriction.getEnumValue());
                case NAME:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getStringValue());
                case IS_LAST_VERSION:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().isLastVersion(), propertyRestriction.getBooleanValue());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @Override
        public Property<ConceptSchemeVersionMetamac> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            ConceptSchemeVersionMetamacCriteriaOrderEnum propertyOrderEnum = ConceptSchemeVersionMetamacCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return ConceptSchemeVersionMetamacProperties.maintainableArtefact().code();
                case URN:
                    return ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn();
                case PROC_STATUS:
                    return ConceptSchemeVersionMetamacProperties.lifecycleMetadata().procStatus();
                case NAME:
                    return ConceptSchemeVersionMetamacProperties.maintainableArtefact().name().texts().label();
                case LAST_UPDATED:
                    return new LeafProperty<ConceptSchemeVersionMetamac>(ConceptSchemeVersionMetamacProperties.maintainableArtefact().lastUpdated().getName(),
                            CoreCommonConstants.CRITERIA_DATETIME_COLUMN_DATETIME, true, ConceptSchemeVersionMetamac.class);
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<ConceptSchemeVersionMetamac> retrievePropertyOrderDefault() throws MetamacException {
            return ConceptSchemeVersionMetamacProperties.id();
        }
    }

    private class ConceptMetamacCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            ConceptMetamacCriteriaPropertyEnum propertyEnum = ConceptMetamacCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyEnum) {
                case CODE:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.nameableArtefact().code(), propertyRestriction.getStringValue());
                case URN:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.nameableArtefact().urn(), propertyRestriction.getStringValue());
                case NAME:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.nameableArtefact().name().texts().label(), propertyRestriction.getStringValue());
                case CONCEPT_PARENT_URN:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.parent().nameableArtefact().urn(), propertyRestriction.getStringValue());
                case CONCEPT_SCHEME_URN:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().urn(), propertyRestriction.getStringValue());
                case CONCEPT_SCHEME_TYPE:
                    return new SculptorPropertyCriteria(new LeafProperty<ConceptMetamac>(ConceptMetamacProperties.itemSchemeVersion().getName(),
                            ConceptSchemeVersionMetamacProperties.type().getName(), true, ConceptMetamac.class), propertyRestriction.getEnumValue());
                case CONCEPT_SCHEME_IS_LAST_VERSION:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().isLastVersion(), propertyRestriction.getBooleanValue());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }
        @Override
        public Property<ConceptMetamac> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            ConceptMetamacCriteriaOrderEnum propertyOrderEnum = ConceptMetamacCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return ConceptMetamacProperties.nameableArtefact().code();
                case URN:
                    return ConceptMetamacProperties.nameableArtefact().urn();
                case NAME:
                    return ConceptMetamacProperties.nameableArtefact().name().texts().label();
                case CONCEPT_SCHEME_URN:
                    return ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().urn();
                case CONCEPT_SCHEME_CODE:
                    return ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().code();
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<ConceptMetamac> retrievePropertyOrderDefault() throws MetamacException {
            return ConceptMetamacProperties.id();
        }
    }

    private class OrganisationSchemeVersionMetamacCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            OrganisationSchemeVersionMetamacCriteriaPropertyEnum propertyEnum = OrganisationSchemeVersionMetamacCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyEnum) {
                case CODE:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().code(), propertyRestriction.getStringValue());
                case URN:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().urn(), propertyRestriction.getStringValue());
                case TYPE:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.organisationSchemeType(), propertyRestriction.getEnumValue());
                case PROC_STATUS:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.lifecycleMetadata().procStatus(), propertyRestriction.getEnumValue());
                case NAME:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getStringValue());
                case IS_LAST_VERSION:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().isLastVersion(), propertyRestriction.getBooleanValue());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @Override
        public Property<OrganisationSchemeVersionMetamac> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            OrganisationSchemeVersionMetamacCriteriaOrderEnum propertyOrderEnum = OrganisationSchemeVersionMetamacCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return OrganisationSchemeVersionMetamacProperties.maintainableArtefact().code();
                case URN:
                    return OrganisationSchemeVersionMetamacProperties.maintainableArtefact().urn();
                case PROC_STATUS:
                    return OrganisationSchemeVersionMetamacProperties.lifecycleMetadata().procStatus();
                case NAME:
                    return OrganisationSchemeVersionMetamacProperties.maintainableArtefact().name().texts().label();
                case LAST_UPDATED:
                    return new LeafProperty<OrganisationSchemeVersionMetamac>(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().lastUpdated().getName(),
                            CoreCommonConstants.CRITERIA_DATETIME_COLUMN_DATETIME, true, OrganisationSchemeVersionMetamac.class);
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<OrganisationSchemeVersionMetamac> retrievePropertyOrderDefault() throws MetamacException {
            return OrganisationSchemeVersionMetamacProperties.id();
        }
    }

    private class OrganisationMetamacCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            OrganisationMetamacCriteriaPropertyEnum propertyEnum = OrganisationMetamacCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyEnum) {
                case CODE:
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.nameableArtefact().code(), propertyRestriction.getStringValue());
                case URN:
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.nameableArtefact().urn(), propertyRestriction.getStringValue());
                case NAME:
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.nameableArtefact().name().texts().label(), propertyRestriction.getStringValue());
                case ORGANISATION_PARENT_URN:
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.parent().nameableArtefact().urn(), propertyRestriction.getStringValue());
                case ORGANISATION_SCHEME_URN:
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.itemSchemeVersion().maintainableArtefact().urn(), propertyRestriction.getStringValue());
                case ORGANISATION_SCHEME_TYPE:
                    return new SculptorPropertyCriteria(new LeafProperty<OrganisationMetamac>(OrganisationMetamacProperties.itemSchemeVersion().getName(), OrganisationSchemeVersionMetamacProperties
                            .organisationSchemeType().getName(), true, OrganisationMetamac.class), propertyRestriction.getEnumValue());
                case ORGANISATION_SCHEME_IS_LAST_VERSION:
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.itemSchemeVersion().maintainableArtefact().isLastVersion(), propertyRestriction.getBooleanValue());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }
        @Override
        public Property<OrganisationMetamac> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            OrganisationMetamacCriteriaOrderEnum propertyOrderEnum = OrganisationMetamacCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return OrganisationMetamacProperties.nameableArtefact().code();
                case URN:
                    return OrganisationMetamacProperties.nameableArtefact().urn();
                case NAME:
                    return OrganisationMetamacProperties.nameableArtefact().name().texts().label();
                case ORGANISATION_SCHEME_URN:
                    return OrganisationMetamacProperties.itemSchemeVersion().maintainableArtefact().urn();
                case ORGANISATION_SCHEME_CODE:
                    return OrganisationMetamacProperties.itemSchemeVersion().maintainableArtefact().code();
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<OrganisationMetamac> retrievePropertyOrderDefault() throws MetamacException {
            return OrganisationMetamacProperties.id();
        }
    }

}
