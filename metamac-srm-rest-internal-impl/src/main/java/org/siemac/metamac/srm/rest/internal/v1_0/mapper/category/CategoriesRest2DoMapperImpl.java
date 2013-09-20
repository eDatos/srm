package org.siemac.metamac.srm.rest.internal.v1_0.mapper.category;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaBase;
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
        super();
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
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            CategorySchemeCriteriaPropertyRestriction propertyNameCriteria = CategorySchemeCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return buildSculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, CategorySchemeVersionMetamacProperties.maintainableArtefact());
                case NAME:
                    return buildSculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().name().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case DESCRIPTION:
                    return buildSculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().description().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case VALID_FROM:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, CategorySchemeVersionMetamacProperties.maintainableArtefact().validFrom(),
                            CategorySchemeVersionMetamac.class, false);
                case VALID_TO:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, CategorySchemeVersionMetamacProperties.maintainableArtefact().validTo(),
                            CategorySchemeVersionMetamac.class, false);
                case PROC_STATUS:
                    return buildSculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.lifeCycleMetadata().procStatus(), PropertyTypeEnum.PROC_STATUS, propertyRestriction);
                case INTERNAL_PUBLICATION_DATE:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, CategorySchemeVersionMetamacProperties.lifeCycleMetadata().internalPublicationDate(),
                            CategorySchemeVersionMetamac.class, true);
                case INTERNAL_PUBLICATION_USER:
                    return buildSculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.lifeCycleMetadata().internalPublicationUser(), PropertyTypeEnum.STRING, propertyRestriction);
                case EXTERNAL_PUBLICATION_DATE:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, CategorySchemeVersionMetamacProperties.lifeCycleMetadata().externalPublicationDate(),
                            CategorySchemeVersionMetamac.class, true);
                case EXTERNAL_PUBLICATION_USER:
                    return buildSculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.lifeCycleMetadata().externalPublicationUser(), PropertyTypeEnum.STRING, propertyRestriction);
                case LATEST:
                    return buildSculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().latestFinal(), PropertyTypeEnum.BOOLEAN, propertyRestriction);
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
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
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
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
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            CategoryCriteriaPropertyRestriction propertyNameCriteria = CategoryCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return buildSculptorPropertyCriteria(CategoryMetamacProperties.nameableArtefact().codeFull(), PropertyTypeEnum.STRING, propertyRestriction);
                case URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, CategoryMetamacProperties.nameableArtefact());
                case NAME:
                    return buildSculptorPropertyCriteria(CategoryMetamacProperties.nameableArtefact().name().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case DESCRIPTION:
                    return buildSculptorPropertyCriteria(CategoryMetamacProperties.nameableArtefact().description().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case CATEGORY_SCHEME_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact());
                case CATEGORY_SCHEME_PROC_STATUS:
                    return buildSculptorPropertyCriteria(CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact().publicLogic(), PropertyTypeEnum.PROC_STATUS_ITEM_SCHEME_FROM_ITEM,
                            propertyRestriction);
                case CATEGORY_SCHEME_LATEST:
                    return buildSculptorPropertyCriteria(CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact().latestFinal(), PropertyTypeEnum.BOOLEAN, propertyRestriction);
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
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
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
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
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            CategorisationCriteriaPropertyRestriction propertyNameCriteria = CategorisationCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return buildSculptorPropertyCriteria(CategorisationProperties.maintainableArtefact().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, CategorisationProperties.maintainableArtefact());
                case NAME:
                    return buildSculptorPropertyCriteria(CategorisationProperties.maintainableArtefact().name().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case ARTEFACT_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, CategorisationProperties.artefactCategorised());
                case CATEGORY_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, CategorisationProperties.category().nameableArtefact());
                case LATEST:
                    return buildSculptorPropertyCriteria(CategorisationProperties.maintainableArtefact().latestFinal(), PropertyTypeEnum.BOOLEAN, propertyRestriction);
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
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
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return CategorisationProperties.maintainableArtefact().code();
        }
    }
}