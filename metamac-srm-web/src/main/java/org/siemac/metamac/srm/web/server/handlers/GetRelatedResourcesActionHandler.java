package org.siemac.metamac.srm.web.server.handlers;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.server.utils.MetamacWebCriteriaUtils;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesAction;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CategoryWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CodeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CodelistOpennessLevelVisualisationWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CodelistOrderVisualisationWebCriteria;
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

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategoryRelatedResourceDto;
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
                            codelistWebCriteria.getConceptUrn(), codelistWebCriteria.getVariableUrn());
                    break;
                }
                case VARIABLE_ELEMENT_WITH_CODE: {
                    VariableElementWebCriteria variableElementWebCriteria = (VariableElementWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getVariableElementCriteriaRestriction(variableElementWebCriteria));
                    result = srmCoreServiceFacade.findVariableElementsForCodesByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria, variableElementWebCriteria.getCodelistUrn());
                    break;
                }
                case CODELIST_ORDER_FOR_DSD_DIMENSION: {
                    CodelistOrderVisualisationWebCriteria codelistOrderVisualisationWebCriteria = (CodelistOrderVisualisationWebCriteria) action.getCriteria();
                    // no criteria
                    result = srmCoreServiceFacade.findOrderVisualisationCanBeDisplayOrderForDsdDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
                            codelistOrderVisualisationWebCriteria.getDsdDimensionUrn());
                    break;
                }
                case CODELIST_OPENNESS_LEVEL_FOR_DSD_DIMENSION: {
                    CodelistOpennessLevelVisualisationWebCriteria codelistOpennessLevelVisualisationWebCriteria = (CodelistOpennessLevelVisualisationWebCriteria) action.getCriteria();
                    // no criteria
                    result = srmCoreServiceFacade.findOpennessVisualisationCanBeHierarchylevelopenForDsdDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
                            codelistOpennessLevelVisualisationWebCriteria.getDsdDimensionUrn());
                    break;
                }
                case CODELIST_WITH_QUANTITY_UNIT: {
                    CodelistWebCriteria codelistWebCriteria = (CodelistWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getCodelistCriteriaRestriction(codelistWebCriteria));
                    result = srmCoreServiceFacade.findCodelistsByConditionWithCodesCanBeQuantityUnit(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    break;
                }
                case CODE_WITH_QUANTITY_UNIT: {
                    CodeWebCriteria codeWebCriteria = (CodeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getCodeCriteriaRestriction(codeWebCriteria));
                    result = srmCoreServiceFacade.findCodesCanBeQuantityUnit(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    break;
                }
                case CONCEPT_SCHEME_WITH_QUANTITY_NUMERATOR: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesByConditionWithConceptsCanBeQuantityNumerator(ServiceContextHolder.getCurrentServiceContext(),
                            conceptSchemeWebCriteria.getRelatedConceptSchemeUrn(), criteria);
                    break;
                }
                case CONCEPT_WITH_QUANTITY_NUMERATOR: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    result = srmCoreServiceFacade.findConceptsCanBeQuantityNumerator(ServiceContextHolder.getCurrentServiceContext(), conceptWebCriteria.getItemSchemeUrn(), criteria);
                    break;
                }
                case CONCEPT_SCHEME_WITH_QUANTITY_DENOMINATOR: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesByConditionWithConceptsCanBeQuantityDenominator(ServiceContextHolder.getCurrentServiceContext(),
                            conceptSchemeWebCriteria.getRelatedConceptSchemeUrn(), criteria);
                    break;
                }
                case CONCEPT_WITH_QUANTITY_DENOMINATOR: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    result = srmCoreServiceFacade.findConceptsCanBeQuantityDenominator(ServiceContextHolder.getCurrentServiceContext(), conceptWebCriteria.getItemSchemeUrn(), criteria);
                    break;
                }
                case CONCEPT_SCHEME_WITH_QUANTITY_BASE_QUANTITY: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesByConditionWithConceptsCanBeQuantityBaseQuantity(ServiceContextHolder.getCurrentServiceContext(),
                            conceptSchemeWebCriteria.getRelatedConceptSchemeUrn(), criteria);
                    break;
                }
                case CONCEPT_WITH_QUANTITY_BASE_QUANTITY: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    result = srmCoreServiceFacade.findConceptsCanBeQuantityBaseQuantity(ServiceContextHolder.getCurrentServiceContext(), conceptWebCriteria.getItemSchemeUrn(), criteria);
                    break;
                }
                case CONCEPT_SCHEME_WITH_CONCEPT_ENUMERATED_REPRESENTATION: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesCanBeEnumeratedRepresentationForConcepts(ServiceContextHolder.getCurrentServiceContext(), conceptSchemeWebCriteria.getConceptUrn(),
                            criteria);
                    break;
                }
                case CODELIST_THAT_CAN_BE_REPLACED: {
                    CodelistWebCriteria codelistWebCriteria = (CodelistWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getCodelistCriteriaRestriction(codelistWebCriteria));
                    result = srmCoreServiceFacade.findCodelistsByConditionCanReplaceTo(ServiceContextHolder.getCurrentServiceContext(), codelistWebCriteria.getCodelisUrnToReplaceCodelist(), criteria);
                    break;
                }
                case CATEGORY_SCHEMES_FOR_CATEGORISATIONS: {
                    CategorySchemeWebCriteria categorySchemeWebCriteria = (CategorySchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getCategorySchemeCriteriaRestriction(categorySchemeWebCriteria));
                    result = srmCoreServiceFacade.findCategorySchemesWithCategoriesCanBeCategorisationCategoryByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    break;
                }
                case CATEGORIES_FOR_CATEGORISATIONS: {
                    CategoryWebCriteria categoryWebCriteria = (CategoryWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getCategoryCriteriaRestriction(categoryWebCriteria));
                    MetamacCriteriaResult<CategoryRelatedResourceDto> categoriesResult = srmCoreServiceFacade.findCategoriesCanBeCategorisationCategoryByCondition(
                            ServiceContextHolder.getCurrentServiceContext(), criteria);

                    // Transform the categories result into a related resource result

                    List<CategoryRelatedResourceDto> categories = categoriesResult.getResults();
                    List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>();
                    for (CategoryRelatedResourceDto categoryRelatedResourceDto : categories) {
                        RelatedResourceDto relatedResourceDto = categoryRelatedResourceDto;
                        relatedResourceDto.setCode(categoryRelatedResourceDto.getCodeFull()); // Set the codeFull into the code field
                        relatedResourceDtos.add(relatedResourceDto);
                    }

                    result = new MetamacCriteriaResult<RelatedResourceDto>();
                    result.setPaginatorResult(categoriesResult.getPaginatorResult());
                    result.setResults(relatedResourceDtos);

                    break;
                }
                case CODELIST_WITH_VARIABLE_ELEMENT_GEOGRAPHICAL_GRANULARITY: {
                    CodelistWebCriteria codelistWebCriteria = (CodelistWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getCodelistCriteriaRestriction(codelistWebCriteria));
                    result = srmCoreServiceFacade.findCodelistsByConditionWhoseCodesCanBeVariableElementGeographicalGranularity(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    break;
                }
                case CODES_WITH_VARIABLE_ELEMENT_GEOGRAPHICAL_GRANULARITY: {
                    CodeWebCriteria codeWebCriteria = (CodeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getCodeCriteriaRestriction(codeWebCriteria));
                    result = srmCoreServiceFacade.findCodesByConditionCanBeVariableElementGeographicalGranularity(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    break;
                }
                case CODELIST_WITH_DSD_SPATIAL_DIMENSION_ENUMERATED_REPRESENTATION: {
                    CodelistWebCriteria codelistWebCriteria = (CodelistWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getCodelistCriteriaRestriction(codelistWebCriteria));
                    result = srmCoreServiceFacade.findCodelistsCanBeEnumeratedRepresentationForDsdSpatialDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
                            codelistWebCriteria.getConceptUrn());
                    break;
                }
                case CODELIST_WITH_DSD_SPATIAL_ATTRIBUTE_ENUMERATED_REPRESENTATION: {
                    CodelistWebCriteria codelistWebCriteria = (CodelistWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getCodelistCriteriaRestriction(codelistWebCriteria));
                    result = srmCoreServiceFacade.findCodelistsCanBeEnumeratedRepresentationForDsdSpatialAttributeByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
                            codelistWebCriteria.getConceptUrn());
                    break;
                }
                default:
                    throw new MetamacWebException(CommonSharedConstants.EXCEPTION_UNKNOWN, "An unknown exception has ocurred. Please contact system administrator.");
            }
            return new GetRelatedResourcesResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
