package org.siemac.metamac.srm.web.server.handlers;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.server.utils.MetamacWebCriteriaUtils;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesAction;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.VariableElementWebCriteria;
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
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdPrimaryMeasureByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
                            conceptSchemeWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPT_WITH_DSD_PRIMARY_MEASURE: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    result = srmCoreServiceFacade.findConceptsCanBeDsdPrimaryMeasureByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria, conceptWebCriteria.getDsdUrn());
                    break;
                }
                case CODELIST_WITH_DSD_PRIMARY_MEASURE_ENUMERATED_REPRESENTATION: {
                    CodelistWebCriteria codelistWebCriteria = (CodelistWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getCodelistCriteriaRestriction(codelistWebCriteria));
                    result = srmCoreServiceFacade.findCodelistsCanBeEnumeratedRepresentationForDsdPrimaryMeasureByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    break;
                }
                case CONCEPT_SCHEMES_WITH_DSD_DIMENSION: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
                            conceptSchemeWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPT_WITH_DSD_DIMENSION: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    result = srmCoreServiceFacade.findConceptsCanBeDsdDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria, conceptWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPT_SCHEMES_WITH_DSD_MEASURE_DIMENSION: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdMeasureDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
                            conceptSchemeWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPT_WITH_DSD_MEASURE_DIMENSION: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    result = srmCoreServiceFacade.findConceptsCanBeDsdMeasureDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria, conceptWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPT_SCHEMES_WITH_DSD_TIME_DIMENSION: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdTimeDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
                            conceptSchemeWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPT_WITH_DSD_TIME_DIMENSION: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    result = srmCoreServiceFacade.findConceptsCanBeDsdTimeDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria, conceptWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPT_SCHEME_WITH_DSD_MEASURE_DIMENSION_ENUMERATED_REPRESENTATION: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesCanBeEnumeratedRepresentationForDsdMeasureDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    break;
                }
                case CODELIST_WITH_DSD_DIMENSION_ENUMERATED_REPRESENTATION: {
                    CodelistWebCriteria codelistWebCriteria = (CodelistWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getCodelistCriteriaRestriction(codelistWebCriteria));
                    result = srmCoreServiceFacade.findCodelistsCanBeEnumeratedRepresentationForDsdDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
                            codelistWebCriteria.getConceptUrn());
                    break;
                }
                case CONCEPT_SCHEMES_WITH_DSD_ROLES: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdRoleByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    break;
                }
                case CONCEPTS_WITH_DSD_ROLES: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    result = srmCoreServiceFacade.findConceptsCanBeDsdRoleByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    break;
                }
                case CONCEPT_SCHEMES_WITH_DSD_ATTRIBUTE: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdAttributeByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
                            conceptSchemeWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPT_WITH_DSD_ATTRIBUTE: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    result = srmCoreServiceFacade.findConceptsCanBeDsdAttributeByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria, conceptWebCriteria.getDsdUrn());
                    break;
                }
                case CODELIST_WITH_DSD_ATTRIBUTE_ENUMERATED_REPRESENTATION: {
                    CodelistWebCriteria codelistWebCriteria = (CodelistWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getCodelistCriteriaRestriction(codelistWebCriteria));
                    result = srmCoreServiceFacade.findCodelistsCanBeEnumeratedRepresentationForDsdAttributeByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
                            codelistWebCriteria.getConceptUrn());
                    break;
                }
                case CODELIST_WITH_CONCEPT_ENUMERATED_REPRESENTATION: {
                    CodelistWebCriteria codelistWebCriteria = (CodelistWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getCodelistCriteriaRestriction(codelistWebCriteria));
                    result = srmCoreServiceFacade.findCodelistsCanBeEnumeratedRepresentationForConceptByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
                            codelistWebCriteria.getConceptUrn());
                    break;
                }
                case VARIABLE_ELEMENT_WITH_CODE: {
                    VariableElementWebCriteria variableElementWebCriteria = (VariableElementWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getVariableElementCriteriaRestriction(variableElementWebCriteria));
                    result = srmCoreServiceFacade.findVariableElementsForCodesByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria, variableElementWebCriteria.getCodelistUrn());
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
