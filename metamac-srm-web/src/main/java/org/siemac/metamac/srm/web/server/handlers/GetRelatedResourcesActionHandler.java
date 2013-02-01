package org.siemac.metamac.srm.web.server.handlers;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.server.utils.MetamacCriteriaUtils;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesAction;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.constants.CommonSharedConstants;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetRelatedResourcesActionHandler extends SecurityActionHandler<GetRelatedResourcesAction, GetRelatedResourcesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetRelatedResourcesActionHandler() {
        super(GetRelatedResourcesAction.class);
    }

    @Override
    public GetRelatedResourcesResult executeSecurityAction(GetRelatedResourcesAction action) throws ActionException {
        try {
            MetamacCriteriaResult<RelatedResourceDto> result = null;

            MetamacCriteria criteria = new MetamacCriteria();
            criteria.setPaginator(new MetamacCriteriaPaginator());
            criteria.getPaginator().setFirstResult(action.getFirstResult());
            criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
            criteria.getPaginator().setCountTotalResults(true);

            switch (action.getStructuralResourcesRelationEnum()) {
                case CONCEPT_SCHEMES_WITH_DSD_PRIMARY_MEASURE: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdPrimaryMeasureByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
                            conceptSchemeWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPTS_WITH_DSD_PRIMARY_MEASURE: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();
                    restriction.getRestrictions().addAll(MetamacCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    criteria.setRestriction(restriction);
                    result = srmCoreServiceFacade.findConceptsCanBeDsdPrimaryMeasureByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria, conceptWebCriteria.getDsdUrn());
                    break;
                }
                case CODELIST_WITH_DSD_PRIMARY_MEASURE_ENUMERATED_REPRESENTATION: {
                    // TODO
                    break;
                }
                case CONCEPT_SCHEMES_WITH_DSD_DIMENSION: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
                            conceptSchemeWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPT_WITH_DSD_DIMENSION: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();
                    restriction.getRestrictions().addAll(MetamacCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    criteria.setRestriction(restriction);
                    result = srmCoreServiceFacade.findConceptsCanBeDsdDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria, conceptWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPT_SCHEMES_WITH_DSD_MEASURE_DIMENSION: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdMeasureDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
                            conceptSchemeWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPT_WITH_DSD_MEASURE_DIMENSION: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();
                    restriction.getRestrictions().addAll(MetamacCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    criteria.setRestriction(restriction);
                    result = srmCoreServiceFacade.findConceptsCanBeDsdMeasureDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria, conceptWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPT_SCHEMES_WITH_DSD_TIME_DIMENSION: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdTimeDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
                            conceptSchemeWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPT_WITH_DSD_TIME_DIMENSION: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();
                    restriction.getRestrictions().addAll(MetamacCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    criteria.setRestriction(restriction);
                    result = srmCoreServiceFacade.findConceptsCanBeDsdTimeDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria, conceptWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPT_SCHEME_WITH_DSD_MEASURE_DIMENSION_ENUMERATED_REPRESENTATION: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesCanBeEnumeratedRepresentationForDsdMeasureDimension(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    break;
                }
                case CODELIST_WITH_DSD_DIMENSION_ENUMERATED_REPRESENTATION: {
                    CodelistWebCriteria codelistWebCriteria = (CodelistWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacCriteriaUtils.getCodelistCriteriaRestriction(codelistWebCriteria));
                    result = srmCoreServiceFacade.findCodelistsCanBeEnumeratedRepresentationForDsdDimension(ServiceContextHolder.getCurrentServiceContext(), criteria,
                            codelistWebCriteria.getConceptUrn());
                    break;
                }
                default:
                    throw new MetamacWebException(CommonSharedConstants.EXCEPTION_UNKNOWN, MetamacSrmWeb.getCoreMessages().exception_common_unknown());
            }
            return new GetRelatedResourcesResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
