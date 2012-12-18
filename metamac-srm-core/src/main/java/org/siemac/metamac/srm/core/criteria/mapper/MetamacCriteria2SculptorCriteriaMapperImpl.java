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
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacProperties;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.criteria.CategoryMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.CategoryMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.CategorySchemeVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.CategorySchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.CodeMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.CodeMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.CodelistVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.CodelistVersionMetamacCriteriaPropertyEnum;
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

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefactProperties.MaintainableArtefactProperty;
import com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionType;

@Component("metamacCriteria2SculptorCriteriaMapperSrm")
public class MetamacCriteria2SculptorCriteriaMapperImpl implements MetamacCriteria2SculptorCriteriaMapper {

    private MetamacCriteria2SculptorCriteria<DataStructureDefinitionVersionMetamac> dataStructureMetamacCriteriaMapper      = null;
    private MetamacCriteria2SculptorCriteria<ConceptSchemeVersionMetamac>           conceptSchemeMetamacCriteriaMapper      = null;
    private MetamacCriteria2SculptorCriteria<ConceptMetamac>                        conceptMetamacCriteriaMapper            = null;
    private MetamacCriteria2SculptorCriteria<OrganisationSchemeVersionMetamac>      organisationSchemeMetamacCriteriaMapper = null;
    private MetamacCriteria2SculptorCriteria<OrganisationMetamac>                   organisationMetamacCriteriaMapper       = null;
    private MetamacCriteria2SculptorCriteria<CategorySchemeVersionMetamac>          categorySchemeMetamacCriteriaMapper     = null;
    private MetamacCriteria2SculptorCriteria<CategoryMetamac>                       categoryMetamacCriteriaMapper           = null;
    private MetamacCriteria2SculptorCriteria<CodelistVersionMetamac>                codelistMetamacCriteriaMapper           = null;
    private MetamacCriteria2SculptorCriteria<CodeMetamac>                           codeMetamacCriteriaMapper               = null;

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

        categorySchemeMetamacCriteriaMapper = new MetamacCriteria2SculptorCriteria<CategorySchemeVersionMetamac>(CategorySchemeVersionMetamac.class,
                CategorySchemeVersionMetamacCriteriaOrderEnum.class, CategorySchemeVersionMetamacCriteriaPropertyEnum.class, new CategorySchemeVersionMetamacCriteriaCallback());

        categoryMetamacCriteriaMapper = new MetamacCriteria2SculptorCriteria<CategoryMetamac>(CategoryMetamac.class, CategoryMetamacCriteriaOrderEnum.class, CategoryMetamacCriteriaPropertyEnum.class,
                new CategoryMetamacCriteriaCallback());

        codelistMetamacCriteriaMapper = new MetamacCriteria2SculptorCriteria<CodelistVersionMetamac>(CodelistVersionMetamac.class, CodelistVersionMetamacCriteriaOrderEnum.class,
                CodelistVersionMetamacCriteriaPropertyEnum.class, new CodelistVersionMetamacCriteriaCallback());

        codeMetamacCriteriaMapper = new MetamacCriteria2SculptorCriteria<CodeMetamac>(CodeMetamac.class, CodeMetamacCriteriaOrderEnum.class, CodeMetamacCriteriaPropertyEnum.class,
                new CodeMetamacCriteriaCallback());
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

    @Override
    public MetamacCriteria2SculptorCriteria<CategorySchemeVersionMetamac> getCategorySchemeMetamacCriteriaMapper() {
        return categorySchemeMetamacCriteriaMapper;
    }

    @Override
    public MetamacCriteria2SculptorCriteria<CategoryMetamac> getCategoryMetamacCriteriaMapper() {
        return categoryMetamacCriteriaMapper;
    }

    @Override
    public MetamacCriteria2SculptorCriteria<CodelistVersionMetamac> getCodelistMetamacCriteriaMapper() {
        return codelistMetamacCriteriaMapper;
    }

    @Override
    public MetamacCriteria2SculptorCriteria<CodeMetamac> getCodeMetamacCriteriaMapper() {
        return codeMetamacCriteriaMapper;
    }

    /**************************************************************************
     * CallBacks classes
     *************************************************************************/

    //
    // DATA STRUCTURE DEFINITIONS
    //

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
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().procStatus(), propertyRestriction.getEnumValue());
                case NAME:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getStringValue());
                case IS_LAST_VERSION:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().isLastVersion(), propertyRestriction.getBooleanValue());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public Property<DataStructureDefinitionVersionMetamac> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            DataStructureDefinitionVersionMetamacCriteriaOrderEnum propertyOrderEnum = DataStructureDefinitionVersionMetamacCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().code();
                case URN:
                    return DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().urn();
                case PROC_STATUS:
                    return DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().procStatus();
                case NAME:
                    return DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().name().texts().label();
                case LAST_UPDATED:
                    return getLastUpdatedLeafProperty(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact(), DataStructureDefinitionVersionMetamac.class);
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<DataStructureDefinitionVersionMetamac> retrievePropertyOrderDefault() throws MetamacException {
            return DataStructureDefinitionVersionMetamacProperties.id();
        }
    }

    //
    // CONCEPTS
    //

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
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().procStatus(), propertyRestriction.getEnumValue());
                case NAME:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getStringValue());
                case IS_LAST_VERSION:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().isLastVersion(), propertyRestriction.getBooleanValue());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public Property<ConceptSchemeVersionMetamac> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            ConceptSchemeVersionMetamacCriteriaOrderEnum propertyOrderEnum = ConceptSchemeVersionMetamacCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return ConceptSchemeVersionMetamacProperties.maintainableArtefact().code();
                case URN:
                    return ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn();
                case PROC_STATUS:
                    return ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().procStatus();
                case NAME:
                    return ConceptSchemeVersionMetamacProperties.maintainableArtefact().name().texts().label();
                case LAST_UPDATED:
                    return getLastUpdatedLeafProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact(), ConceptSchemeVersionMetamac.class);
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
                            ConceptSchemeVersionMetamacProperties.type().getName(), false, ConceptMetamac.class), propertyRestriction.getEnumValue());
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

    //
    // ORGANISATIONS
    //

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
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.lifeCycleMetadata().procStatus(), propertyRestriction.getEnumValue());
                case NAME:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getStringValue());
                case IS_LAST_VERSION:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().isLastVersion(), propertyRestriction.getBooleanValue());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public Property<OrganisationSchemeVersionMetamac> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            OrganisationSchemeVersionMetamacCriteriaOrderEnum propertyOrderEnum = OrganisationSchemeVersionMetamacCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return OrganisationSchemeVersionMetamacProperties.maintainableArtefact().code();
                case URN:
                    return OrganisationSchemeVersionMetamacProperties.maintainableArtefact().urn();
                case PROC_STATUS:
                    return OrganisationSchemeVersionMetamacProperties.lifeCycleMetadata().procStatus();
                case NAME:
                    return OrganisationSchemeVersionMetamacProperties.maintainableArtefact().name().texts().label();
                case LAST_UPDATED:
                    return getLastUpdatedLeafProperty(OrganisationSchemeVersionMetamacProperties.maintainableArtefact(), OrganisationSchemeVersionMetamac.class);
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
                            .organisationSchemeType().getName(), false, OrganisationMetamac.class), propertyRestriction.getEnumValue());
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

    //
    // CATEGORIES
    //

    private class CategorySchemeVersionMetamacCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            CategorySchemeVersionMetamacCriteriaPropertyEnum propertyEnum = CategorySchemeVersionMetamacCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyEnum) {
                case CODE:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().code(), propertyRestriction.getStringValue());
                case URN:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().urn(), propertyRestriction.getStringValue());
                case PROC_STATUS:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.lifeCycleMetadata().procStatus(), propertyRestriction.getEnumValue());
                case NAME:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getStringValue());
                case IS_LAST_VERSION:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().isLastVersion(), propertyRestriction.getBooleanValue());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public Property<CategorySchemeVersionMetamac> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            CategorySchemeVersionMetamacCriteriaOrderEnum propertyOrderEnum = CategorySchemeVersionMetamacCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return CategorySchemeVersionMetamacProperties.maintainableArtefact().code();
                case URN:
                    return CategorySchemeVersionMetamacProperties.maintainableArtefact().urn();
                case PROC_STATUS:
                    return CategorySchemeVersionMetamacProperties.lifeCycleMetadata().procStatus();
                case NAME:
                    return CategorySchemeVersionMetamacProperties.maintainableArtefact().name().texts().label();
                case LAST_UPDATED:
                    return getLastUpdatedLeafProperty(CategorySchemeVersionMetamacProperties.maintainableArtefact(), CategorySchemeVersionMetamac.class);
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<CategorySchemeVersionMetamac> retrievePropertyOrderDefault() throws MetamacException {
            return CategorySchemeVersionMetamacProperties.id();
        }
    }

    private class CategoryMetamacCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            CategoryMetamacCriteriaPropertyEnum propertyEnum = CategoryMetamacCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyEnum) {
                case CODE:
                    return new SculptorPropertyCriteria(CategoryMetamacProperties.nameableArtefact().code(), propertyRestriction.getStringValue());
                case URN:
                    return new SculptorPropertyCriteria(CategoryMetamacProperties.nameableArtefact().urn(), propertyRestriction.getStringValue());
                case NAME:
                    return new SculptorPropertyCriteria(CategoryMetamacProperties.nameableArtefact().name().texts().label(), propertyRestriction.getStringValue());
                case CATEGORY_PARENT_URN:
                    return new SculptorPropertyCriteria(CategoryMetamacProperties.parent().nameableArtefact().urn(), propertyRestriction.getStringValue());
                case CATEGORY_SCHEME_URN:
                    return new SculptorPropertyCriteria(CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact().urn(), propertyRestriction.getStringValue());
                case CATEGORY_SCHEME_IS_LAST_VERSION:
                    return new SculptorPropertyCriteria(CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact().isLastVersion(), propertyRestriction.getBooleanValue());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }
        @Override
        public Property<CategoryMetamac> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            CategoryMetamacCriteriaOrderEnum propertyOrderEnum = CategoryMetamacCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return CategoryMetamacProperties.nameableArtefact().code();
                case URN:
                    return CategoryMetamacProperties.nameableArtefact().urn();
                case NAME:
                    return CategoryMetamacProperties.nameableArtefact().name().texts().label();
                case CATEGORY_SCHEME_URN:
                    return CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact().urn();
                case CATEGORY_SCHEME_CODE:
                    return CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact().code();
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<CategoryMetamac> retrievePropertyOrderDefault() throws MetamacException {
            return CategoryMetamacProperties.id();
        }
    }

    //
    // CODES
    //

    private class CodelistVersionMetamacCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            CodelistVersionMetamacCriteriaPropertyEnum propertyEnum = CodelistVersionMetamacCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyEnum) {
                case CODE:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().code(), propertyRestriction.getStringValue());
                case URN:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().urn(), propertyRestriction.getStringValue());
                case PROC_STATUS:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.lifeCycleMetadata().procStatus(), propertyRestriction.getEnumValue());
                case NAME:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getStringValue());
                case IS_LAST_VERSION:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().isLastVersion(), propertyRestriction.getBooleanValue());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public Property<CodelistVersionMetamac> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            CodelistVersionMetamacCriteriaOrderEnum propertyOrderEnum = CodelistVersionMetamacCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return CodelistVersionMetamacProperties.maintainableArtefact().code();
                case URN:
                    return CodelistVersionMetamacProperties.maintainableArtefact().urn();
                case PROC_STATUS:
                    return CodelistVersionMetamacProperties.lifeCycleMetadata().procStatus();
                case NAME:
                    return CodelistVersionMetamacProperties.maintainableArtefact().name().texts().label();
                case LAST_UPDATED:
                    return getLastUpdatedLeafProperty(CodelistVersionMetamacProperties.maintainableArtefact(), CodelistVersionMetamac.class);
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<CodelistVersionMetamac> retrievePropertyOrderDefault() throws MetamacException {
            return CodelistVersionMetamacProperties.id();
        }
    }

    private class CodeMetamacCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            CodeMetamacCriteriaPropertyEnum propertyEnum = CodeMetamacCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyEnum) {
                case CODE:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.nameableArtefact().code(), propertyRestriction.getStringValue());
                case URN:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.nameableArtefact().urn(), propertyRestriction.getStringValue());
                case NAME:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.nameableArtefact().name().texts().label(), propertyRestriction.getStringValue());
                case CODE_PARENT_URN:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.parent().nameableArtefact().urn(), propertyRestriction.getStringValue());
                case CODELIST_URN:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.itemSchemeVersion().maintainableArtefact().urn(), propertyRestriction.getStringValue());
                case CODELIST_IS_LAST_VERSION:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.itemSchemeVersion().maintainableArtefact().isLastVersion(), propertyRestriction.getBooleanValue());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }
        @Override
        public Property<CodeMetamac> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            CodeMetamacCriteriaOrderEnum propertyOrderEnum = CodeMetamacCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return CodeMetamacProperties.nameableArtefact().code();
                case URN:
                    return CodeMetamacProperties.nameableArtefact().urn();
                case NAME:
                    return CodeMetamacProperties.nameableArtefact().name().texts().label();
                case CODELIST_URN:
                    return CodeMetamacProperties.itemSchemeVersion().maintainableArtefact().urn();
                case CODELIST_CODE:
                    return CodeMetamacProperties.itemSchemeVersion().maintainableArtefact().code();
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<CodeMetamac> retrievePropertyOrderDefault() throws MetamacException {
            return CodeMetamacProperties.id();
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private LeafProperty getLastUpdatedLeafProperty(MaintainableArtefactProperty maintainableArtefactProperty, Class entityClass) {
        return new LeafProperty(maintainableArtefactProperty.lastUpdated().getName(), CoreCommonConstants.CRITERIA_DATETIME_COLUMN_DATETIME, true, entityClass);
    }
}
