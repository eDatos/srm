package org.siemac.metamac.srm.rest.external.v1_0.service;

import static org.siemac.metamac.srm.rest.external.v1_0.service.utils.SrmRestInternalUtils.hasField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria.Operator;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.common.v1_0.domain.ComparisonOperator;
import org.siemac.metamac.rest.exception.RestCommonServiceExceptionType;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Agencies;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Agency;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.AgencyScheme;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.AgencySchemes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Categorisation;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Categorisations;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategoryScheme;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategorySchemes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CodeCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Codelist;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CodelistFamilies;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CodelistFamily;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Codelists;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Concept;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ConceptTypes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Concepts;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ContentConstraint;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ContentConstraints;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataConsumer;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataConsumerScheme;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataConsumerSchemes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataConsumers;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataProvider;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataProviderScheme;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataProviderSchemes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataProviders;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataStructures;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Organisation;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.OrganisationScheme;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.OrganisationSchemes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.OrganisationUnit;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.OrganisationUnitScheme;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.OrganisationUnitSchemes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.OrganisationUnits;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Organisations;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.RegionReference;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Variable;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableElement;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableElements;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableElementsGeoInfo;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableFamilies;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableFamily;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Variables;
import org.siemac.metamac.srm.core.base.serviceapi.MiscMetamacService;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacProperties;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacResultSelection;
import org.siemac.metamac.srm.core.code.domain.CodelistFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.VariableElementProperties;
import org.siemac.metamac.srm.core.code.domain.VariableElementResult;
import org.siemac.metamac.srm.core.code.domain.VariableElementResultSelection;
import org.siemac.metamac.srm.core.code.domain.VariableFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.VariableProperties;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.GeneratorUrnUtils;
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
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.external.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.category.CategoriesDo2RestMapperV10;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.category.CategoriesRest2DoMapper;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.code.CodesDo2RestMapperV10;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.code.CodesRest2DoMapper;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.concept.ConceptsDo2RestMapperV10;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.concept.ConceptsRest2DoMapper;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.constraint.ContentConstraintsDo2RestMapperV10;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.constraint.ContentConstraintsRest2DoMapper;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.dsd.DataStructuresDo2RestMapperV10;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.dsd.DataStructuresRest2DoMapper;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.organisation.OrganisationsDo2RestMapperV10;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.organisation.OrganisationsRest2DoMapper;
import org.siemac.metamac.srm.rest.external.v1_0.service.utils.SrmRestInternalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.constraint.domain.RegionValue;
import com.arte.statistic.sdmx.srm.core.constraint.serviceapi.ConstraintsService;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

@Service("srmRestExternalFacadeV10")
public class SrmRestExternalFacadeV10Impl implements SrmRestExternalFacadeV10 {

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
    private ConstraintsService                    constraintsService;

    @Autowired
    private MiscMetamacService                    miscMetamacService;

    @Autowired
    private ConfigurationService                  configurationService;

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
    private ContentConstraintsRest2DoMapper       contentConstraintsRest2DoMapper;

    @Autowired
    private ContentConstraintsDo2RestMapperV10    contentConstraintsDo2RestMapper;

    @Autowired
    private DataStructuresDo2RestMapperV10        dataStructuresDo2RestMapperV10;

    private final ServiceContext                  ctx                      = new ServiceContext("restInternal", "restInternal", "restInternal");
    private final Logger                          logger                   = LoggerFactory.getLogger(SrmRestExternalFacadeV10Impl.class);
    private final PagingParameter                 pagingParameterOneResult = PagingParameter.pageAccess(1, 1, false);

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
            ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemePublished(agencyID, resourceID, version);

            // Transform
            ConceptScheme conceptScheme = conceptsDo2RestMapper.toConceptScheme(conceptSchemeVersion);
            return conceptScheme;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Concepts findConcepts(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            checkParameterNotWildcardFindItems(agencyID, resourceID, version);

            if (mustFindItemsInsteadRetrieveAllItemsOfItemScheme(agencyID, resourceID, version, query, orderBy, limit, offset)) {
                // Find. Retrieve concepts paginated
                SculptorCriteria sculptorCriteria = conceptsRest2DoMapper.getConceptCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);
                PagedResult<ConceptMetamac> entitiesPagedResult = findConceptsCore(agencyID, resourceID, version, null, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

                // Transform
                Concepts concepts = conceptsDo2RestMapper.toConcepts(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit());
                return concepts;
            } else {
                // Retrieve all concepts of conceptScheme, without pagination
                ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemePublished(agencyID, resourceID, version);
                List<ItemResult> items = conceptsService.retrieveConceptsByConceptSchemeUrnUnordered(ctx, conceptSchemeVersion.getMaintainableArtefact().getUrn(), ItemMetamacResultSelection.API);

                // Transform
                Concepts concepts = conceptsDo2RestMapper.toConcepts(items, conceptSchemeVersion);
                return concepts;
            }
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Concept retrieveConcept(String agencyID, String resourceID, String version, String conceptID) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, conceptID, SrmRestConstants.PARAMETER_CONCEPT_ID);

            // Find one
            PagedResult<ConceptMetamac> entitiesPagedResult = findConceptsCore(agencyID, resourceID, version, conceptID, null, pagingParameterOneResult);
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
            CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemePublished(agencyID, resourceID, version);

            // Transform
            CategoryScheme categoryScheme = categoriesDo2RestMapper.toCategoryScheme(categorySchemeVersion);
            return categoryScheme;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Categories findCategories(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            checkParameterNotWildcardFindItems(agencyID, resourceID, version);

            if (mustFindItemsInsteadRetrieveAllItemsOfItemScheme(agencyID, resourceID, version, query, orderBy, limit, offset)) {
                // Find. Retrieve categories paginated
                SculptorCriteria sculptorCriteria = categoriesRest2DoMapper.getCategoryCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);
                PagedResult<CategoryMetamac> entitiesPagedResult = findCategoriesCore(agencyID, resourceID, version, null, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

                // Transform
                Categories categories = categoriesDo2RestMapper.toCategories(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit());
                return categories;
            } else {
                // Retrieve all categories of categoryScheme, without pagination
                CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemePublished(agencyID, resourceID, version);
                List<ItemResult> items = categoriesService
                        .retrieveCategoriesByCategorySchemeUrnUnordered(ctx, categorySchemeVersion.getMaintainableArtefact().getUrn(), ItemMetamacResultSelection.API);

                // Transform
                Categories categories = categoriesDo2RestMapper.toCategories(items, categorySchemeVersion);
                return categories;
            }
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Category retrieveCategory(String agencyID, String resourceID, String version, String categoryID) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, categoryID, SrmRestConstants.PARAMETER_CATEGORY_ID);

            // Find one
            PagedResult<CategoryMetamac> entitiesPagedResult = findCategoriesCore(agencyID, resourceID, version, categoryID, null, pagingParameterOneResult);
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
            PagedResult<com.arte.statistic.sdmx.srm.core.category.domain.Categorisation> entitiesPagedResult = findCategorisationsCore(agencyID, resourceID, version, null, pagingParameterOneResult);
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
            OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveOrganisationSchemePublished(agencyID, resourceID, version);

            // Transform
            OrganisationScheme itemScheme = organisationsDo2RestMapper.toOrganisationScheme(organisationSchemeVersion);
            return itemScheme;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Organisations findOrganisations(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            checkParameterNotWildcardFindItems(agencyID, resourceID, version);

            if (mustFindItemsInsteadRetrieveAllItemsOfItemScheme(agencyID, resourceID, version, query, orderBy, limit, offset)) {
                // Find. Retrieve organisations paginated
                SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);
                PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(null, agencyID, resourceID, version, null, sculptorCriteria.getConditions(),
                        sculptorCriteria.getPagingParameter());

                // Transform
                Organisations organisations = organisationsDo2RestMapper.toOrganisations(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit());
                return organisations;
            } else {
                // Retrieve all organisations of organisationScheme, without pagination
                OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveOrganisationSchemePublished(agencyID, resourceID, version);
                List<ItemResult> items = organisationsService.retrieveOrganisationsByOrganisationSchemeUrnUnordered(ctx, organisationSchemeVersion.getMaintainableArtefact().getUrn(),
                        ItemMetamacResultSelection.API);

                // Transform
                Organisations organisations = organisationsDo2RestMapper.toOrganisations(items, organisationSchemeVersion);
                return organisations;
            }
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Organisation retrieveOrganisation(String agencyID, String resourceID, String version, String organisationID) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, organisationID, SrmRestConstants.PARAMETER_ORGANISATION_ID);

            // Find one
            PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(null, agencyID, resourceID, version, organisationID, null, pagingParameterOneResult);
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
            OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveAgencySchemePublished(agencyID, resourceID, version);

            // Transform
            AgencyScheme itemScheme = organisationsDo2RestMapper.toAgencyScheme(organisationSchemeVersion);
            return itemScheme;
        } catch (Exception e) {
            throw manageException(e);
        }
    }
    @Override
    public Agencies findAgencies(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            checkParameterNotWildcardFindItems(agencyID, resourceID, version);

            if (mustFindItemsInsteadRetrieveAllItemsOfItemScheme(agencyID, resourceID, version, query, orderBy, limit, offset)) {
                // Find. Retrieve organisations paginated
                SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);
                PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.AGENCY, agencyID, resourceID, version, null, sculptorCriteria.getConditions(),
                        sculptorCriteria.getPagingParameter());

                // Transform
                Agencies agencies = organisationsDo2RestMapper.toAgencies(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit());
                return agencies;
            } else {
                // Retrieve all organisations of organisationScheme, without pagination
                OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveAgencySchemePublished(agencyID, resourceID, version);
                List<ItemResult> items = organisationsService.retrieveOrganisationsByOrganisationSchemeUrnUnordered(ctx, organisationSchemeVersion.getMaintainableArtefact().getUrn(),
                        ItemMetamacResultSelection.API);

                // Transform
                Agencies agencies = organisationsDo2RestMapper.toAgencies(items, organisationSchemeVersion);
                return agencies;
            }
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Agency retrieveAgency(String agencyID, String resourceID, String version, String organisationID) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, organisationID, SrmRestConstants.PARAMETER_ORGANISATION_ID);

            // Find one
            PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.AGENCY, agencyID, resourceID, version, organisationID, null, pagingParameterOneResult);
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
            OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveOrganisationUnitSchemePublished(agencyID, resourceID, version);

            // Transform
            OrganisationUnitScheme itemScheme = organisationsDo2RestMapper.toOrganisationUnitScheme(organisationSchemeVersion);
            return itemScheme;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public OrganisationUnits findOrganisationUnits(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            checkParameterNotWildcardFindItems(agencyID, resourceID, version);

            if (mustFindItemsInsteadRetrieveAllItemsOfItemScheme(agencyID, resourceID, version, query, orderBy, limit, offset)) {
                // Find. Retrieve organisations paginated
                SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);
                PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.ORGANISATION_UNIT, agencyID, resourceID, version, null,
                        sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

                // Transform
                OrganisationUnits organisationUnits = organisationsDo2RestMapper.toOrganisationUnits(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit());
                return organisationUnits;
            } else {
                // Retrieve all organisations of organisationScheme, without pagination
                OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveOrganisationUnitSchemePublished(agencyID, resourceID, version);
                List<ItemResult> items = organisationsService.retrieveOrganisationsByOrganisationSchemeUrnUnordered(ctx, organisationSchemeVersion.getMaintainableArtefact().getUrn(),
                        ItemMetamacResultSelection.API);

                // Transform
                OrganisationUnits organisationUnits = organisationsDo2RestMapper.toOrganisationUnits(items, organisationSchemeVersion);
                return organisationUnits;
            }
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public OrganisationUnit retrieveOrganisationUnit(String agencyID, String resourceID, String version, String organisationID) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, organisationID, SrmRestConstants.PARAMETER_ORGANISATION_ID);

            // Find one
            PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.ORGANISATION_UNIT, agencyID, resourceID, version, organisationID, null,
                    pagingParameterOneResult);
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
            OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveDataProviderSchemePublished(agencyID, resourceID, version);

            // Transform
            DataProviderScheme itemScheme = organisationsDo2RestMapper.toDataProviderScheme(organisationSchemeVersion);
            return itemScheme;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public DataProviders findDataProviders(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            checkParameterNotWildcardFindItems(agencyID, resourceID, version);

            if (mustFindItemsInsteadRetrieveAllItemsOfItemScheme(agencyID, resourceID, version, query, orderBy, limit, offset)) {
                // Find. Retrieve organisations paginated
                SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);
                PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.DATA_PROVIDER, agencyID, resourceID, version, null, sculptorCriteria.getConditions(),
                        sculptorCriteria.getPagingParameter());

                // Transform
                DataProviders dataProviders = organisationsDo2RestMapper.toDataProviders(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit());
                return dataProviders;
            } else {
                // Retrieve all organisations of organisationScheme, without pagination
                OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveDataProviderSchemePublished(agencyID, resourceID, version);
                List<ItemResult> items = organisationsService.retrieveOrganisationsByOrganisationSchemeUrnUnordered(ctx, organisationSchemeVersion.getMaintainableArtefact().getUrn(),
                        ItemMetamacResultSelection.API);

                // Transform
                DataProviders dataProviders = organisationsDo2RestMapper.toDataProviders(items, organisationSchemeVersion);
                return dataProviders;
            }
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public DataProvider retrieveDataProvider(String agencyID, String resourceID, String version, String organisationID) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, organisationID, SrmRestConstants.PARAMETER_ORGANISATION_ID);

            // Find one
            PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.DATA_PROVIDER, agencyID, resourceID, version, organisationID, null,
                    pagingParameterOneResult);
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
            OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveDataConsumerSchemePublished(agencyID, resourceID, version);

            // Transform
            DataConsumerScheme itemScheme = organisationsDo2RestMapper.toDataConsumerScheme(organisationSchemeVersion);
            return itemScheme;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public DataConsumers findDataConsumers(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            checkParameterNotWildcardFindItems(agencyID, resourceID, version);

            if (mustFindItemsInsteadRetrieveAllItemsOfItemScheme(agencyID, resourceID, version, query, orderBy, limit, offset)) {
                // Find. Retrieve organisations paginated
                SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);
                PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.DATA_CONSUMER, agencyID, resourceID, version, null, sculptorCriteria.getConditions(),
                        sculptorCriteria.getPagingParameter());

                // Transform
                DataConsumers dataConsumers = organisationsDo2RestMapper.toDataConsumers(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit());
                return dataConsumers;
            } else {
                // Retrieve all organisations of organisationScheme, without pagination
                OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveDataConsumerSchemePublished(agencyID, resourceID, version);
                List<ItemResult> items = organisationsService.retrieveOrganisationsByOrganisationSchemeUrnUnordered(ctx, organisationSchemeVersion.getMaintainableArtefact().getUrn(),
                        ItemMetamacResultSelection.API);

                // Transform
                DataConsumers dataConsumers = organisationsDo2RestMapper.toDataConsumers(items, organisationSchemeVersion);
                return dataConsumers;
            }
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public DataConsumer retrieveDataConsumer(String agencyID, String resourceID, String version, String organisationID) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, organisationID, SrmRestConstants.PARAMETER_ORGANISATION_ID);

            // Find one
            PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.DATA_CONSUMER, agencyID, resourceID, version, organisationID, null,
                    pagingParameterOneResult);
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
            CodelistVersionMetamac codelistVersion = retrieveCodelistPublished(agencyID, resourceID, version);

            // Transform
            Codelist codelist = codesDo2RestMapper.toCodelist(codelistVersion);
            return codelist;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Codes findCodes(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String order, String openness) {
        try {
            checkParameterNotWildcardFindItems(agencyID, resourceID, version);

            if (isFindByDefaultGeographicalGranularitiesCodelist(agencyID, resourceID, version, query, orderBy, limit, offset)) {
                String codelistUrn = configurationService.retrieveDefaultCodelistGeographicalGranularityUrn();
                String[] codelistUrnSplited = UrnUtils.splitUrnItemScheme(codelistUrn);
                agencyID = codelistUrnSplited[0];
                resourceID = codelistUrnSplited[1];
                version = codelistUrnSplited[2];
                query = null;
                orderBy = null;
                limit = null;
            }

            if (mustFindItemsInsteadRetrieveAllItemsOfItemScheme(agencyID, resourceID, version, query, orderBy, limit, offset)) {
                checkParameterEmpty(SrmRestConstants.PARAMETER_ORDER_ID, order);
                checkParameterEmpty(SrmRestConstants.PARAMETER_OPENNESS_ID, openness);

                // Find. Retrieve codes paginated
                SculptorCriteria sculptorCriteria = codesRest2DoMapper.getCodeCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);
                PagedResult<CodeMetamac> entitiesPagedResult = findCodesCore(agencyID, resourceID, version, null, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

                // Transform
                Codes codes = codesDo2RestMapper.toCodes(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit());
                return codes;
            } else {
                // Retrieve all codes of codelist, without pagination
                CodelistVersionMetamac codelistVersion = retrieveCodelistPublished(agencyID, resourceID, version);
                if (order == null) {
                    order = codelistVersion.getDefaultOrderVisualisation().getNameableArtefact().getCode();
                } else {
                    // check exist
                    retrieveCodelistOrderVisualisation(agencyID, resourceID, version, codelistVersion.getMaintainableArtefact().getUrn(), order);
                }
                if (openness == null) {
                    openness = codelistVersion.getDefaultOpennessVisualisation().getNameableArtefact().getCode();
                } else {
                    // check exist
                    retrieveCodelistOpennessVisualisation(agencyID, resourceID, version, codelistVersion.getMaintainableArtefact().getUrn(), openness);
                }
                List<ItemResult> items = codesService
                        .retrieveCodesByCodelistUrnOrderedInDepth(ctx, codelistVersion.getMaintainableArtefact().getUrn(), CodeMetamacResultSelection.API, order, openness);

                // Transform
                Codes codes = codesDo2RestMapper.toCodes(items, codelistVersion);
                return codes;
            }
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public VariableFamily retrieveVariableFamilyById(String id) {
        try {
            // Find one
            PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily> entitiesPagedResult = findVariableFamiliesCore(id, null, pagingParameterOneResult);
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
            PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> entitiesPagedResult = findVariablesCore(id, null, null, pagingParameterOneResult);
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
    public VariableElement retrieveVariableElementById(String variableID, String resourceID) {
        try {
            checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_VARIABLE_ID, variableID);
            checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_RESOURCE_ID, resourceID);

            // Find one
            PagedResult<org.siemac.metamac.srm.core.code.domain.VariableElement> entitiesPagedResult = findVariableElementsCore(variableID, resourceID, false, null, pagingParameterOneResult);
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.VARIABLE_ELEMENT_NOT_FOUND, resourceID, variableID);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            org.siemac.metamac.srm.core.code.domain.VariableElement variableElementEntity = entitiesPagedResult.getValues().get(0);
            VariableElement variableElement = codesDo2RestMapper.toVariableElement(variableElementEntity);
            return variableElement;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public VariableElements findVariableElements(String variableID, String query, String orderBy, String limit, String offset) {
        try {
            // Retrieve by criteria
            SculptorCriteria sculptorCriteria = codesRest2DoMapper.getVariableElementCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            if (mustFindVariableElementsInsteadRetrieveAllVariableElementsOfVariable(variableID, null, query, sculptorCriteria.getConditions(), orderBy, limit, offset)) {
                // Find. Retrieve variable elements paginated
                PagedResult<org.siemac.metamac.srm.core.code.domain.VariableElement> entitiesPagedResult = findVariableElementsCore(variableID, null, false, sculptorCriteria.getConditions(),
                        sculptorCriteria.getPagingParameter());

                // Transform
                VariableElements variableElements = codesDo2RestMapper.toVariableElements(entitiesPagedResult, variableID, query, orderBy, sculptorCriteria.getLimit());
                return variableElements;
            } else {
                // Retrieve all variable elements of variable, without pagination
                String variableUrn = GeneratorUrnUtils.generateVariableUrn(variableID);
                List<String> variableElementsCodes = null;
                if (query != null) {
                    variableElementsCodes = extractVariableElementCodesIfOnlyQueryByCode(sculptorCriteria.getConditions());
                }
                VariableElementResultSelection selection = new VariableElementResultSelection(); // do not retrieve nothing extra metadata as default
                List<VariableElementResult> entities = codesService.findVariableElementsByVariableEfficiently(ctx, variableUrn, variableElementsCodes, selection);

                // Transform
                VariableElements variableElements = codesDo2RestMapper.toVariableElements(entities, variableID, query);
                return variableElements;
            }
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Response findVariableElementsGeoInfoJson(String variableID, String resourceID, String query, String orderBy, String limit, String offset, String fields) {
        String target = (String) findVariableElementsGeoInfoCommon(variableID, resourceID, query, orderBy, limit, offset, fields, MediaType.APPLICATION_JSON);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(target).build();
    }

    @Override
    public VariableElementsGeoInfo findVariableElementsGeoInfoXml(String variableID, String resourceID, String query, String orderBy, String limit, String offset, String fields) {
        VariableElementsGeoInfo target = (VariableElementsGeoInfo) findVariableElementsGeoInfoCommon(variableID, resourceID, query, orderBy, limit, offset, fields, MediaType.APPLICATION_XML);
        return target;
    }

    @Override
    public CodelistFamily retrieveCodelistFamilyById(String id) {
        try {
            // Find one
            PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistFamily> entitiesPagedResult = findCodelistFamiliesCore(id, null, pagingParameterOneResult);
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
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, codeID, SrmRestConstants.PARAMETER_CODE_ID);

            // Find one
            PagedResult<CodeMetamac> entitiesPagedResult = findCodesCore(agencyID, resourceID, version, codeID, null, pagingParameterOneResult);
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
            PagedResult<DataStructureDefinitionVersionMetamac> entitiesPagedResult = findDataStructuresCore(agencyID, resourceID, version, null, pagingParameterOneResult);
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

    @Override
    public ContentConstraints findContentConstraints(String query, String orderBy, String limit, String offset) {
        return findContentConstraints(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public ContentConstraints findContentConstraints(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findContentConstraints(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public ContentConstraints findContentConstraints(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findContentConstraints(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public ContentConstraint retrieveContentConstraint(String agencyID, String resourceID, String version) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            // Find one
            PagedResult<com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint> entitiesPagedResult = findContentConstraintsCore(agencyID, resourceID, version, null,
                    pagingParameterOneResult);
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CONTENT_CONSTRAINT_NOT_FOUND, resourceID, version, agencyID);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            ContentConstraint contentConstraint = contentConstraintsDo2RestMapper.toContentConstraint(entitiesPagedResult.getValues().get(0));
            return contentConstraint;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public RegionReference retrieveRegionForContentConstraint(String agencyID, String resourceID, String version, String regionCode) {
        try {
            String contentConstraintUrn = GeneratorUrnUtils.generateSdmxContentConstraintUrn(new String[]{agencyID}, resourceID, version);

            // Retrieve
            RegionValue regionValue = constraintsService.findRegionValueByUrn(ctx, contentConstraintUrn, regionCode);

            if (regionValue == null) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CONTENT_CONSTRAINT_REGION_NOT_FOUND, regionCode,
                        contentConstraintUrn);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // If not draft is allowed and isn't final, then is an error.
            if (BooleanUtils.isNotTrue(regionValue.getContentConstraint().getMaintainableArtefact().getFinalLogicClient())) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CONTENT_CONSTRAINT_UNPUBLISHED, regionCode,
                        contentConstraintUrn);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            RegionReference result = contentConstraintsDo2RestMapper.toRegionReference(contentConstraintUrn, regionValue);

            return result;
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

    private ConceptSchemeVersionMetamac retrieveConceptSchemePublished(String agencyID, String resourceID, String version) throws MetamacException {
        PagedResult<ConceptSchemeVersionMetamac> entitiesPagedResult = findConceptSchemesCore(agencyID, resourceID, version, null, pagingParameterOneResult);
        if (entitiesPagedResult.getValues().size() != 1) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND, resourceID, version, agencyID);
            throw new RestException(exception, Status.NOT_FOUND);
        }
        return entitiesPagedResult.getValues().get(0);
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
                .maintainableArtefact(), ConceptMetamacProperties.nameableArtefact(), conditionalCriteriaQuery, ConceptMetamac.class);

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

    private CategorySchemeVersionMetamac retrieveCategorySchemePublished(String agencyID, String resourceID, String version) throws MetamacException {
        PagedResult<CategorySchemeVersionMetamac> entitiesPagedResult = findCategorySchemesCore(agencyID, resourceID, version, null, pagingParameterOneResult);
        if (entitiesPagedResult.getValues().size() != 1) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CATEGORY_SCHEME_NOT_FOUND, resourceID, version, agencyID);
            throw new RestException(exception, Status.NOT_FOUND);
        }
        return entitiesPagedResult.getValues().get(0);
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
                .maintainableArtefact(), CategoryMetamacProperties.nameableArtefact(), conditionalCriteriaQuery, CategoryMetamac.class);

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

    private OrganisationSchemeVersionMetamac retrieveOrganisationSchemePublished(String agencyID, String resourceID, String version) throws MetamacException {
        return retrieveOrganisationSchemePublished(null, agencyID, resourceID, version, RestServiceExceptionType.ORGANISATION_SCHEME_NOT_FOUND);
    }
    private OrganisationSchemeVersionMetamac retrieveAgencySchemePublished(String agencyID, String resourceID, String version) throws MetamacException {
        return retrieveOrganisationSchemePublished(OrganisationSchemeTypeEnum.AGENCY_SCHEME, agencyID, resourceID, version, RestServiceExceptionType.AGENCY_SCHEME_NOT_FOUND);
    }
    private OrganisationSchemeVersionMetamac retrieveDataConsumerSchemePublished(String agencyID, String resourceID, String version) throws MetamacException {
        return retrieveOrganisationSchemePublished(OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME, agencyID, resourceID, version, RestServiceExceptionType.DATA_CONSUMER_SCHEME_NOT_FOUND);
    }
    private OrganisationSchemeVersionMetamac retrieveDataProviderSchemePublished(String agencyID, String resourceID, String version) throws MetamacException {
        return retrieveOrganisationSchemePublished(OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME, agencyID, resourceID, version, RestServiceExceptionType.DATA_PROVIDER_SCHEME_NOT_FOUND);
    }
    private OrganisationSchemeVersionMetamac retrieveOrganisationUnitSchemePublished(String agencyID, String resourceID, String version) throws MetamacException {
        return retrieveOrganisationSchemePublished(OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME, agencyID, resourceID, version, RestServiceExceptionType.ORGANISATION_UNIT_SCHEME_NOT_FOUND);
    }
    private OrganisationSchemeVersionMetamac retrieveOrganisationSchemePublished(OrganisationSchemeTypeEnum type, String agencyID, String resourceID, String version,
            RestCommonServiceExceptionType exceptionType) throws MetamacException {
        PagedResult<OrganisationSchemeVersionMetamac> entitiesPagedResult = findOrganisationSchemesCore(type, agencyID, resourceID, version, null, pagingParameterOneResult);
        if (entitiesPagedResult.getValues().size() != 1) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(exceptionType, resourceID, version, agencyID);
            throw new RestException(exception, Status.NOT_FOUND);
        }
        return entitiesPagedResult.getValues().get(0);
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

    private CodelistVersionMetamac retrieveCodelistPublished(String agencyID, String resourceID, String version) throws MetamacException {
        PagedResult<CodelistVersionMetamac> entitiesPagedResult = findCodelistsCore(agencyID, resourceID, version, null, pagingParameterOneResult);
        if (entitiesPagedResult.getValues().size() != 1) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CODELIST_NOT_FOUND, resourceID, version, agencyID);
            throw new RestException(exception, Status.NOT_FOUND);
        }
        return entitiesPagedResult.getValues().get(0);
    }

    private CodelistOrderVisualisation retrieveCodelistOrderVisualisation(String agencyID, String resourceID, String version, String codelistUrn, String order) throws MetamacException {
        try {
            CodelistOrderVisualisation codelistOrderVisualisation = codesService.retrieveCodelistOrderVisualisationByCode(ctx, codelistUrn, order);
            return codelistOrderVisualisation;
        } catch (MetamacException e) {
            if (e.getExceptionItems().size() == 1 && ServiceExceptionType.CODELIST_ORDER_VISUALISATION_NOT_FOUND.getCode().equals(e.getExceptionItems().get(0).getCode())) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CODELIST_ORDER_CONFIGURATION_NOT_FOUND, order, resourceID,
                        version, agencyID);
                throw new RestException(exception, Status.NOT_FOUND);
            } else {
                throw e;
            }
        }
    }

    private CodelistOpennessVisualisation retrieveCodelistOpennessVisualisation(String agencyID, String resourceID, String version, String codelistUrn, String openness) throws MetamacException {
        try {
            CodelistOpennessVisualisation codelistOpennessVisualisation = codesService.retrieveCodelistOpennessVisualisationByCode(ctx, codelistUrn, openness);
            return codelistOpennessVisualisation;
        } catch (MetamacException e) {
            if (e.getExceptionItems().size() == 1 && ServiceExceptionType.CODELIST_OPENNESS_VISUALISATION_NOT_FOUND.getCode().equals(e.getExceptionItems().get(0).getCode())) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CODELIST_OPENNESS_CONFIGURATION_NOT_FOUND, openness,
                        resourceID, version, agencyID);
                throw new RestException(exception, Status.NOT_FOUND);
            } else {
                throw e;
            }
        }
    }

    private PagedResult<CodelistVersionMetamac> findCodelistsCore(String agencyID, String resourceID, String version, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find item schemes by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItemSchemes(agencyID, resourceID, version, conditionalCriteriaQuery, CodelistVersionMetamac.class);

        // Only Codelists with access-type = PUBLIC, are public. But in LLCC-75, the codelist that are access-type = RESTRICTED never were marked as publicLogic=true,
        // for this is unnecessary to check this condition.
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
                .itemSchemeVersion().maintainableArtefact(), OrganisationMetamacProperties.nameableArtefact(), conditionalCriteriaQuery, OrganisationMetamac.class);
        if (type != null) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(OrganisationMetamac.class).withProperty(OrganisationMetamacProperties.organisationType()).eq(type).buildSingle());
        }

        // Find
        PagedResult<OrganisationMetamac> entitiesPagedResult = organisationsService.findOrganisationsByCondition(ctx, conditionalCriteria, pagingParameter);
        return entitiesPagedResult;
    }

    private PagedResult<org.siemac.metamac.srm.core.code.domain.VariableElement> findVariableElementsCore(String variableID, String resourceID, boolean onlyGeographical,
            List<ConditionalCriteria> conditionalCriteriaQuery, PagingParameter pagingParameter) throws MetamacException {

        List<ConditionalCriteria> conditionalCriteria = new ArrayList<ConditionalCriteria>();
        if (CollectionUtils.isNotEmpty(conditionalCriteriaQuery)) {
            conditionalCriteria.addAll(conditionalCriteriaQuery);
        } else {
            // init
            conditionalCriteria.addAll(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.VariableElement.class).distinctRoot().build());
        }
        if (variableID != null && !SrmRestConstants.WILDCARD_ALL.equals(variableID)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.VariableElement.class)
                    .withProperty(VariableElementProperties.variable().nameableArtefact().code()).eq(variableID).buildSingle());
        }
        if (onlyGeographical) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.VariableElement.class).withProperty(VariableElementProperties.variable().type())
                    .eq(VariableTypeEnum.GEOGRAPHICAL).buildSingle());
        }
        if (resourceID != null && !SrmRestConstants.WILDCARD_ALL.equals(resourceID)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.VariableElement.class)
                    .withProperty(VariableElementProperties.identifiableArtefact().code()).eq(resourceID).buildSingle());
        }
        // Find
        PagedResult<org.siemac.metamac.srm.core.code.domain.VariableElement> entitiesPagedResult = codesService.findVariableElementsByCondition(ctx, conditionalCriteria, pagingParameter);
        return entitiesPagedResult;
    }

    private ContentConstraints findContentConstraints(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = contentConstraintsRest2DoMapper.getContentConstraintCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Find
            PagedResult<com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint> entitiesPagedResult = findContentConstraintsCore(agencyID, resourceID, version,
                    sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            ContentConstraints contentConstraints = contentConstraintsDo2RestMapper.toContentConstraints(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
            return contentConstraints;
        } catch (Exception e) {
            throw manageException(e);
        }
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

    private PagedResult<com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint> findContentConstraintsCore(String agencyID, String resourceID, String version,
            List<ConditionalCriteria> conditionalCriteriaQuery, PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaContentConstraints(agencyID, resourceID, version, conditionalCriteriaQuery,
                ContentConstraint.class);

        // Find
        PagedResult<com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint> entitiesPagedResult = constraintsService.findContentConstraintsByCondition(ctx, conditionalCriteria,
                pagingParameter);

        return entitiesPagedResult;
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
        if (familyID != null && !SrmRestConstants.WILDCARD_ALL.equals(familyID)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.Variable.class)
                    .withProperty(VariableProperties.families().nameableArtefact().code()).eq(familyID).buildSingle());
        }
        // Find
        PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> entitiesPagedResult = codesService.findVariablesByCondition(ctx, conditionalCriteria, pagingParameter);
        return entitiesPagedResult;
    }

    private PagedResult<CodeMetamac> findCodesCore(String agencyID, String resourceID, String version, String codeID, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find items by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItems(agencyID, resourceID, version, codeID, CodeMetamacProperties.itemSchemeVersion()
                .maintainableArtefact(), CodeMetamacProperties.nameableArtefact(), conditionalCriteriaQuery, CodeMetamac.class);

        // Only Codelists with access-type = PUBLIC, are public. But in LLCC-75, the codelist that are access-type = RESTRICTED never were marked as publicLogic=true,
        // for this is unnecessary to check this condition.
        // For the above comment the following conditions is irrelevant:
        // Only codelists with access == public
        conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class)
                .withProperty(new LeafProperty<CodeMetamac>(CodeMetamacProperties.itemSchemeVersion().getName(), CodelistVersionMetamacProperties.accessType().getName(), false, CodeMetamac.class))
                .eq(AccessTypeEnum.PUBLIC).buildSingle());

        // Find
        PagedResult<CodeMetamac> entitiesPagedResult = codesService.findCodesByCondition(ctx, conditionalCriteria, pagingParameter);
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

    private Object findVariableElementsGeoInfoCommon(String variableID, String resourceID, String query, String orderBy, String limit, String offset, String fields, String mediaType) {
        try {
            // Build criteria and selection
            SculptorCriteria sculptorCriteria = codesRest2DoMapper.getVariableElementCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            VariableElementResultSelection selection = new VariableElementResultSelection();
            selection.setReturnOnlyGeographicalVariableElements(true);
            selection.setLongitudeLatitude(!hasField(fields, SrmRestConstants.FIELD_EXCLUDE_LONGITUDE_LATITUDE));
            selection.setGeographicalGranularity(!hasField(fields, SrmRestConstants.FIELD_EXCLUDE_GEOGRAPHICAL_GRANULARITY));
            if (MediaType.APPLICATION_JSON.equals(mediaType)) {
                selection.setShapeGeojson(!hasField(fields, SrmRestConstants.FIELD_EXCLUDE_GEOMETRY));
            }
            if (MediaType.APPLICATION_XML.equals(mediaType)) {
                selection.setShapeWkt(!hasField(fields, SrmRestConstants.FIELD_EXCLUDE_GEOMETRY));
            }

            Object target = null;
            DateTime lastUpdatedDateVariableElementsGeoInfo = retrieveLastUpdatedDateVariableElementsGeographicalInformation();
            if (mustFindVariableElementsInsteadRetrieveAllVariableElementsOfVariable(variableID, resourceID, query, sculptorCriteria.getConditions(), orderBy, limit, offset)) {
                // Find. Retrieve variable elements paginated
                PagedResult<org.siemac.metamac.srm.core.code.domain.VariableElement> entitiesPagedResult = findVariableElementsCore(variableID, resourceID, true, sculptorCriteria.getConditions(),
                        sculptorCriteria.getPagingParameter());

                // Transform
                if (MediaType.APPLICATION_JSON.equals(mediaType)) {
                    target = codesDo2RestMapper.toVariableElementsGeoJson(entitiesPagedResult, selection, lastUpdatedDateVariableElementsGeoInfo);
                } else if (MediaType.APPLICATION_XML.equals(mediaType)) {
                    target = codesDo2RestMapper.toVariableElementsGeoXml(entitiesPagedResult, selection, lastUpdatedDateVariableElementsGeoInfo);
                }
            } else {
                // Retrieve all variable elements of variable, without pagination
                String variableUrn = GeneratorUrnUtils.generateVariableUrn(variableID);
                List<String> variableElementsCodes = null;
                if (query != null) {
                    variableElementsCodes = extractVariableElementCodesIfOnlyQueryByCode(sculptorCriteria.getConditions());
                }

                List<VariableElementResult> entities = codesService.findVariableElementsByVariableEfficiently(ctx, variableUrn, variableElementsCodes, selection);

                // Transform
                if (MediaType.APPLICATION_JSON.equals(mediaType)) {
                    target = codesDo2RestMapper.toVariableElementsGeoJson(entities, selection, lastUpdatedDateVariableElementsGeoInfo);
                } else if (MediaType.APPLICATION_XML.equals(mediaType)) {
                    target = codesDo2RestMapper.toVariableElementsGeoXml(entities, selection, lastUpdatedDateVariableElementsGeoInfo);
                }
            }
            return target;
        } catch (Exception e) {
            throw manageException(e);
        }
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
        checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_AGENCY_ID, agencyID);
    }

    private void checkParameterNotWildcardFindItemSchemes(String agencyID, String resourceID) {
        checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_RESOURCE_ID, resourceID);
    }

    private void checkParameterNotWildcardRetrieveItemScheme(String agencyID, String resourceID, String version) {
        checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_AGENCY_ID, agencyID);
        checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_RESOURCE_ID, resourceID);
        checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_VERSION, version);
    }

    private void checkParameterNotWildcardFindItems(String agencyID, String resourceID, String version) {
        // nothing
    }

    private void checkParameterNotWildcardRetrieveItem(String agencyID, String resourceID, String version, String itemID, String parameterNameItemID) {
        checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_AGENCY_ID, agencyID);
        checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_RESOURCE_ID, resourceID);
        checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_VERSION, version);
        checkParameterNotWildcardAll(parameterNameItemID, itemID);
    }

    /**
     * Check unsupported wildcards in some parameters
     */
    private void checkParameterNotWildcardAll(String parameterName, String parameterValue) {
        if (SrmRestConstants.WILDCARD_ALL.equals(parameterValue)) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.PARAMETER_INCORRECT, parameterName);
            throw new RestException(exception, Status.BAD_REQUEST);
        }
    }

    /**
     * Check parameter is empty
     */
    private void checkParameterEmpty(String parameterName, String parameterValue) {
        if (!StringUtils.isBlank(parameterValue)) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.PARAMETER_INCORRECT, parameterName);
            throw new RestException(exception, Status.BAD_REQUEST);
        }
    }

    private boolean mustFindItemsInsteadRetrieveAllItemsOfItemScheme(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        if (SrmRestConstants.WILDCARD_ALL.equals(agencyID) || SrmRestConstants.WILDCARD_ALL.equals(resourceID) || SrmRestConstants.WILDCARD_ALL.equals(version)) {
            return true;
        }
        if (query != null || orderBy != null || limit != null || offset != null) {
            return true;
        }
        // can retrieve all items of itemScheme
        return false;
    }

    private boolean mustFindVariableElementsInsteadRetrieveAllVariableElementsOfVariable(String variableID, String variableElementID, String query, List<ConditionalCriteria> queries, String orderBy,
            String limit, String offset) {
        if (variableID == null || SrmRestConstants.WILDCARD_ALL.equals(variableID)) {
            // only when it is a specific variable
            return true;
        }
        if (variableElementID != null && !SrmRestConstants.WILDCARD_ALL.equals(variableElementID)) {
            // retrieving only one. do not execute query
            return true;
        }
        if (orderBy != null || limit != null || offset != null) {
            return true;
        }
        if (query != null && !isQueryOnlyByVariableElementCodes(queries)) {
            return true;
        }
        // can retrieve all
        return false;
    }

    /**
     * If query != null, query must have three sentences to can find all variable elements: order, distinctRoot and code (id)
     */
    private boolean isQueryOnlyByVariableElementCodes(List<ConditionalCriteria> queries) {
        if (queries.size() != 3) {
            return false;
        }
        ConditionalCriteria query = extractQueryVariableElementWhenOnlyQueryByCode(queries);
        if (!query.getPropertyFullName().equals(VariableElementProperties.identifiableArtefact().code().getName())) {
            return false;
        }
        if (!query.getOperator().equals(Operator.In) && !query.getOperator().equals(Operator.Equal)) {
            return false;
        }
        return true;
    }

    /**
     * Must be [API]/codelists/~all/~all/~all/codes?query=DEFAULT_GEOGRAPHICAL_GRANULARITIES_CODELIST EQ TRUE
     */
    private boolean isFindByDefaultGeographicalGranularitiesCodelist(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        if (!SrmRestConstants.WILDCARD_ALL.equals(agencyID) || !SrmRestConstants.WILDCARD_ALL.equals(resourceID) || !SrmRestConstants.WILDCARD_ALL.equals(version)) {
            return false;
        }
        if (query == null || orderBy != null || limit != null || offset != null) {
            return false;
        }
        return query.equalsIgnoreCase(CodeCriteriaPropertyRestriction.DEFAULT_GEOGRAPHICAL_GRANULARITIES_CODELIST + " " + ComparisonOperator.EQ.value() + " 'TRUE'");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private List<String> extractVariableElementCodesIfOnlyQueryByCode(List<ConditionalCriteria> queries) {
        ConditionalCriteria query = extractQueryVariableElementWhenOnlyQueryByCode(queries);
        if (query.getOperator().equals(Operator.In)) {
            return (List) query.getFirstOperant();
        } else if (query.getOperator().equals(Operator.Equal)) {
            return Arrays.asList((String) query.getFirstOperant());
        }
        return null;
    }

    private ConditionalCriteria extractQueryVariableElementWhenOnlyQueryByCode(List<ConditionalCriteria> queries) {
        return queries.get(1);
    }

    private DateTime retrieveLastUpdatedDateVariableElementsGeographicalInformation() throws MetamacException {
        return miscMetamacService.findLastUpdatedVariableElementsGeographicalInformation(ctx);
    }
}