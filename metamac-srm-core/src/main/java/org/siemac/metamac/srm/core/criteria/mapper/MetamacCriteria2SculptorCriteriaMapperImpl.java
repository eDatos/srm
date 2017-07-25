package org.siemac.metamac.srm.core.criteria.mapper;

import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.SculptorCriteriaConjunction;
import org.siemac.metamac.core.common.criteria.SculptorCriteriaDisjunction;
import org.siemac.metamac.core.common.criteria.SculptorPropertyCriteria;
import org.siemac.metamac.core.common.criteria.SculptorPropertyCriteriaBase;
import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria;
import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.utils.CriteriaUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacProperties;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableElementProperties;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.domain.VariableFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.VariableProperties;
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
import org.siemac.metamac.srm.core.criteria.CodelistFamilyCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.CodelistFamilyCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.CodelistVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.CodelistVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.ConceptMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.ConceptMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.ConceptSchemeVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.ConceptSchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.DataStructureDefinitionVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.DataStructureDefinitionVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.OrganisationContactCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.OrganisationContactCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.OrganisationMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.OrganisationMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.OrganisationSchemeVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.OrganisationSchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.VariableCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.VariableCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.VariableElementCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.VariableElementCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.VariableFamilyCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.VariableFamilyCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacProperties;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacProperties;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacProperties;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefactProperties.IdentifiableArtefactProperty;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeProperties.ItemSchemeProperty;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefactProperties.MaintainableArtefactProperty;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefactProperties.NameableArtefactProperty;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureProperties.StructureProperty;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionType;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Contact;
import com.arte.statistic.sdmx.srm.core.organisation.domain.ContactProperties;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationSchemeVersion;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponent;

@Component("metamacCriteria2SculptorCriteriaMapperSrm")
public class MetamacCriteria2SculptorCriteriaMapperImpl implements MetamacCriteria2SculptorCriteriaMapper {

    private MetamacCriteria2SculptorCriteria<DataStructureDefinitionVersionMetamac> dataStructureMetamacCriteriaMapper      = null;
    private MetamacCriteria2SculptorCriteria<ConceptSchemeVersionMetamac>           conceptSchemeMetamacCriteriaMapper      = null;
    private MetamacCriteria2SculptorCriteria<ConceptMetamac>                        conceptMetamacCriteriaMapper            = null;
    private MetamacCriteria2SculptorCriteria<OrganisationSchemeVersionMetamac>      organisationSchemeMetamacCriteriaMapper = null;
    private MetamacCriteria2SculptorCriteria<OrganisationMetamac>                   organisationMetamacCriteriaMapper       = null;
    private MetamacCriteria2SculptorCriteria<Contact>                               organisationContactCriteriaMapper       = null;
    private MetamacCriteria2SculptorCriteria<CategorySchemeVersionMetamac>          categorySchemeMetamacCriteriaMapper     = null;
    private MetamacCriteria2SculptorCriteria<CategoryMetamac>                       categoryMetamacCriteriaMapper           = null;
    private MetamacCriteria2SculptorCriteria<CodelistVersionMetamac>                codelistMetamacCriteriaMapper           = null;
    private MetamacCriteria2SculptorCriteria<CodeMetamac>                           codeMetamacCriteriaMapper               = null;
    private MetamacCriteria2SculptorCriteria<CodelistFamily>                        codelistFamilyCriteriaMapper            = null;
    private MetamacCriteria2SculptorCriteria<VariableFamily>                        variableFamilyCriteriaMapper            = null;
    private MetamacCriteria2SculptorCriteria<Variable>                              variableCriteriaMapper                  = null;
    private MetamacCriteria2SculptorCriteria<VariableElement>                       variableElementCriteriaMapper           = null;

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

        organisationContactCriteriaMapper = new MetamacCriteria2SculptorCriteria<Contact>(Contact.class, OrganisationContactCriteriaOrderEnum.class, OrganisationContactCriteriaPropertyEnum.class,
                new OrganisationContactCriteriaCallback());

        categorySchemeMetamacCriteriaMapper = new MetamacCriteria2SculptorCriteria<CategorySchemeVersionMetamac>(CategorySchemeVersionMetamac.class,
                CategorySchemeVersionMetamacCriteriaOrderEnum.class, CategorySchemeVersionMetamacCriteriaPropertyEnum.class, new CategorySchemeVersionMetamacCriteriaCallback());

        categoryMetamacCriteriaMapper = new MetamacCriteria2SculptorCriteria<CategoryMetamac>(CategoryMetamac.class, CategoryMetamacCriteriaOrderEnum.class, CategoryMetamacCriteriaPropertyEnum.class,
                new CategoryMetamacCriteriaCallback());

        codelistMetamacCriteriaMapper = new MetamacCriteria2SculptorCriteria<CodelistVersionMetamac>(CodelistVersionMetamac.class, CodelistVersionMetamacCriteriaOrderEnum.class,
                CodelistVersionMetamacCriteriaPropertyEnum.class, new CodelistVersionMetamacCriteriaCallback());

        codeMetamacCriteriaMapper = new MetamacCriteria2SculptorCriteria<CodeMetamac>(CodeMetamac.class, CodeMetamacCriteriaOrderEnum.class, CodeMetamacCriteriaPropertyEnum.class,
                new CodeMetamacCriteriaCallback());

        codelistFamilyCriteriaMapper = new MetamacCriteria2SculptorCriteria<CodelistFamily>(CodelistFamily.class, CodelistFamilyCriteriaOrderEnum.class, CodelistFamilyCriteriaPropertyEnum.class,
                new CodelistFamilyCriteriaCallback());

        variableFamilyCriteriaMapper = new MetamacCriteria2SculptorCriteria<VariableFamily>(VariableFamily.class, VariableFamilyCriteriaOrderEnum.class, VariableFamilyCriteriaPropertyEnum.class,
                new VariableFamilyCriteriaCallback());

        variableCriteriaMapper = new MetamacCriteria2SculptorCriteria<Variable>(Variable.class, VariableCriteriaOrderEnum.class, VariableCriteriaPropertyEnum.class, new VariableCriteriaCallback());

        variableElementCriteriaMapper = new MetamacCriteria2SculptorCriteria<VariableElement>(VariableElement.class, VariableElementCriteriaOrderEnum.class, VariableElementCriteriaPropertyEnum.class,
                new VariableElementCriteriaCallback());

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
    public MetamacCriteria2SculptorCriteria<Contact> getOrganisationContactCriteriaMapper() {
        return organisationContactCriteriaMapper;
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

    @Override
    public MetamacCriteria2SculptorCriteria<CodelistFamily> getCodelistFamilyCriteriaMapper() {
        return codelistFamilyCriteriaMapper;
    }

    @Override
    public MetamacCriteria2SculptorCriteria<VariableFamily> getVariableFamilyCriteriaMapper() {
        return variableFamilyCriteriaMapper;
    }

    @Override
    public MetamacCriteria2SculptorCriteria<Variable> getVariableCriteriaMapper() {
        return variableCriteriaMapper;
    }

    @Override
    public MetamacCriteria2SculptorCriteria<VariableElement> getVariableElementCriteriaMapper() {
        return variableElementCriteriaMapper;
    }

    /**************************************************************************
     * CallBacks classes
     *************************************************************************/

    //
    // DATA STRUCTURE DEFINITIONS
    //

    private class DataStructureDefinitionVersionMetamacCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            DataStructureDefinitionVersionMetamacCriteriaPropertyEnum propertyEnum = DataStructureDefinitionVersionMetamacCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyEnum) {
                case CODE:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().code(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case PROC_STATUS:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().procStatus(), propertyRestriction.getEnumValue(),
                            propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().description().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case MAINTAINER_URN:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().maintainer().nameableArtefact().urn(),
                            propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case STATISTICAL_OPERATION_URN:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.statisticalOperation().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case IS_LAST_VERSION:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().isLastVersion(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case LATEST_FINAL:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().latestFinal(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case LATEST_PUBLIC:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.maintainableArtefact().latestPublic(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case INTERNAL_PUBLICATION_DATE:
                    return new SculptorPropertyCriteria(getDatetimeLeafPropertyEmbedded(DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().internalPublicationDate(),
                            DataStructureDefinitionVersion.class), propertyRestriction.getDateValue(), propertyRestriction.getOperationType());
                case INTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().internalPublicationUser(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case EXTERNAL_PUBLICATION_DATE:
                    return new SculptorPropertyCriteria(getDatetimeLeafPropertyEmbedded(DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().externalPublicationDate(),
                            DataStructureDefinitionVersion.class), propertyRestriction.getDateValue(), propertyRestriction.getOperationType());
                case EXTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().externalPublicationUser(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case DIMENSION_CONCEPT_URN: {
                    SculptorPropertyCriteria propertyCriteria1 = new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.grouping().components().cptIdRef().nameableArtefact()
                            .urn(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                    SculptorPropertyCriteria propertyCriteria2 = new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.grouping().components().componentType(),
                            TypeComponent.DIMENSION_COMPONENT, OperationType.EQ);
                    return new SculptorCriteriaConjunction(propertyCriteria1, propertyCriteria2);
                }
                case ATTRIBUTE_CONCEPT_URN: {
                    SculptorPropertyCriteria propertyCriteria1 = new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.grouping().components().cptIdRef().nameableArtefact()
                            .urn(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                    SculptorPropertyCriteria propertyCriteria2 = new SculptorPropertyCriteria(DataStructureDefinitionVersionMetamacProperties.grouping().components().componentType(),
                            TypeComponent.DATA_ATTRIBUTE, OperationType.EQ);
                    return new SculptorCriteriaConjunction(propertyCriteria1, propertyCriteria2);
                }
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
                case RESOURCE_CREATED_DATE:
                    return getResourceCreatedDateLeafProperty(DataStructureDefinitionVersionMetamacProperties.structure(), DataStructureDefinitionVersionMetamac.class);
                case RESOURCE_LAST_UPDATED:
                    return getResourceLastUpdatedLeafProperty(DataStructureDefinitionVersionMetamacProperties.structure(), DataStructureDefinitionVersionMetamac.class);
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
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().code(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case PROC_STATUS:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().procStatus(), propertyRestriction.getEnumValue(),
                            propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().description().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case MAINTAINER_URN:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().maintainer().nameableArtefact().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case CONCEPT_SCHEME_TYPE:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.type(), propertyRestriction.getEnumValue(), propertyRestriction.getOperationType());
                case STATISTICAL_OPERATION_URN:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.relatedOperation().urn(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case IS_LAST_VERSION:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().isLastVersion(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case LATEST_FINAL:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().latestFinal(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case LATEST_PUBLIC:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().latestPublic(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case INTERNAL_PUBLICATION_DATE:
                    return new SculptorPropertyCriteria(
                            getDatetimeLeafPropertyEmbedded(ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().internalPublicationDate(), ConceptSchemeVersion.class),
                            propertyRestriction.getDateValue(), propertyRestriction.getOperationType());
                case INTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().internalPublicationUser(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case EXTERNAL_PUBLICATION_DATE:
                    return new SculptorPropertyCriteria(
                            getDatetimeLeafPropertyEmbedded(ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().externalPublicationDate(), ConceptSchemeVersion.class),
                            propertyRestriction.getDateValue(), propertyRestriction.getOperationType());
                case EXTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().externalPublicationUser(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
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
                case RESOURCE_CREATED_DATE:
                    return getResourceCreatedDateLeafProperty(ConceptSchemeVersionMetamacProperties.itemScheme(), ConceptSchemeVersionMetamac.class);
                case RESOURCE_LAST_UPDATED:
                    return getResourceLastUpdatedLeafProperty(ConceptSchemeVersionMetamacProperties.itemScheme(), ConceptSchemeVersionMetamac.class);
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
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.nameableArtefact().code(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.nameableArtefact().urn(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.nameableArtefact().name().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.nameableArtefact().description().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case DESCRIPTION_SOURCE:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.descriptionSource().texts().label(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case ACRONYM:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.acronym().texts().label(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case CONCEPT_PARENT_URN:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.parent().nameableArtefact().urn(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case CONCEPT_SCHEME_URN:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case CONCEPT_SCHEME_TYPE:
                    return new SculptorPropertyCriteria(new LeafProperty<ConceptMetamac>(ConceptMetamacProperties.itemSchemeVersion().getName(),
                            ConceptSchemeVersionMetamacProperties.type().getName(), false, ConceptMetamac.class), propertyRestriction.getEnumValue(), propertyRestriction.getOperationType());
                case CONCEPT_SCHEME_IS_LAST_VERSION:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().isLastVersion(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case CONCEPT_SCHEME_LATEST_FINAL:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().latestFinal(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case CONCEPT_SCHEME_LATEST_PUBLIC:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().latestPublic(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }
        @SuppressWarnings("unchecked")
        @Override
        public Property<ConceptMetamac> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            ConceptMetamacCriteriaOrderEnum propertyOrderEnum = ConceptMetamacCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return ConceptMetamacProperties.nameableArtefact().code();
                case URN:
                    return ConceptMetamacProperties.nameableArtefact().urn();
                case CONCEPT_SCHEME_URN:
                    return ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().urn();
                case CONCEPT_SCHEME_CODE:
                    return ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().code();
                case LAST_UPDATED:
                    return getLastUpdatedLeafProperty(ConceptMetamacProperties.nameableArtefact(), ConceptMetamac.class);
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
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().code(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case TYPE:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.organisationSchemeType(), propertyRestriction.getEnumValue(), propertyRestriction.getOperationType());
                case PROC_STATUS:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.lifeCycleMetadata().procStatus(), propertyRestriction.getEnumValue(),
                            propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().description().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case MAINTAINER_URN:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().maintainer().nameableArtefact().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case IS_LAST_VERSION:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().isLastVersion(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case LATEST_FINAL:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().latestFinal(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case LATEST_PUBLIC:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.maintainableArtefact().latestPublic(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case INTERNAL_PUBLICATION_DATE:
                    return new SculptorPropertyCriteria(getDatetimeLeafPropertyEmbedded(OrganisationSchemeVersionMetamacProperties.lifeCycleMetadata().internalPublicationDate(),
                            OrganisationSchemeVersion.class), propertyRestriction.getDateValue(), propertyRestriction.getOperationType());
                case INTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.lifeCycleMetadata().internalPublicationUser(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case EXTERNAL_PUBLICATION_DATE:
                    return new SculptorPropertyCriteria(getDatetimeLeafPropertyEmbedded(OrganisationSchemeVersionMetamacProperties.lifeCycleMetadata().externalPublicationDate(),
                            OrganisationSchemeVersion.class), propertyRestriction.getDateValue(), propertyRestriction.getOperationType());
                case EXTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(OrganisationSchemeVersionMetamacProperties.lifeCycleMetadata().externalPublicationUser(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
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
                case RESOURCE_CREATED_DATE:
                    return getResourceCreatedDateLeafProperty(OrganisationSchemeVersionMetamacProperties.itemScheme(), OrganisationSchemeVersionMetamac.class);
                case RESOURCE_LAST_UPDATED:
                    return getResourceLastUpdatedLeafProperty(OrganisationSchemeVersionMetamacProperties.itemScheme(), OrganisationSchemeVersionMetamac.class);
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
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.nameableArtefact().code(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.nameableArtefact().urn(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.nameableArtefact().name().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.nameableArtefact().description().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case TYPE:
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.organisationType(), propertyRestriction.getEnumValue(), propertyRestriction.getOperationType());
                case ORGANISATION_PARENT_URN:
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.parent().nameableArtefact().urn(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case ORGANISATION_SCHEME_URN:
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.itemSchemeVersion().maintainableArtefact().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case ORGANISATION_SCHEME_TYPE:
                    return new SculptorPropertyCriteria(new LeafProperty<OrganisationMetamac>(OrganisationMetamacProperties.itemSchemeVersion().getName(), OrganisationSchemeVersionMetamacProperties
                            .organisationSchemeType().getName(), false, OrganisationMetamac.class), propertyRestriction.getEnumValue(), propertyRestriction.getOperationType());
                case ORGANISATION_SCHEME_IS_LAST_VERSION:
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.itemSchemeVersion().maintainableArtefact().isLastVersion(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case ORGANISATION_SCHEME_LATEST_FINAL:
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.itemSchemeVersion().maintainableArtefact().latestFinal(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case ORGANISATION_SCHEME_LATEST_PUBLIC:
                    return new SculptorPropertyCriteria(OrganisationMetamacProperties.itemSchemeVersion().maintainableArtefact().latestPublic(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }
        @SuppressWarnings("unchecked")
        @Override
        public Property<OrganisationMetamac> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            OrganisationMetamacCriteriaOrderEnum propertyOrderEnum = OrganisationMetamacCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return OrganisationMetamacProperties.nameableArtefact().code();
                case URN:
                    return OrganisationMetamacProperties.nameableArtefact().urn();
                case ORGANISATION_SCHEME_URN:
                    return OrganisationMetamacProperties.itemSchemeVersion().maintainableArtefact().urn();
                case ORGANISATION_SCHEME_CODE:
                    return OrganisationMetamacProperties.itemSchemeVersion().maintainableArtefact().code();
                case LAST_UPDATED:
                    return getLastUpdatedLeafProperty(OrganisationMetamacProperties.nameableArtefact(), OrganisationMetamac.class);
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<OrganisationMetamac> retrievePropertyOrderDefault() throws MetamacException {
            return OrganisationMetamacProperties.id();
        }
    }

    private class OrganisationContactCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            OrganisationContactCriteriaPropertyEnum propertyEnum = OrganisationContactCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyEnum) {
                case ORGANISATION_URN:
                    return new SculptorPropertyCriteria(ContactProperties.organisation().nameableArtefact().urn(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(ContactProperties.name().texts().label(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case ORGANISATION_UNIT:
                    return new SculptorPropertyCriteria(ContactProperties.organisationUnit().texts().label(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case RESPONSIBILITY:
                    return new SculptorPropertyCriteria(ContactProperties.responsibility().texts().label(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @Override
        public Property<Contact> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            OrganisationContactCriteriaOrderEnum propertyOrderEnum = OrganisationContactCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return ContactProperties.code();
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<Contact> retrievePropertyOrderDefault() throws MetamacException {
            return ContactProperties.id();
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
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().code(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case PROC_STATUS:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.lifeCycleMetadata().procStatus(), propertyRestriction.getEnumValue(),
                            propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().description().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case MAINTAINER_URN:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().maintainer().nameableArtefact().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case IS_LAST_VERSION:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().isLastVersion(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case LATEST_FINAL:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().latestFinal(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case LATEST_PUBLIC:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().latestPublic(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case INTERNAL_PUBLICATION_DATE:
                    return new SculptorPropertyCriteria(getDatetimeLeafPropertyEmbedded(CategorySchemeVersionMetamacProperties.lifeCycleMetadata().internalPublicationDate(),
                            CategorySchemeVersion.class), propertyRestriction.getDateValue(), propertyRestriction.getOperationType());
                case INTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.lifeCycleMetadata().internalPublicationUser(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case EXTERNAL_PUBLICATION_DATE:
                    return new SculptorPropertyCriteria(getDatetimeLeafPropertyEmbedded(CategorySchemeVersionMetamacProperties.lifeCycleMetadata().externalPublicationDate(),
                            CategorySchemeVersion.class), propertyRestriction.getDateValue(), propertyRestriction.getOperationType());
                case EXTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.lifeCycleMetadata().externalPublicationUser(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
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
                case RESOURCE_CREATED_DATE:
                    return getResourceCreatedDateLeafProperty(CategorySchemeVersionMetamacProperties.itemScheme(), CategorySchemeVersionMetamac.class);
                case RESOURCE_LAST_UPDATED:
                    return getResourceLastUpdatedLeafProperty(CategorySchemeVersionMetamacProperties.itemScheme(), CategorySchemeVersionMetamac.class);
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
                    return new SculptorPropertyCriteria(CategoryMetamacProperties.nameableArtefact().code(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(CategoryMetamacProperties.nameableArtefact().urn(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(CategoryMetamacProperties.nameableArtefact().name().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(CategoryMetamacProperties.nameableArtefact().description().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case CATEGORY_PARENT_URN:
                    return new SculptorPropertyCriteria(CategoryMetamacProperties.parent().nameableArtefact().urn(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case CATEGORY_SCHEME_URN:
                    return new SculptorPropertyCriteria(CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case CATEGORY_SCHEME_IS_LAST_VERSION:
                    return new SculptorPropertyCriteria(CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact().isLastVersion(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case CATEGORY_SCHEME_LATEST_FINAL:
                    return new SculptorPropertyCriteria(CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact().latestFinal(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case CATEGORY_SCHEME_LATEST_PUBLIC:
                    return new SculptorPropertyCriteria(CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact().latestPublic(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case CATEGORY_SCHEME_EXTERNALLY_PUBLISHED:
                    return new SculptorPropertyCriteria(CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact().publicLogic(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }
        @SuppressWarnings("unchecked")
        @Override
        public Property<CategoryMetamac> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            CategoryMetamacCriteriaOrderEnum propertyOrderEnum = CategoryMetamacCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return CategoryMetamacProperties.nameableArtefact().code();
                case URN:
                    return CategoryMetamacProperties.nameableArtefact().urn();
                case CATEGORY_SCHEME_URN:
                    return CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact().urn();
                case CATEGORY_SCHEME_CODE:
                    return CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact().code();
                case LAST_UPDATED:
                    return getLastUpdatedLeafProperty(CategoryMetamacProperties.nameableArtefact(), CategoryMetamac.class);
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
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().code(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().urn(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case PROC_STATUS:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.lifeCycleMetadata().procStatus(), propertyRestriction.getEnumValue(), propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case DESCRIPTION_SOURCE:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.descriptionSource().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().description().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case MAINTAINER_URN:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().maintainer().nameableArtefact().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case ACCESS_TYPE:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.accessType(), propertyRestriction.getEnumValue(), propertyRestriction.getOperationType());
                case IS_LAST_VERSION:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().isLastVersion(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case LATEST_FINAL:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().latestFinal(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case LATEST_PUBLIC:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().latestPublic(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case CODELIST_FAMILY_URN:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.family().nameableArtefact().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case INTERNAL_PUBLICATION_DATE:
                    return new SculptorPropertyCriteria(getDatetimeLeafPropertyEmbedded(CodelistVersionMetamacProperties.lifeCycleMetadata().internalPublicationDate(), CodelistVersion.class),
                            propertyRestriction.getDateValue(), propertyRestriction.getOperationType());
                case INTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.lifeCycleMetadata().internalPublicationUser(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case EXTERNAL_PUBLICATION_DATE:
                    return new SculptorPropertyCriteria(getDatetimeLeafPropertyEmbedded(CodelistVersionMetamacProperties.lifeCycleMetadata().externalPublicationDate(), CodelistVersion.class),
                            propertyRestriction.getDateValue(), propertyRestriction.getOperationType());
                case EXTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.lifeCycleMetadata().externalPublicationUser(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case VARIABLE:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.variable().nameableArtefact().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case VARIABLE_ELEMENT:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.variable().variableElements().identifiableArtefact().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
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
                case RESOURCE_CREATED_DATE:
                    return getResourceCreatedDateLeafProperty(CodelistVersionMetamacProperties.itemScheme(), CodelistVersionMetamac.class);
                case RESOURCE_LAST_UPDATED:
                    return getResourceLastUpdatedLeafProperty(CodelistVersionMetamacProperties.itemScheme(), CodelistVersionMetamac.class);
                case LAST_UPDATED:
                    return getLastUpdatedLeafProperty(CodelistVersionMetamacProperties.maintainableArtefact(), CodelistVersionMetamac.class);
                case CODELIST_FAMILY_URN:
                    return CodelistVersionMetamacProperties.family().nameableArtefact().urn();
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
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            CodeMetamacCriteriaPropertyEnum propertyEnum = CodeMetamacCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyEnum) {
                case CODE:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.nameableArtefact().code(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.nameableArtefact().urn(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.nameableArtefact().name().texts().label(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.nameableArtefact().description().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case SHORT_NAME: {
                    SculptorPropertyCriteria propertyCriteria1 = new SculptorPropertyCriteria(CodeMetamacProperties.shortName().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                    SculptorPropertyCriteria propertyCriteria2 = new SculptorPropertyCriteria(CodeMetamacProperties.variableElement().shortName().texts().label(),
                            propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                    return new SculptorCriteriaDisjunction(propertyCriteria1, propertyCriteria2);
                }
                case CODE_PARENT_URN:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.parent().nameableArtefact().urn(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case CODELIST_URN:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.itemSchemeVersion().maintainableArtefact().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case CODELIST_IS_LAST_VERSION:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.itemSchemeVersion().maintainableArtefact().isLastVersion(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case CODELIST_LATEST_FINAL:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.itemSchemeVersion().maintainableArtefact().latestFinal(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                case CODELIST_LATEST_PUBLIC:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.itemSchemeVersion().maintainableArtefact().latestPublic(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperationType());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }
        @SuppressWarnings("unchecked")
        @Override
        public Property<CodeMetamac> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            CodeMetamacCriteriaOrderEnum propertyOrderEnum = CodeMetamacCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return CodeMetamacProperties.nameableArtefact().code();
                case URN:
                    return CodeMetamacProperties.nameableArtefact().urn();
                case CODELIST_URN:
                    return CodeMetamacProperties.itemSchemeVersion().maintainableArtefact().urn();
                case CODELIST_CODE:
                    return CodeMetamacProperties.itemSchemeVersion().maintainableArtefact().code();
                case LAST_UPDATED:
                    return getLastUpdatedLeafProperty(CodeMetamacProperties.nameableArtefact(), CodeMetamac.class);
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<CodeMetamac> retrievePropertyOrderDefault() throws MetamacException {
            return CodeMetamacProperties.id();
        }
    }

    private class CodelistFamilyCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            CodelistFamilyCriteriaPropertyEnum propertyEnum = CodelistFamilyCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyEnum) {
                case CODE:
                    return new SculptorPropertyCriteria(CodelistFamilyProperties.nameableArtefact().code(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(CodelistFamilyProperties.nameableArtefact().name().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public Property<CodelistFamily> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            CodelistFamilyCriteriaOrderEnum propertyOrderEnum = CodelistFamilyCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return CodelistFamilyProperties.nameableArtefact().code();
                case URN:
                    return CodelistFamilyProperties.nameableArtefact().urn();
                case LAST_UPDATED:
                    return getLastUpdatedLeafProperty(CodelistFamilyProperties.nameableArtefact(), CodelistFamily.class);
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<CodelistFamily> retrievePropertyOrderDefault() throws MetamacException {
            return CodelistFamilyProperties.id();
        }
    }

    private class VariableFamilyCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            VariableFamilyCriteriaPropertyEnum propertyEnum = VariableFamilyCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyEnum) {
                case CODE:
                    return new SculptorPropertyCriteria(VariableFamilyProperties.nameableArtefact().code(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(VariableFamilyProperties.nameableArtefact().name().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public Property<VariableFamily> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            VariableFamilyCriteriaOrderEnum propertyOrderEnum = VariableFamilyCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return VariableFamilyProperties.nameableArtefact().code();
                case URN:
                    return VariableFamilyProperties.nameableArtefact().urn();
                case LAST_UPDATED:
                    return getLastUpdatedLeafProperty(VariableFamilyProperties.nameableArtefact(), VariableFamily.class);
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<VariableFamily> retrievePropertyOrderDefault() throws MetamacException {
            return VariableFamilyProperties.id();
        }
    }

    private class VariableCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            VariableCriteriaPropertyEnum propertyEnum = VariableCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyEnum) {
                case CODE:
                    return new SculptorPropertyCriteria(VariableProperties.nameableArtefact().code(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(VariableProperties.nameableArtefact().name().texts().label(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case SHORT_NAME:
                    return new SculptorPropertyCriteria(VariableProperties.shortName().texts().label(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(VariableProperties.nameableArtefact().urn(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case VARIABLE_FAMILY_URN:
                    return new SculptorPropertyCriteria(VariableProperties.families().nameableArtefact().urn(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case GEOGRAPHICAL:
                    return new SculptorPropertyCriteria(VariableProperties.type(), propertyRestriction.getEnumValue(), propertyRestriction.getOperationType());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public Property<Variable> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            VariableCriteriaOrderEnum propertyOrderEnum = VariableCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return VariableProperties.nameableArtefact().code();
                case URN:
                    return VariableProperties.nameableArtefact().urn();
                case LAST_UPDATED:
                    return getLastUpdatedLeafProperty(VariableProperties.nameableArtefact(), Variable.class);
                case VARIABLE_FAMILY_URN:
                    return VariableProperties.families().nameableArtefact().urn();
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<Variable> retrievePropertyOrderDefault() throws MetamacException {
            return VariableProperties.id();
        }
    }

    private class VariableElementCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            VariableElementCriteriaPropertyEnum propertyEnum = VariableElementCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyEnum) {
                case CODE:
                    return new SculptorPropertyCriteria(VariableElementProperties.identifiableArtefact().code(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(VariableElementProperties.identifiableArtefact().urn(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case SHORT_NAME:
                    return new SculptorPropertyCriteria(VariableElementProperties.shortName().texts().label(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case VARIABLE_URN:
                    return new SculptorPropertyCriteria(VariableElementProperties.variable().nameableArtefact().urn(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case IS_GEOGRAPHICAL_VARIABLE_ELEMENT:
                    return new SculptorPropertyCriteria(VariableElementProperties.variable().type(), propertyRestriction.getEnumValue(), propertyRestriction.getOperationType());
                case HAS_SHAPE:
                    return new SculptorPropertyCriteria(VariableElementProperties.shapeWkt(), propertyRestriction.getStringValue(), propertyRestriction.getOperationType());
                case GEOGRAPHICAL_GRANULARITY:
                    return new SculptorPropertyCriteria(VariableElementProperties.geographicalGranularity().nameableArtefact().urn(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperationType());
                case VALID_FROM_DATE:
                    return new SculptorPropertyCriteria(getDatetimeLeafPropertyEmbedded(VariableElementProperties.validFrom(), VariableElement.class), propertyRestriction.getDateValue(),
                            propertyRestriction.getOperationType());
                case VALID_TO_DATE:
                    return new SculptorPropertyCriteria(getDatetimeLeafPropertyEmbedded(VariableElementProperties.validTo(), VariableElement.class), propertyRestriction.getDateValue(),
                            propertyRestriction.getOperationType());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public Property<VariableElement> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            VariableElementCriteriaOrderEnum propertyOrderEnum = VariableElementCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyOrderEnum) {
                case CODE:
                    return VariableElementProperties.identifiableArtefact().code();
                case URN:
                    return VariableElementProperties.identifiableArtefact().urn();
                case LAST_UPDATED:
                    return getLastUpdatedLeafProperty(VariableElementProperties.identifiableArtefact(), VariableElement.class);
                case VARIABLE_URN:
                    return VariableElementProperties.variable().nameableArtefact().urn();
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<VariableElement> retrievePropertyOrderDefault() throws MetamacException {
            return VariableElementProperties.id();
        }
    }

    @SuppressWarnings("rawtypes")
    private LeafProperty getResourceCreatedDateLeafProperty(ItemSchemeProperty itemSchemeProperty, Class entityClass) {
        return CriteriaUtils.getDatetimedLeafProperty(itemSchemeProperty.resourceCreatedDate(), entityClass);
    }

    @SuppressWarnings("rawtypes")
    private LeafProperty getResourceLastUpdatedLeafProperty(ItemSchemeProperty itemSchemeProperty, Class entityClass) {
        return CriteriaUtils.getDatetimedLeafProperty(itemSchemeProperty.resourceLastUpdated(), entityClass);
    }

    @SuppressWarnings("rawtypes")
    private LeafProperty getResourceCreatedDateLeafProperty(StructureProperty structureProperty, Class entityClass) {
        return CriteriaUtils.getDatetimedLeafProperty(structureProperty.resourceCreatedDate(), entityClass);
    }

    @SuppressWarnings("rawtypes")
    private LeafProperty getResourceLastUpdatedLeafProperty(StructureProperty structureProperty, Class entityClass) {
        return CriteriaUtils.getDatetimedLeafProperty(structureProperty.resourceLastUpdated(), entityClass);
    }

    @SuppressWarnings("rawtypes")
    private LeafProperty getLastUpdatedLeafProperty(MaintainableArtefactProperty maintainableArtefactProperty, Class entityClass) {
        return CriteriaUtils.getDatetimedLeafProperty(maintainableArtefactProperty.lastUpdated(), entityClass);
    }

    @SuppressWarnings("rawtypes")
    private LeafProperty getLastUpdatedLeafProperty(NameableArtefactProperty nameableArtefactProperty, Class entityClass) {
        return CriteriaUtils.getDatetimedLeafProperty(nameableArtefactProperty.lastUpdated(), entityClass);
    }

    @SuppressWarnings("rawtypes")
    private LeafProperty getLastUpdatedLeafProperty(IdentifiableArtefactProperty identifiableArtefactProperty, Class entityClass) {
        return CriteriaUtils.getDatetimedLeafProperty(identifiableArtefactProperty.lastUpdated(), entityClass);
    }

    @SuppressWarnings("rawtypes")
    private LeafProperty getDatetimeLeafPropertyEmbedded(Property property, Class entityClass) {
        return CriteriaUtils.getDatetimeLeafPropertyEmbedded(property, entityClass);
    }
}
