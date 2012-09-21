package org.siemac.metamac.srm.core.mapper;

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
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacProperties;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionType;

@Component("metamacCriteria2SculptorCriteriaMapperSrm")
public class MetamacCriteria2SculptorCriteriaMapperImpl implements MetamacCriteria2SculptorCriteriaMapper {

    // @Autowired
    // @Qualifier("metamacCriteria2SculptorCriteriaMapperSdmxSrm")
    // private com.arte.statistic.sdmx.srm.core.mapper.MetamacCriteria2SculptorCriteriaMapper metamacCriteria2SculptorCriteriaMapperSdmxSrm;

    private MetamacCriteria2SculptorCriteria<DataStructureDefinitionVersionMetamac> dataStructureMetamacMapper = null;

    private MetamacCriteria2SculptorCriteria<ConceptSchemeVersionMetamac>           conceptSchemeMetamacMapper = null;

    private MetamacCriteria2SculptorCriteria<ConceptMetamac>                        conceptMetamacMapper       = null;

    /**************************************************************************
     * Constructor
     **************************************************************************/

    public MetamacCriteria2SculptorCriteriaMapperImpl() throws MetamacException {
        dataStructureMetamacMapper = new MetamacCriteria2SculptorCriteria<DataStructureDefinitionVersionMetamac>(DataStructureDefinitionVersionMetamac.class,
                DataStructureDefinitionVersionMetamacCriteriaOrderEnum.class, DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.class,
                new DataStructureDefinitionVersionMetamacCriteriaCallback());

        conceptSchemeMetamacMapper = new MetamacCriteria2SculptorCriteria<ConceptSchemeVersionMetamac>(ConceptSchemeVersionMetamac.class, ConceptSchemeVersionMetamacCriteriaOrderEnum.class,
                ConceptSchemeVersionMetamacCriteriaPropertyEnum.class, new ConceptSchemeVersionMetamacCriteriaCallback());

        conceptMetamacMapper = new MetamacCriteria2SculptorCriteria<ConceptMetamac>(ConceptMetamac.class, ConceptMetamacCriteriaOrderEnum.class, ConceptMetamacCriteriaPropertyEnum.class,
                new ConceptMetamacCriteriaCallback());
    }

    /**************************************************************************
     * Mappings
     **************************************************************************/

    @Override
    public MetamacCriteria2SculptorCriteria<DataStructureDefinitionVersionMetamac> getDataStructureDefinitionCriteriaMapper() {
        return dataStructureMetamacMapper;
    }

    @Override
    public MetamacCriteria2SculptorCriteria<ConceptSchemeVersionMetamac> getConceptSchemeMetamacCriteriaMapper() {
        return conceptSchemeMetamacMapper;
    }

    @Override
    public MetamacCriteria2SculptorCriteria<ConceptMetamac> getConceptMetamacCriteriaMapper() {
        return conceptMetamacMapper;
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
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.procStatus(), propertyRestriction.getEnumValue());
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
                    return DataStructureDefinitionVersionMetamacProperties.procStatus();
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
        public Property<ConceptSchemeVersionMetamac> retrievePropertyOrderDefault() throws MetamacException {
            return ConceptSchemeVersionMetamacProperties.id();
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
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.procStatus(), propertyRestriction.getEnumValue());
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
                    return ConceptSchemeVersionMetamacProperties.procStatus();
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
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<ConceptMetamac> retrievePropertyOrderDefault() throws MetamacException {
            return ConceptMetamacProperties.id();
        }
    }
}
