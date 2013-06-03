package org.siemac.metamac.srm.rest.internal.v1_0.mapper.category;

import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.common.query.domain.SculptorPropertyCriteria;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategorisationCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategorisationCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategorySchemeCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategorySchemeCriteriaPropertyRestriction;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacProperties;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacProperties;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.base.BaseRest2DoMapperV10Impl;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorisationProperties;

@Component
public class CategoriesRest2DoMapperImpl extends BaseRest2DoMapperV10Impl implements CategoriesRest2DoMapper {

    private RestCriteria2SculptorCriteria<CategorySchemeVersionMetamac> categorySchemeCriteriaMapper = null;
    private RestCriteria2SculptorCriteria<CategoryMetamac>              categoryCriteriaMapper       = null;
    private RestCriteria2SculptorCriteria<Categorisation>               categorisationCriteriaMapper = null;

    public CategoriesRest2DoMapperImpl() {
        categorySchemeCriteriaMapper = new RestCriteria2SculptorCriteria<CategorySchemeVersionMetamac>(CategorySchemeVersionMetamac.class, CategorySchemeCriteriaPropertyOrder.class,
                CategorySchemeCriteriaPropertyRestriction.class, new CategorySchemeCriteriaCallback());
        categoryCriteriaMapper = new RestCriteria2SculptorCriteria<CategoryMetamac>(CategoryMetamac.class, CategoryCriteriaPropertyOrder.class, CategoryCriteriaPropertyRestriction.class,
                new CategoryCriteriaCallback());
        categorisationCriteriaMapper = new RestCriteria2SculptorCriteria<Categorisation>(Categorisation.class, CategorisationCriteriaPropertyOrder.class,
                CategorisationCriteriaPropertyRestriction.class, new CategorisationCriteriaCallback());
    }

    @Override
    public RestCriteria2SculptorCriteria<CategorySchemeVersionMetamac> getCategorySchemeCriteriaMapper() {
        return categorySchemeCriteriaMapper;
    }

    @Override
    public RestCriteria2SculptorCriteria<CategoryMetamac> getCategoryCriteriaMapper() {
        return categoryCriteriaMapper;
    }

    @Override
    public RestCriteria2SculptorCriteria<Categorisation> getCategorisationCriteriaMapper() {
        return categorisationCriteriaMapper;
    }

    private class CategorySchemeCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            CategorySchemeCriteriaPropertyRestriction propertyNameCriteria = CategorySchemeCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().code(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().urnProvider(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().description().texts().label(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case VALID_FROM:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, CategorySchemeVersionMetamacProperties.maintainableArtefact().validFrom(), CategorySchemeVersionMetamac.class, false);
                case VALID_TO:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, CategorySchemeVersionMetamacProperties.maintainableArtefact().validTo(), CategorySchemeVersionMetamac.class, false);
                case PROC_STATUS:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.lifeCycleMetadata().procStatus(),
                            propertyRestrictionValueToProcStatusEnum(propertyRestriction.getValue()), propertyRestriction.getOperationType());
                case INTERNAL_PUBLICATION_DATE:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, CategorySchemeVersionMetamacProperties.lifeCycleMetadata().internalPublicationDate(),
                            CategorySchemeVersionMetamac.class, true);
                case INTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.lifeCycleMetadata().internalPublicationUser(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case EXTERNAL_PUBLICATION_DATE:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, CategorySchemeVersionMetamacProperties.lifeCycleMetadata().externalPublicationDate(),
                            CategorySchemeVersionMetamac.class, true);
                case EXTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.lifeCycleMetadata().externalPublicationUser(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case LATEST:
                    return new SculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().latestFinal(), Boolean.valueOf(propertyRestriction.getValue()),
                            propertyRestriction.getOperationType());
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            CategorySchemeCriteriaPropertyOrder propertyNameCriteria = CategorySchemeCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return CategorySchemeVersionMetamacProperties.maintainableArtefact().code();
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return CategorySchemeVersionMetamacProperties.maintainableArtefact().code();
        }
    }

    private class CategoryCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            CategoryCriteriaPropertyRestriction propertyNameCriteria = CategoryCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return new SculptorPropertyCriteria(CategoryMetamacProperties.nameableArtefact().codeFull(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(CategoryMetamacProperties.nameableArtefact().urnProvider(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(CategoryMetamacProperties.nameableArtefact().name().texts().label(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(CategoryMetamacProperties.nameableArtefact().description().texts().label(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case CATEGORY_SCHEME_URN:
                    return new SculptorPropertyCriteria(CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact().urnProvider(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case CATEGORY_SCHEME_PROC_STATUS:
                    return new SculptorPropertyCriteria(new LeafProperty<CategoryMetamac>(CategoryMetamacProperties.itemSchemeVersion().getName(), CategorySchemeVersionMetamacProperties
                            .lifeCycleMetadata().procStatus().getName(), false, CategoryMetamac.class), propertyRestrictionValueToProcStatusEnum(propertyRestriction.getValue()),
                            propertyRestriction.getOperationType());
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            CategoryCriteriaPropertyOrder propertyNameCriteria = CategoryCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return CategoryMetamacProperties.nameableArtefact().codeFull();
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return CategoryMetamacProperties.nameableArtefact().codeFull();
        }
    }

    private class CategorisationCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            CategorisationCriteriaPropertyRestriction propertyNameCriteria = CategorisationCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return new SculptorPropertyCriteria(CategorisationProperties.maintainableArtefact().code(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(CategorisationProperties.maintainableArtefact().urnProvider(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(CategorisationProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case ARTEFACT_URN:
                    return new SculptorPropertyCriteria(CategorisationProperties.artefactCategorised().urnProvider(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case CATEGORY_URN:
                    return new SculptorPropertyCriteria(CategorisationProperties.category().nameableArtefact().urnProvider(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case LATEST:
                    return new SculptorPropertyCriteria(CategorisationProperties.maintainableArtefact().latestFinal(), Boolean.valueOf(propertyRestriction.getValue()),
                            propertyRestriction.getOperationType());
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            CategorisationCriteriaPropertyOrder propertyNameCriteria = CategorisationCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return CategorisationProperties.maintainableArtefact().code();
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return CategorisationProperties.maintainableArtefact().code();
        }
    }
}