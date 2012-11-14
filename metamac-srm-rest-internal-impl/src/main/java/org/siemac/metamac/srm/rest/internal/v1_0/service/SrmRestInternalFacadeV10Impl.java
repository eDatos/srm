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
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept.ConceptsDo2RestMapperV10;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept.ConceptsRest2DoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return findConceptSchemesCommon(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public ConceptSchemes findConceptSchemes(String agencyID, String query, String orderBy, String limit, String offset) {
        return findConceptSchemesCommon(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public ConceptSchemes findConceptSchemes(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        return findConceptSchemesCommon(agencyID, resourceID, null, query, orderBy, limit, offset);
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

    private ConceptSchemes findConceptSchemesCommon(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = restCriteria2SculptorCriteriaMapper.getConceptSchemeCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Find
            PagedResult<ConceptSchemeVersionMetamac> conceptsSchemesEntitiesResult = findConceptSchemesCore(agencyID, resourceID, version, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            ConceptSchemes conceptSchemes = do2RestInternalMapper.toConceptSchemes(conceptsSchemesEntitiesResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit());
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

        // Only published
        conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().procStatus())
                .in(ProcStatusEnum.INTERNALLY_PUBLISHED, ProcStatusEnum.EXTERNALLY_PUBLISHED).buildSingle());
        // Agency
        if (agencyID != null && !RestInternalConstants.WILDCARD.equals(agencyID)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                    .withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().maintainer().idAsMaintainer()).eq(agencyID).buildSingle());
        }
        // Concept scheme
        if (resourceID != null && !RestInternalConstants.WILDCARD.equals(resourceID)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().code())
                    .eq(resourceID).buildSingle());
        }
        // Version
        if (version != null && !RestInternalConstants.WILDCARD.equals(version)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().versionLogic())
                    .eq(version).buildSingle());
        }

        // Find
        PagedResult<ConceptSchemeVersionMetamac> conceptsSchemesEntitiesResult = conceptsService.findConceptSchemesByCondition(ctx, conditionalCriteria, pagingParameter);
        return conceptsSchemesEntitiesResult;
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