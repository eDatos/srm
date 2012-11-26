package org.siemac.metamac.srm.rest.internal.v1_0.service;

import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.aop.LoggingInterceptor;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.exception.RestCommonServiceExceptionType;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Agencies;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Agency;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencyScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencySchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Category;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.CategoryScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.CategorySchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptTypes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnit;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnitScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnitSchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnits;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacProperties;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.category.CategoriesDo2RestMapperV10;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.category.CategoriesRest2DoMapper;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept.ConceptsDo2RestMapperV10;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept.ConceptsRest2DoMapper;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.organisation.OrganisationsDo2RestMapperV10;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.organisation.OrganisationsRest2DoMapper;
import org.siemac.metamac.srm.rest.internal.v1_0.service.utils.SrmRestInternalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

@Service("srmRestInternalFacadeV10")
public class SrmRestInternalFacadeV10Impl implements SrmRestInternalFacadeV10 {

    @Autowired
    private ConceptsMetamacService        conceptsService;

    @Autowired
    private CategoriesMetamacService      categoriesService;

    @Autowired
    private OrganisationsMetamacService   organisationsService;

    @Autowired
    private ConceptsRest2DoMapper         conceptsRest2DoMapper;

    @Autowired
    private CategoriesRest2DoMapper       categoriesRest2DoMapper;

    @Autowired
    private OrganisationsRest2DoMapper    organisationsRest2DoMapper;

    @Autowired
    private ConceptsDo2RestMapperV10      conceptsDo2RestMapper;

    @Autowired
    private CategoriesDo2RestMapperV10    categoriesDo2RestMapper;

    @Autowired
    private OrganisationsDo2RestMapperV10 organisationsDo2RestMapper;

    private ServiceContext                ctx    = new ServiceContext("restInternal", "restInternal", "restInternal");
    private Logger                        logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    // TODO latest default? cuando no se especifica versión

    @Override
    public ConceptSchemes findConceptSchemes(String query, String orderBy, String limit, String offset) {
        return findConceptSchemes(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public ConceptSchemes findConceptSchemes(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findConceptSchemes(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public ConceptSchemes findConceptSchemes(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findConceptSchemes(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public ConceptScheme retrieveConceptScheme(String agencyID, String resourceID, String version) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            // Find one
            PagedResult<ConceptSchemeVersionMetamac> entitiesPagedResult = findConceptSchemesCore(agencyID, resourceID, version, null, PagingParameter.pageAccess(1, 1, false));
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND, agencyID, resourceID, version);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            ConceptScheme conceptScheme = conceptsDo2RestMapper.toConceptScheme(entitiesPagedResult.getValues().get(0));
            return conceptScheme;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Concepts findConcepts(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            checkParameterNotWildcardFindItems(agencyID, resourceID, version);
            
            // Find
            SculptorCriteria sculptorCriteria = conceptsRest2DoMapper.getConceptCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);
            PagedResult<ConceptMetamac> entitiesPagedResult = findConceptsCore(agencyID, resourceID, version, null, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            Concepts concepts = conceptsDo2RestMapper.toConcepts(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit());
            return concepts;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Concept retrieveConcept(String agencyID, String resourceID, String version, String conceptID) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, conceptID, RestInternalConstants.PARAMETER_CONCEPT_ID);

            // Find one
            PagedResult<ConceptMetamac> entitiesPagedResult = findConceptsCore(agencyID, resourceID, version, conceptID, null, PagingParameter.pageAccess(1, 1, false));
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CONCEPT_NOT_FOUND, conceptID, agencyID, resourceID, version);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            Concept concept = conceptsDo2RestMapper.toConcept(entitiesPagedResult.getValues().get(0));
            return concept;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public ConceptTypes retrieveConceptTypes() {
        try {
            // Retrieve all
            List<ConceptType> entitiesResult = conceptsService.findAllConceptTypes(ctx);

            // Transform
            ConceptTypes conceptTypes = conceptsDo2RestMapper.toConceptTypes(entitiesResult);
            return conceptTypes;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public CategorySchemes findCategorySchemes(String query, String orderBy, String limit, String offset) {
        return findCategorySchemes(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public CategorySchemes findCategorySchemes(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findCategorySchemes(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public CategorySchemes findCategorySchemes(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findCategorySchemes(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public CategoryScheme retrieveCategoryScheme(String agencyID, String resourceID, String version) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            // Find one
            PagedResult<CategorySchemeVersionMetamac> entitiesPagedResult = findCategorySchemesCore(agencyID, resourceID, version, null, PagingParameter.pageAccess(1, 1, false));
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CATEGORY_SCHEME_NOT_FOUND, agencyID, resourceID, version);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            CategoryScheme categoryScheme = categoriesDo2RestMapper.toCategoryScheme(entitiesPagedResult.getValues().get(0));
            return categoryScheme;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Categories findCategories(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            checkParameterNotWildcardFindItems(agencyID, resourceID, version);

            // Find
            SculptorCriteria sculptorCriteria = categoriesRest2DoMapper.getCategoryCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);
            PagedResult<CategoryMetamac> entitiesPagedResult = findCategoriesCore(agencyID, resourceID, version, null, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            Categories categories = categoriesDo2RestMapper.toCategories(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit());
            return categories;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Category retrieveCategory(String agencyID, String resourceID, String version, String categoryID) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, categoryID, RestInternalConstants.PARAMETER_CATEGORY_ID);
            
            // Find one
            PagedResult<CategoryMetamac> entitiesPagedResult = findCategoriesCore(agencyID, resourceID, version, categoryID, null, PagingParameter.pageAccess(1, 1, false));
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CATEGORY_NOT_FOUND, categoryID, agencyID, resourceID, version);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            Category category = categoriesDo2RestMapper.toCategory(entitiesPagedResult.getValues().get(0));
            return category;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public AgencySchemes findAgencySchemes(String query, String orderBy, String limit, String offset) {
        return findAgencySchemes(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public AgencySchemes findAgencySchemes(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findAgencySchemes(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public AgencySchemes findAgencySchemes(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findAgencySchemes(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public AgencyScheme retrieveAgencyScheme(String agencyID, String resourceID, String version) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            // Find one
            PagedResult<OrganisationSchemeVersionMetamac> entitiesPagedResult = findOrganisationSchemesCore(OrganisationSchemeTypeEnum.AGENCY_SCHEME, agencyID, resourceID, version, null,
                    PagingParameter.pageAccess(1, 1, false));
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.AGENCY_SCHEME_NOT_FOUND, agencyID, resourceID, version);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            AgencyScheme itemScheme = organisationsDo2RestMapper.toAgencyScheme(entitiesPagedResult.getValues().get(0));
            return itemScheme;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Agencies findAgencies(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            checkParameterNotWildcardFindItems(agencyID, resourceID, version);

            // Find
            SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);
            PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.AGENCY, agencyID, resourceID, version, null, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            Agencies items = organisationsDo2RestMapper.toAgencies(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit());
            return items;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Agency retrieveAgency(String agencyID, String resourceID, String version, String organisationID) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, organisationID, RestInternalConstants.PARAMETER_ORGANISATION_ID);
            
            // Find one
            PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.AGENCY, agencyID, resourceID, version, organisationID, null,
                    PagingParameter.pageAccess(1, 1, false));
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.AGENCY_NOT_FOUND, organisationID, agencyID, resourceID,
                        version);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            Agency item = organisationsDo2RestMapper.toAgency(entitiesPagedResult.getValues().get(0));
            return item;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public OrganisationUnitSchemes findOrganisationUnitSchemes(String query, String orderBy, String limit, String offset) {
        return findOrganisationUnitSchemes(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public OrganisationUnitSchemes findOrganisationUnitSchemes(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findOrganisationUnitSchemes(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public OrganisationUnitSchemes findOrganisationUnitSchemes(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findOrganisationUnitSchemes(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public OrganisationUnitScheme retrieveOrganisationUnitScheme(String agencyID, String resourceID, String version) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            // Find one
            PagedResult<OrganisationSchemeVersionMetamac> entitiesPagedResult = findOrganisationSchemesCore(OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME, agencyID, resourceID, version, null,
                    PagingParameter.pageAccess(1, 1, false));
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.ORGANISATION_UNIT_SCHEME_NOT_FOUND, agencyID, resourceID,
                        version);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            OrganisationUnitScheme itemScheme = organisationsDo2RestMapper.toOrganisationUnitScheme(entitiesPagedResult.getValues().get(0));
            return itemScheme;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public OrganisationUnits findOrganisationUnits(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            checkParameterNotWildcardFindItems(agencyID, resourceID, version);

            // Find
            SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);
            PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.ORGANISATION_UNIT, agencyID, resourceID, version, null, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            OrganisationUnits items = organisationsDo2RestMapper.toOrganisationUnits(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit());
            return items;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public OrganisationUnit retrieveOrganisationUnit(String agencyID, String resourceID, String version, String organisationID) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, organisationID, RestInternalConstants.PARAMETER_ORGANISATION_ID);
            
            // Find one
            PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.ORGANISATION_UNIT, agencyID, resourceID, version, organisationID, null,
                    PagingParameter.pageAccess(1, 1, false));
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.ORGANISATION_UNIT_NOT_FOUND, organisationID, agencyID,
                        resourceID, version);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            OrganisationUnit item = organisationsDo2RestMapper.toOrganisationUnit(entitiesPagedResult.getValues().get(0));
            return item;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private ConceptSchemes findConceptSchemes(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = conceptsRest2DoMapper.getConceptSchemeCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Find
            PagedResult<ConceptSchemeVersionMetamac> entitiesPagedResult = findConceptSchemesCore(agencyID, resourceID, version, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            ConceptSchemes conceptSchemes = conceptsDo2RestMapper.toConceptSchemes(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
            return conceptSchemes;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesCore(String agencyID, String resourceID, String version, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find item schemes by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItemSchemes(agencyID, resourceID, version, conditionalCriteriaQuery,
                ConceptSchemeVersionMetamac.class);

        // Find
        PagedResult<ConceptSchemeVersionMetamac> entitiesPagedResult = conceptsService.findConceptSchemesByCondition(ctx, conditionalCriteria, pagingParameter);
        return entitiesPagedResult;
    }

    private PagedResult<ConceptMetamac> findConceptsCore(String agencyID, String resourceID, String version, String conceptID, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find items by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItems(agencyID, resourceID, version, conceptID, conditionalCriteriaQuery, ConceptMetamac.class);

        // Find
        PagedResult<ConceptMetamac> entitiesPagedResult = conceptsService.findConceptsByCondition(ctx, conditionalCriteria, pagingParameter);
        return entitiesPagedResult;
    }

    private CategorySchemes findCategorySchemes(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = categoriesRest2DoMapper.getCategorySchemeCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Find
            PagedResult<CategorySchemeVersionMetamac> entitiesPagedResult = findCategorySchemesCore(agencyID, resourceID, version, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            CategorySchemes categorySchemes = categoriesDo2RestMapper.toCategorySchemes(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
            return categorySchemes;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private PagedResult<CategorySchemeVersionMetamac> findCategorySchemesCore(String agencyID, String resourceID, String version, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find item schemes by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItemSchemes(agencyID, resourceID, version, conditionalCriteriaQuery,
                CategorySchemeVersionMetamac.class);

        // Find
        PagedResult<CategorySchemeVersionMetamac> entitiesPagedResult = categoriesService.findCategorySchemesByCondition(ctx, conditionalCriteria, pagingParameter);
        return entitiesPagedResult;
    }

    private PagedResult<CategoryMetamac> findCategoriesCore(String agencyID, String resourceID, String version, String categoryID, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find items by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItems(agencyID, resourceID, version, categoryID, conditionalCriteriaQuery, CategoryMetamac.class);

        // Find
        PagedResult<CategoryMetamac> entitiesPagedResult = categoriesService.findCategoriesByCondition(ctx, conditionalCriteria, pagingParameter);
        return entitiesPagedResult;
    }

    private AgencySchemes findAgencySchemes(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationSchemeCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Find
            PagedResult<OrganisationSchemeVersionMetamac> entitiesPagedResult = findOrganisationSchemesCore(OrganisationSchemeTypeEnum.AGENCY_SCHEME, agencyID, resourceID, version,
                    sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            AgencySchemes itemSchemes = organisationsDo2RestMapper.toAgencySchemes(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
            return itemSchemes;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private OrganisationUnitSchemes findOrganisationUnitSchemes(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationSchemeCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Find
            PagedResult<OrganisationSchemeVersionMetamac> entitiesPagedResult = findOrganisationSchemesCore(OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME, agencyID, resourceID, version,
                    sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            OrganisationUnitSchemes itemSchemes = organisationsDo2RestMapper.toOrganisationUnitSchemes(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
            return itemSchemes;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private PagedResult<OrganisationSchemeVersionMetamac> findOrganisationSchemesCore(OrganisationSchemeTypeEnum type, String agencyID, String resourceID, String version,
            List<ConditionalCriteria> conditionalCriteriaQuery, PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find item schemes by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItemSchemes(agencyID, resourceID, version, conditionalCriteriaQuery,
                OrganisationSchemeVersionMetamac.class);
        if (type != null) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(OrganisationSchemeVersionMetamac.class).withProperty(OrganisationSchemeVersionMetamacProperties.organisationSchemeType())
                    .eq(type).buildSingle());
        }

        // Find
        PagedResult<OrganisationSchemeVersionMetamac> entitiesPagedResult = organisationsService.findOrganisationSchemesByCondition(ctx, conditionalCriteria, pagingParameter);
        return entitiesPagedResult;
    }

    private PagedResult<OrganisationMetamac> findOrganisationsCore(OrganisationTypeEnum type, String agencyID, String resourceID, String version, String organisationID,
            List<ConditionalCriteria> conditionalCriteriaQuery, PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find items by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItems(agencyID, resourceID, version, organisationID, conditionalCriteriaQuery,
                ConceptMetamac.class);
        if (type != null) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(OrganisationMetamac.class).withProperty(OrganisationMetamacProperties.organisationType()).eq(type).buildSingle());
        }

        // Find
        PagedResult<OrganisationMetamac> entitiesPagedResult = organisationsService.findOrganisationsByCondition(ctx, conditionalCriteria, pagingParameter);
        return entitiesPagedResult;
    }

    /**
     * Throws response error, logging exception
     */
    private RestException manageException(Exception e) {
        logger.error("Error", e);
        if (e instanceof RestException) {
            return (RestException) e;
        } else {
            // do not show information details about exception to user
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestCommonServiceExceptionType.UNKNOWN);
            return new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }

    private void checkParameterNotWildcardFindItemSchemes(String agencyID) {
        checkParameterNotWildcard(RestInternalConstants.PARAMETER_AGENCY_ID, agencyID);
    }

    private void checkParameterNotWildcardFindItemSchemes(String agencyID, String resourceID) {
        checkParameterNotWildcard(RestInternalConstants.PARAMETER_RESOURCE_ID, resourceID);
    }

    private void checkParameterNotWildcardRetrieveItemScheme(String agencyID, String resourceID, String version) {
        checkParameterNotWildcard(RestInternalConstants.PARAMETER_AGENCY_ID, agencyID);
        checkParameterNotWildcard(RestInternalConstants.PARAMETER_RESOURCE_ID, resourceID);
        checkParameterNotWildcard(RestInternalConstants.PARAMETER_VERSION, version);
    }

    private void checkParameterNotWildcardFindItems(String agencyID, String resourceID, String version) {
        // nothing
    }

    private void checkParameterNotWildcardRetrieveItem(String agencyID, String resourceID, String version, String itemID, String parameterNameItemID) {
        checkParameterNotWildcard(RestInternalConstants.PARAMETER_AGENCY_ID, agencyID);
        checkParameterNotWildcard(RestInternalConstants.PARAMETER_RESOURCE_ID, resourceID);
        checkParameterNotWildcard(RestInternalConstants.PARAMETER_VERSION, version);
        checkParameterNotWildcard(parameterNameItemID, itemID);
    }

    /**
     * Check unsupported wildcards in some parameters
     */
    private void checkParameterNotWildcard(String parameterName, String parameterValue) {
        if (RestInternalConstants.WILDCARD.equals(parameterValue) || RestInternalConstants.LATEST.equals(parameterValue)) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.PARAMETER_INCORRECT, parameterName);
            throw new RestException(exception, Status.BAD_REQUEST);
        }
    }
}