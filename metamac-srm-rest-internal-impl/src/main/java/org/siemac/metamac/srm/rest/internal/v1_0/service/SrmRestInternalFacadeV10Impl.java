package org.siemac.metamac.srm.rest.internal.v1_0.service;

import java.util.ArrayList;
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
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptTypes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concepts;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept.ConceptsDo2RestMapperV10;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept.ConceptsRest2DoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefactProperties.MaintainableArtefactProperty;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefactProperties.NameableArtefactProperty;

@Service("srmRestInternalFacadeV10")
public class SrmRestInternalFacadeV10Impl implements SrmRestInternalFacadeV10 {

    @Autowired
    private ConceptsMetamacService   conceptsService;

    @Autowired
    private ConceptsRest2DoMapper    restCriteria2SculptorCriteriaMapper;

    @Autowired
    private ConceptsDo2RestMapperV10 do2RestInternalMapper;

    private ServiceContext           ctx    = new ServiceContext("restInternal", "restInternal", "restInternal");
    private Logger                   logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public ConceptSchemes findConceptSchemes(String query, String orderBy, String limit, String offset) {
        return findConceptSchemesCommon(null, null, query, orderBy, limit, offset);
    }

    @Override
    public ConceptSchemes findConceptSchemes(String agencyID, String query, String orderBy, String limit, String offset) {
        return findConceptSchemesCommon(agencyID, null, query, orderBy, limit, offset);
    }

    @Override
    public ConceptSchemes findConceptSchemes(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        return findConceptSchemesCommon(agencyID, resourceID, query, orderBy, limit, offset);
    }

    @Override
    public ConceptScheme retrieveConceptScheme(String agencyID, String resourceID, String version) {
        try {
            // Find one
            PagedResult<ConceptSchemeVersionMetamac> conceptsSchemesEntitiesResult = findConceptSchemesCore(agencyID, resourceID, version, null, PagingParameter.pageAccess(1, 1, false));
            if (conceptsSchemesEntitiesResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND, agencyID, resourceID, version);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            ConceptScheme conceptScheme = do2RestInternalMapper.toConceptScheme(conceptsSchemesEntitiesResult.getValues().get(0));
            return conceptScheme;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Concepts findConcepts(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = restCriteria2SculptorCriteriaMapper.getConceptCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Find
            PagedResult<ConceptMetamac> conceptsEntitiesResult = findConceptsCore(agencyID, resourceID, version, null, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            Concepts concepts = do2RestInternalMapper.toConcepts(conceptsEntitiesResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit());
            return concepts;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Concept retrieveConcept(String agencyID, String resourceID, String version, String conceptID) {
        try {
            // Find one
            PagedResult<ConceptMetamac> conceptsEntitiesResult = findConceptsCore(agencyID, resourceID, version, conceptID, null, PagingParameter.pageAccess(1, 1, false));
            if (conceptsEntitiesResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CONCEPT_NOT_FOUND, conceptID, agencyID, resourceID, version);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            Concept concept = do2RestInternalMapper.toConcept(conceptsEntitiesResult.getValues().get(0));
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
            ConceptTypes conceptTypes = do2RestInternalMapper.toConceptTypes(entitiesResult);
            return conceptTypes;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private ConceptSchemes findConceptSchemesCommon(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = restCriteria2SculptorCriteriaMapper.getConceptSchemeCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Find
            PagedResult<ConceptSchemeVersionMetamac> conceptsSchemesEntitiesResult = findConceptSchemesCore(agencyID, resourceID, null, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            ConceptSchemes conceptSchemes = do2RestInternalMapper.toConceptSchemes(conceptsSchemesEntitiesResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
            return conceptSchemes;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesCore(String agencyID, String resourceID, String version, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find concept schemes by criteria
        List<ConditionalCriteria> conditionalCriteria = new ArrayList<ConditionalCriteria>();
        if (conditionalCriteriaQuery != null) {
            conditionalCriteria.addAll(conditionalCriteriaQuery);
        }

        // Additional constraints
        addConditionalCriteriaPublished(conditionalCriteria, ConceptSchemeVersionMetamac.class, ConceptSchemeVersionMetamacProperties.maintainableArtefact());
        addConditionalCriteriaByAgency(conditionalCriteria, agencyID, ConceptSchemeVersionMetamac.class, ConceptSchemeVersionMetamacProperties.maintainableArtefact());
        addConditionalCriteriaByConceptScheme(conditionalCriteria, resourceID, ConceptSchemeVersionMetamac.class, ConceptSchemeVersionMetamacProperties.maintainableArtefact());
        addConditionalCriteriaByConceptSchemeVersion(conditionalCriteria, version, ConceptSchemeVersionMetamac.class, ConceptSchemeVersionMetamacProperties.maintainableArtefact());

        // Find
        PagedResult<ConceptSchemeVersionMetamac> conceptsSchemesEntitiesResult = conceptsService.findConceptSchemesByCondition(ctx, conditionalCriteria, pagingParameter);
        return conceptsSchemesEntitiesResult;
    }

    private PagedResult<ConceptMetamac> findConceptsCore(String agencyID, String resourceID, String version, String conceptID, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find concepts by criteria
        List<ConditionalCriteria> conditionalCriteria = new ArrayList<ConditionalCriteria>();
        if (conditionalCriteriaQuery != null) {
            conditionalCriteria.addAll(conditionalCriteriaQuery);
        }

        // Additional constraints
        addConditionalCriteriaPublished(conditionalCriteria, ConceptMetamac.class, ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact());
        addConditionalCriteriaByAgency(conditionalCriteria, agencyID, ConceptMetamac.class, ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact());
        addConditionalCriteriaByConceptScheme(conditionalCriteria, resourceID, ConceptMetamac.class, ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact());
        addConditionalCriteriaByConceptSchemeVersion(conditionalCriteria, version, ConceptMetamac.class, ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact());
        addConditionalCriteriaByConcept(conditionalCriteria, conceptID, ConceptMetamac.class, ConceptMetamacProperties.nameableArtefact());

        // Find
        PagedResult<ConceptMetamac> conceptsEntitiesResult = conceptsService.findConceptsByCondition(ctx, conditionalCriteria, pagingParameter);
        return conceptsEntitiesResult;
    }

    /**
     * Internally or externally published
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addConditionalCriteriaPublished(List<ConditionalCriteria> conditionalCriteria, Class entity, MaintainableArtefactProperty maintainableArtefactProperty) {
        conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).withProperty(maintainableArtefactProperty.finalLogic()).eq(Boolean.TRUE).buildSingle());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addConditionalCriteriaByAgency(List<ConditionalCriteria> conditionalCriteria, String agencyID, Class entity, MaintainableArtefactProperty maintainableArtefactProperty) {
        if (agencyID != null && !RestInternalConstants.WILDCARD.equals(agencyID)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).withProperty(maintainableArtefactProperty.maintainer().idAsMaintainer()).eq(agencyID).buildSingle());
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addConditionalCriteriaByConceptScheme(List<ConditionalCriteria> conditionalCriteria, String code, Class entity, MaintainableArtefactProperty maintainableArtefactProperty) {
        if (code != null && !RestInternalConstants.WILDCARD.equals(code)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).withProperty(maintainableArtefactProperty.code()).eq(code).buildSingle());
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addConditionalCriteriaByConceptSchemeVersion(List<ConditionalCriteria> conditionalCriteria, String version, Class entity, MaintainableArtefactProperty maintainableArtefactProperty) {
        if (version != null && !RestInternalConstants.WILDCARD.equals(version)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).withProperty(maintainableArtefactProperty.versionLogic()).eq(version).buildSingle());
        }
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addConditionalCriteriaByConcept(List<ConditionalCriteria> conditionalCriteria, String code, Class entity, NameableArtefactProperty nameableArtefactProperty) {
        if (code != null && !RestInternalConstants.WILDCARD.equals(code)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(entity).withProperty(nameableArtefactProperty.code()).eq(code).buildSingle());
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
}