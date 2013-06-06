package org.siemac.metamac.srm.rest.internal.v1_0.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.aop.LoggingInterceptor;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.exception.RestCommonServiceExceptionType;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Agencies;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Agency;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AgencyScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AgencySchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categorisation;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categorisations;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategorySchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelist;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistFamilies;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistFamily;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelists;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptTypes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataConsumer;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataConsumerScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataConsumerSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataConsumers;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProvider;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProviderScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProviderSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProviders;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructures;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Organisation;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnit;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnitScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnitSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnits;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Organisations;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Variable;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableFamilies;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableFamily;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Variables;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacProperties;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.VariableFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.VariableProperties;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.serviceapi.DataStructureDefinitionMetamacService;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacProperties;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.category.CategoriesDo2RestMapperV10;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.category.CategoriesRest2DoMapper;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.code.CodesDo2RestMapperV10;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.code.CodesRest2DoMapper;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept.ConceptsDo2RestMapperV10;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept.ConceptsRest2DoMapper;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.dsd.DataStructuresDo2RestMapperV10;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.dsd.DataStructuresRest2DoMapper;
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
    private ConceptsMetamacService                conceptsService;

    @Autowired
    private CategoriesMetamacService              categoriesService;

    @Autowired
    private OrganisationsMetamacService           organisationsService;

    @Autowired
    private CodesMetamacService                   codesService;

    @Autowired
    private DataStructureDefinitionMetamacService dataStructureDefinitionService;

    @Autowired
    private ConceptsRest2DoMapper                 conceptsRest2DoMapper;

    @Autowired
    private CategoriesRest2DoMapper               categoriesRest2DoMapper;

    @Autowired
    private OrganisationsRest2DoMapper            organisationsRest2DoMapper;

    @Autowired
    private CodesRest2DoMapper                    codesRest2DoMapper;

    @Autowired
    private DataStructuresRest2DoMapper           dataStructuresRest2DoMapper;

    @Autowired
    private ConceptsDo2RestMapperV10              conceptsDo2RestMapper;

    @Autowired
    private CategoriesDo2RestMapperV10            categoriesDo2RestMapper;

    @Autowired
    private OrganisationsDo2RestMapperV10         organisationsDo2RestMapper;

    @Autowired
    private CodesDo2RestMapperV10                 codesDo2RestMapper;

    @Autowired
    private DataStructuresDo2RestMapperV10        dataStructuresDo2RestMapperV10;

    private final ServiceContext                  ctx    = new ServiceContext("restInternal", "restInternal", "restInternal");
    private final Logger                          logger = LoggerFactory.getLogger(LoggingInterceptor.class);

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
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND, resourceID, version, agencyID);
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
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CONCEPT_NOT_FOUND, conceptID, version, resourceID, agencyID);
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
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CATEGORY_SCHEME_NOT_FOUND, resourceID, version, agencyID);
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
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils
                        .getException(RestServiceExceptionType.CATEGORY_NOT_FOUND, categoryID, version, resourceID, agencyID);
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
    public Categorisations findCategorisations(String query, String orderBy, String limit, String offset) {
        return findCategorisations(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public Categorisations findCategorisations(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findCategorisations(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public Categorisations findCategorisations(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findCategorisations(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public Categorisation retrieveCategorisation(String agencyID, String resourceID, String version) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            // Find one
            PagedResult<com.arte.statistic.sdmx.srm.core.category.domain.Categorisation> entitiesPagedResult = findCategorisationsCore(agencyID, resourceID, version, null,
                    PagingParameter.pageAccess(1, 1, false));
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CATEGORISATION_NOT_FOUND, resourceID, version, agencyID);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            Categorisation categorisation = categoriesDo2RestMapper.toCategorisation(entitiesPagedResult.getValues().get(0));
            return categorisation;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public OrganisationSchemes findOrganisationSchemes(String query, String orderBy, String limit, String offset) {
        return findOrganisationSchemes(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public OrganisationSchemes findOrganisationSchemes(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findOrganisationSchemes(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public OrganisationSchemes findOrganisationSchemes(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findOrganisationSchemes(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public OrganisationScheme retrieveOrganisationScheme(String agencyID, String resourceID, String version) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            // Find one
            PagedResult<OrganisationSchemeVersionMetamac> entitiesPagedResult = findOrganisationSchemesCore(null, agencyID, resourceID, version, null, PagingParameter.pageAccess(1, 1, false));
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.ORGANISATION_SCHEME_NOT_FOUND, resourceID, version, agencyID);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            OrganisationScheme itemScheme = organisationsDo2RestMapper.toOrganisationScheme(entitiesPagedResult.getValues().get(0));
            return itemScheme;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Organisations findOrganisations(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            checkParameterNotWildcardFindItems(agencyID, resourceID, version);

            // Find
            SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);
            PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(null, agencyID, resourceID, version, null, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            Organisations items = organisationsDo2RestMapper.toOrganisations(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit());
            return items;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Organisation retrieveOrganisation(String agencyID, String resourceID, String version, String organisationID) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, organisationID, RestInternalConstants.PARAMETER_ORGANISATION_ID);

            // Find one
            PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(null, agencyID, resourceID, version, organisationID, null, PagingParameter.pageAccess(1, 1, false));
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.ORGANISATION_NOT_FOUND, organisationID, version, resourceID,
                        agencyID);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            Organisation item = organisationsDo2RestMapper.toOrganisation(entitiesPagedResult.getValues().get(0));
            return item;
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
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.AGENCY_SCHEME_NOT_FOUND, resourceID, version, agencyID);
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
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.AGENCY_NOT_FOUND, organisationID, version, resourceID,
                        agencyID);
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
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.ORGANISATION_UNIT_SCHEME_NOT_FOUND, resourceID, version,
                        agencyID);
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
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.ORGANISATION_UNIT_NOT_FOUND, organisationID, version,
                        resourceID, agencyID);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            OrganisationUnit item = organisationsDo2RestMapper.toOrganisationUnit(entitiesPagedResult.getValues().get(0));
            return item;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public DataProviderSchemes findDataProviderSchemes(String query, String orderBy, String limit, String offset) {
        return findDataProviderSchemes(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public DataProviderSchemes findDataProviderSchemes(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findDataProviderSchemes(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public DataProviderSchemes findDataProviderSchemes(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findDataProviderSchemes(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public DataProviderScheme retrieveDataProviderScheme(String agencyID, String resourceID, String version) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            // Find one
            PagedResult<OrganisationSchemeVersionMetamac> entitiesPagedResult = findOrganisationSchemesCore(OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME, agencyID, resourceID, version, null,
                    PagingParameter.pageAccess(1, 1, false));
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils
                        .getException(RestServiceExceptionType.DATA_PROVIDER_SCHEME_NOT_FOUND, resourceID, version, agencyID);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            DataProviderScheme itemScheme = organisationsDo2RestMapper.toDataProviderScheme(entitiesPagedResult.getValues().get(0));
            return itemScheme;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public DataProviders findDataProviders(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            checkParameterNotWildcardFindItems(agencyID, resourceID, version);

            // Find
            SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);
            PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.DATA_PROVIDER, agencyID, resourceID, version, null, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            DataProviders items = organisationsDo2RestMapper.toDataProviders(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit());
            return items;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public DataProvider retrieveDataProvider(String agencyID, String resourceID, String version, String organisationID) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, organisationID, RestInternalConstants.PARAMETER_ORGANISATION_ID);

            // Find one
            PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.DATA_PROVIDER, agencyID, resourceID, version, organisationID, null,
                    PagingParameter.pageAccess(1, 1, false));
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.DATA_PROVIDER_NOT_FOUND, organisationID, version, resourceID,
                        agencyID);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            DataProvider item = organisationsDo2RestMapper.toDataProvider(entitiesPagedResult.getValues().get(0));
            return item;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public DataConsumerSchemes findDataConsumerSchemes(String query, String orderBy, String limit, String offset) {
        return findDataConsumerSchemes(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public DataConsumerSchemes findDataConsumerSchemes(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findDataConsumerSchemes(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public DataConsumerSchemes findDataConsumerSchemes(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findDataConsumerSchemes(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public DataConsumerScheme retrieveDataConsumerScheme(String agencyID, String resourceID, String version) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            // Find one
            PagedResult<OrganisationSchemeVersionMetamac> entitiesPagedResult = findOrganisationSchemesCore(OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME, agencyID, resourceID, version, null,
                    PagingParameter.pageAccess(1, 1, false));
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils
                        .getException(RestServiceExceptionType.DATA_CONSUMER_SCHEME_NOT_FOUND, resourceID, version, agencyID);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            DataConsumerScheme itemScheme = organisationsDo2RestMapper.toDataConsumerScheme(entitiesPagedResult.getValues().get(0));
            return itemScheme;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public DataConsumers findDataConsumers(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            checkParameterNotWildcardFindItems(agencyID, resourceID, version);

            // Find
            SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);
            PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.DATA_CONSUMER, agencyID, resourceID, version, null, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            DataConsumers items = organisationsDo2RestMapper.toDataConsumers(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit());
            return items;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public DataConsumer retrieveDataConsumer(String agencyID, String resourceID, String version, String organisationID) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, organisationID, RestInternalConstants.PARAMETER_ORGANISATION_ID);

            // Find one
            PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.DATA_CONSUMER, agencyID, resourceID, version, organisationID, null,
                    PagingParameter.pageAccess(1, 1, false));
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.DATA_CONSUMER_NOT_FOUND, organisationID, version, resourceID,
                        agencyID);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            DataConsumer item = organisationsDo2RestMapper.toDataConsumer(entitiesPagedResult.getValues().get(0));
            return item;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Codelists findCodelists(String query, String orderBy, String limit, String offset) {
        return findCodelists(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public Codelists findCodelists(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findCodelists(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public Codelists findCodelists(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findCodelists(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public Codelist retrieveCodelist(String agencyID, String resourceID, String version) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            // Find one
            PagedResult<CodelistVersionMetamac> entitiesPagedResult = findCodelistsCore(agencyID, resourceID, version, null, PagingParameter.pageAccess(1, 1, false));
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CODELIST_NOT_FOUND, resourceID, version, agencyID);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            Codelist codelist = codesDo2RestMapper.toCodelist(entitiesPagedResult.getValues().get(0));
            return codelist;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Codes findCodes(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            checkParameterNotWildcardFindItems(agencyID, resourceID, version);

            // Find
            SculptorCriteria sculptorCriteria = codesRest2DoMapper.getCodeCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);
            PagedResult<CodeMetamac> entitiesPagedResult = findCodesCore(agencyID, resourceID, version, null, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            Codes codes = codesDo2RestMapper.toCodes(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit());
            return codes;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public VariableFamily retrieveVariableFamilyById(String id) {
        try {
            // Find one
            PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily> entitiesPagedResult = findVariableFamiliesCore(id, null, PagingParameter.pageAccess(1, 1, false));
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.VARIABLE_FAMILY_NOT_FOUND, id);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            org.siemac.metamac.srm.core.code.domain.VariableFamily variableFamilyEntity = entitiesPagedResult.getValues().get(0);
            VariableFamily variableFamily = codesDo2RestMapper.toVariableFamily(variableFamilyEntity);
            return variableFamily;

        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public VariableFamilies findVariableFamilies(String query, String orderBy, String limit, String offset) {
        try {
            // Retrieve variableFamilies by criteria
            SculptorCriteria sculptorCriteria = codesRest2DoMapper.getVariableFamilyCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Retrieve
            PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily> entitiesPagedResult = findVariableFamiliesCore(null, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            VariableFamilies variableFamilies = codesDo2RestMapper.toVariableFamilies(entitiesPagedResult, query, orderBy, sculptorCriteria.getLimit());
            return variableFamilies;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Variables findVariablesByFamily(String familyID, String query, String orderBy, String limit, String offset) {
        try {
            // Retrieve variables by criteria
            SculptorCriteria sculptorCriteria = codesRest2DoMapper.getVariableCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Retrieve
            PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> entitiesPagedResult = findVariablesCore(null, familyID, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            Variables variables = codesDo2RestMapper.toVariablesByFamily(familyID, entitiesPagedResult, query, orderBy, sculptorCriteria.getLimit());
            return variables;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Variable retrieveVariableById(String id) {
        try {
            // Find one
            PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> entitiesPagedResult = findVariablesCore(id, null, null, PagingParameter.pageAccess(1, 1, false));
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.VARIABLE_NOT_FOUND, id);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            org.siemac.metamac.srm.core.code.domain.Variable variableEntity = entitiesPagedResult.getValues().get(0);
            Variable variable = codesDo2RestMapper.toVariable(variableEntity);
            return variable;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Variables findVariables(String query, String orderBy, String limit, String offset) {
        try {
            // Retrieve variables by criteria
            SculptorCriteria sculptorCriteria = codesRest2DoMapper.getVariableCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Retrieve
            PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> entitiesPagedResult = findVariablesCore(null, null, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            Variables variables = codesDo2RestMapper.toVariables(entitiesPagedResult, query, orderBy, sculptorCriteria.getLimit());
            return variables;
        } catch (Exception e) {
            throw manageException(e);
        }
    }
    @Override
    public CodelistFamily retrieveCodelistFamilyById(String id) {
        try {
            // Find one
            PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistFamily> entitiesPagedResult = findCodelistFamiliesCore(id, null, PagingParameter.pageAccess(1, 1, false));
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CODELIST_FAMILY_NOT_FOUND, id);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            org.siemac.metamac.srm.core.code.domain.CodelistFamily codelistFamilyEntity = entitiesPagedResult.getValues().get(0);
            CodelistFamily codelistFamily = codesDo2RestMapper.toCodelistFamily(codelistFamilyEntity);
            return codelistFamily;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public CodelistFamilies findCodelistFamilies(String query, String orderBy, String limit, String offset) {
        try {
            // Retrieve codelistFamilies by criteria
            SculptorCriteria sculptorCriteria = codesRest2DoMapper.getCodelistFamilyCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Retrieve
            PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistFamily> entitiesPagedResult = findCodelistFamiliesCore(null, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            CodelistFamilies codelistFamilies = codesDo2RestMapper.toCodelistFamilies(entitiesPagedResult, query, orderBy, sculptorCriteria.getLimit());
            return codelistFamilies;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Code retrieveCode(String agencyID, String resourceID, String version, String codeID) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, codeID, RestInternalConstants.PARAMETER_CODE_ID);

            // Find one
            PagedResult<CodeMetamac> entitiesPagedResult = findCodesCore(agencyID, resourceID, version, codeID, null, PagingParameter.pageAccess(1, 1, false));
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CODE_NOT_FOUND, codeID, version, resourceID, agencyID);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            Code code = codesDo2RestMapper.toCode(entitiesPagedResult.getValues().get(0));
            return code;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public DataStructures findDataStructures(String query, String orderBy, String limit, String offset) {
        return findDataStructures(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public DataStructures findDataStructures(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findDataStructures(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public DataStructures findDataStructures(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findDataStructures(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public DataStructure retrieveDataStructure(String agencyID, String resourceID, String version) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            // Find one
            PagedResult<DataStructureDefinitionVersionMetamac> entitiesPagedResult = findDataStructuresCore(agencyID, resourceID, version, null, PagingParameter.pageAccess(1, 1, false));
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.DATA_STRUCTURE_NOT_FOUND, resourceID, version, agencyID);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            DataStructure dataStructure = dataStructuresDo2RestMapperV10.toDataStructure(entitiesPagedResult.getValues().get(0));
            return dataStructure;
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
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItems(agencyID, resourceID, version, conceptID, ConceptMetamacProperties.itemSchemeVersion()
                .maintainableArtefact(), conditionalCriteriaQuery, ConceptMetamac.class);

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

    private Categorisations findCategorisations(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = categoriesRest2DoMapper.getCategorisationCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Find
            PagedResult<com.arte.statistic.sdmx.srm.core.category.domain.Categorisation> entitiesPagedResult = findCategorisationsCore(agencyID, resourceID, version, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            Categorisations categorisations = categoriesDo2RestMapper.toCategorisations(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
            return categorisations;
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
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItems(agencyID, resourceID, version, categoryID, CategoryMetamacProperties.itemSchemeVersion()
                .maintainableArtefact(), conditionalCriteriaQuery, CategoryMetamac.class);

        // Find
        PagedResult<CategoryMetamac> entitiesPagedResult = categoriesService.findCategoriesByCondition(ctx, conditionalCriteria, pagingParameter);
        return entitiesPagedResult;
    }

    private PagedResult<com.arte.statistic.sdmx.srm.core.category.domain.Categorisation> findCategorisationsCore(String agencyID, String resourceID, String version,
            List<ConditionalCriteria> conditionalCriteriaQuery, PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItemSchemes(agencyID, resourceID, version, conditionalCriteriaQuery, Categorisation.class);

        // Find
        PagedResult<com.arte.statistic.sdmx.srm.core.category.domain.Categorisation> entitiesPagedResult = categoriesService.findCategorisationsByCondition(ctx, conditionalCriteria, pagingParameter);
        return entitiesPagedResult;
    }

    private OrganisationSchemes findOrganisationSchemes(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationSchemeCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Find
            PagedResult<OrganisationSchemeVersionMetamac> entitiesPagedResult = findOrganisationSchemesCore(null, agencyID, resourceID, version, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            OrganisationSchemes itemSchemes = organisationsDo2RestMapper.toOrganisationSchemes(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
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

    private DataProviderSchemes findDataProviderSchemes(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationSchemeCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Find
            PagedResult<OrganisationSchemeVersionMetamac> entitiesPagedResult = findOrganisationSchemesCore(OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME, agencyID, resourceID, version,
                    sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            DataProviderSchemes itemSchemes = organisationsDo2RestMapper.toDataProviderSchemes(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
            return itemSchemes;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private DataConsumerSchemes findDataConsumerSchemes(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationSchemeCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Find
            PagedResult<OrganisationSchemeVersionMetamac> entitiesPagedResult = findOrganisationSchemesCore(OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME, agencyID, resourceID, version,
                    sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            DataConsumerSchemes itemSchemes = organisationsDo2RestMapper.toDataConsumerSchemes(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
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

    private Codelists findCodelists(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = codesRest2DoMapper.getCodelistCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Find
            PagedResult<CodelistVersionMetamac> entitiesPagedResult = findCodelistsCore(agencyID, resourceID, version, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            Codelists codelists = codesDo2RestMapper.toCodelists(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
            return codelists;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private PagedResult<CodelistVersionMetamac> findCodelistsCore(String agencyID, String resourceID, String version, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find item schemes by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItemSchemes(agencyID, resourceID, version, conditionalCriteriaQuery, CodelistVersionMetamac.class);
        conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).withProperty(CodelistVersionMetamacProperties.accessType()).eq(AccessTypeEnum.PUBLIC)
                .buildSingle());

        // Find
        PagedResult<CodelistVersionMetamac> entitiesPagedResult = codesService.findCodelistsByCondition(ctx, conditionalCriteria, pagingParameter);
        return entitiesPagedResult;
    }

    private PagedResult<OrganisationMetamac> findOrganisationsCore(OrganisationTypeEnum type, String agencyID, String resourceID, String version, String organisationID,
            List<ConditionalCriteria> conditionalCriteriaQuery, PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find items by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItems(agencyID, resourceID, version, organisationID, OrganisationMetamacProperties
                .itemSchemeVersion().maintainableArtefact(), conditionalCriteriaQuery, OrganisationMetamac.class);
        if (type != null) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(OrganisationMetamac.class).withProperty(OrganisationMetamacProperties.organisationType()).eq(type).buildSingle());
        }

        // Find
        PagedResult<OrganisationMetamac> entitiesPagedResult = organisationsService.findOrganisationsByCondition(ctx, conditionalCriteria, pagingParameter);
        return entitiesPagedResult;
    }

    private PagedResult<CodeMetamac> findCodesCore(String agencyID, String resourceID, String version, String codeID, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find items by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItems(agencyID, resourceID, version, codeID, CodeMetamacProperties.itemSchemeVersion()
                .maintainableArtefact(), conditionalCriteriaQuery, CodeMetamac.class);
        // Only codelists with access == public
        conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class)
                .withProperty(new LeafProperty<CodeMetamac>(CodeMetamacProperties.itemSchemeVersion().getName(), CodelistVersionMetamacProperties.accessType().getName(), false, CodeMetamac.class))
                .eq(AccessTypeEnum.PUBLIC).buildSingle());

        // Find
        PagedResult<CodeMetamac> entitiesPagedResult = codesService.findCodesByCondition(ctx, conditionalCriteria, pagingParameter);
        return entitiesPagedResult;
    }

    private DataStructures findDataStructures(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = dataStructuresRest2DoMapper.getDataStructureDefinitionCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Find
            PagedResult<DataStructureDefinitionVersionMetamac> entitiesPagedResult = findDataStructuresCore(agencyID, resourceID, version, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            DataStructures dataStructures = dataStructuresDo2RestMapperV10.toDataStructures(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
            return dataStructures;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private PagedResult<DataStructureDefinitionVersionMetamac> findDataStructuresCore(String agencyID, String resourceID, String version, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaStructures(agencyID, resourceID, version, conditionalCriteriaQuery,
                DataStructureDefinitionVersionMetamac.class);

        // Find
        PagedResult<DataStructureDefinitionVersionMetamac> entitiesPagedResult = dataStructureDefinitionService.findDataStructureDefinitionsByCondition(ctx, conditionalCriteria, pagingParameter);
        return entitiesPagedResult;
    }

    private PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily> findVariableFamiliesCore(String variableFamilyID, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        List<ConditionalCriteria> conditionalCriteria = new ArrayList<ConditionalCriteria>();
        if (CollectionUtils.isNotEmpty(conditionalCriteriaQuery)) {
            conditionalCriteria.addAll(conditionalCriteriaQuery);
        } else {
            // init
            conditionalCriteria.addAll(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.VariableFamily.class).distinctRoot().build());
        }
        if (variableFamilyID != null) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.VariableFamily.class)
                    .withProperty(VariableFamilyProperties.nameableArtefact().code()).eq(variableFamilyID).buildSingle());
        }
        // Find
        PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily> entitiesPagedResult = codesService.findVariableFamiliesByCondition(ctx, conditionalCriteria, pagingParameter);
        return entitiesPagedResult;
    }

    private PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> findVariablesCore(String variableID, String familyID, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        List<ConditionalCriteria> conditionalCriteria = new ArrayList<ConditionalCriteria>();
        if (CollectionUtils.isNotEmpty(conditionalCriteriaQuery)) {
            conditionalCriteria.addAll(conditionalCriteriaQuery);
        } else {
            // init
            conditionalCriteria.addAll(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.Variable.class).distinctRoot().build());
        }
        if (variableID != null) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.Variable.class).withProperty(VariableProperties.nameableArtefact().code())
                    .eq(variableID).buildSingle());
        }
        if (familyID != null) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.Variable.class)
                    .withProperty(VariableProperties.families().nameableArtefact().code()).eq(familyID).buildSingle());
        }
        // Find
        PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> entitiesPagedResult = codesService.findVariablesByCondition(ctx, conditionalCriteria, pagingParameter);
        return entitiesPagedResult;
    }

    private PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistFamily> findCodelistFamiliesCore(String codelistFamilyID, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        List<ConditionalCriteria> conditionalCriteria = new ArrayList<ConditionalCriteria>();
        if (CollectionUtils.isNotEmpty(conditionalCriteriaQuery)) {
            conditionalCriteria.addAll(conditionalCriteriaQuery);
        } else {
            // init
            conditionalCriteria.addAll(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.CodelistFamily.class).distinctRoot().build());
        }
        if (codelistFamilyID != null) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.CodelistFamily.class)
                    .withProperty(CodelistFamilyProperties.nameableArtefact().code()).eq(codelistFamilyID).buildSingle());
        }
        // Find
        PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistFamily> entitiesPagedResult = codesService.findCodelistFamiliesByCondition(ctx, conditionalCriteria, pagingParameter);
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
        checkParameterNotWildcardAll(RestInternalConstants.PARAMETER_AGENCY_ID, agencyID);
    }

    private void checkParameterNotWildcardFindItemSchemes(String agencyID, String resourceID) {
        checkParameterNotWildcardAll(RestInternalConstants.PARAMETER_RESOURCE_ID, resourceID);
    }

    private void checkParameterNotWildcardRetrieveItemScheme(String agencyID, String resourceID, String version) {
        checkParameterNotWildcardAll(RestInternalConstants.PARAMETER_AGENCY_ID, agencyID);
        checkParameterNotWildcardAll(RestInternalConstants.PARAMETER_RESOURCE_ID, resourceID);
        checkParameterNotWildcardAll(RestInternalConstants.PARAMETER_VERSION, version);
    }

    private void checkParameterNotWildcardFindItems(String agencyID, String resourceID, String version) {
        // nothing
    }

    private void checkParameterNotWildcardRetrieveItem(String agencyID, String resourceID, String version, String itemID, String parameterNameItemID) {
        checkParameterNotWildcardAll(RestInternalConstants.PARAMETER_AGENCY_ID, agencyID);
        checkParameterNotWildcardAll(RestInternalConstants.PARAMETER_RESOURCE_ID, resourceID);
        checkParameterNotWildcardAll(RestInternalConstants.PARAMETER_VERSION, version);
        checkParameterNotWildcardAll(parameterNameItemID, itemID);
    }

    /**
     * Check unsupported wildcards in some parameters
     */
    private void checkParameterNotWildcardAll(String parameterName, String parameterValue) {
        if (RestInternalConstants.WILDCARD.equals(parameterValue)) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.PARAMETER_INCORRECT, parameterName);
            throw new RestException(exception, Status.BAD_REQUEST);
        }
    }
}