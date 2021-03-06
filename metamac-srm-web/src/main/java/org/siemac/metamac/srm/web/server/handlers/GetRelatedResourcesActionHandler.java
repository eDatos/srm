package org.siemac.metamac.srm.web.server.handlers;

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
import org.siemac.metamac.srm.web.shared.criteria.ItemWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.VariableElementWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.VersionableResourceWebCriteria;
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

            // Filter by LATEST_FINAL instead of IS_LAST_VERSION. We want the last internally published resources. The search by isLastVersion may give us a wrong result.

            if (action.getCriteria() != null) {
                if (action.getCriteria() instanceof VersionableResourceWebCriteria) {

                    Boolean isLastVersion = ((VersionableResourceWebCriteria) action.getCriteria()).getIsLastVersion();

                    ((VersionableResourceWebCriteria) action.getCriteria()).setIsLatestFinal(isLastVersion);
                    ((VersionableResourceWebCriteria) action.getCriteria()).setIsLastVersion(null);

                } else if (action.getCriteria() instanceof ItemWebCriteria) {

                    Boolean isLastVersion = ((ItemWebCriteria) action.getCriteria()).getIsLastVersion();

                    ((ItemWebCriteria) action.getCriteria()).setIsLatestFinal(isLastVersion);
                    ((ItemWebCriteria) action.getCriteria()).setIsLastVersion(null);
                }
            }

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
                    result = srmCoreServiceFacade.findCodelistsWithCodesCanBeQuantityUnitByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    break;
                }
                case CODE_WITH_QUANTITY_UNIT: {
                    CodeWebCriteria codeWebCriteria = (CodeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getCodeCriteriaRestriction(codeWebCriteria));
                    result = srmCoreServiceFacade.findCodesCanBeQuantityUnitByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    break;
                }
                case CONCEPT_SCHEME_WITH_QUANTITY_NUMERATOR: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeQuantityNumeratorByCondition(ServiceContextHolder.getCurrentServiceContext(),
                            conceptSchemeWebCriteria.getRelatedConceptSchemeUrn(), criteria);
                    break;
                }
                case CONCEPT_WITH_QUANTITY_NUMERATOR: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    result = srmCoreServiceFacade.findConceptsCanBeQuantityNumeratorByCondition(ServiceContextHolder.getCurrentServiceContext(), conceptWebCriteria.getItemSchemeUrn(), criteria);
                    break;
                }
                case CONCEPT_SCHEME_WITH_QUANTITY_DENOMINATOR: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeQuantityDenominatorByCondition(ServiceContextHolder.getCurrentServiceContext(),
                            conceptSchemeWebCriteria.getRelatedConceptSchemeUrn(), criteria);
                    break;
                }
                case CONCEPT_WITH_QUANTITY_DENOMINATOR: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    result = srmCoreServiceFacade.findConceptsCanBeQuantityDenominatorByCondition(ServiceContextHolder.getCurrentServiceContext(), conceptWebCriteria.getItemSchemeUrn(), criteria);
                    break;
                }
                case CONCEPT_SCHEME_WITH_QUANTITY_BASE_QUANTITY: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeQuantityBaseQuantityByCondition(ServiceContextHolder.getCurrentServiceContext(),
                            conceptSchemeWebCriteria.getRelatedConceptSchemeUrn(), criteria);
                    break;
                }
                case CONCEPT_WITH_QUANTITY_BASE_QUANTITY: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    result = srmCoreServiceFacade.findConceptsCanBeQuantityBaseQuantityByCondition(ServiceContextHolder.getCurrentServiceContext(), conceptWebCriteria.getItemSchemeUrn(), criteria);
                    break;
                }
                case CONCEPT_SCHEME_WITH_CONCEPT_ENUMERATED_REPRESENTATION: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesCanBeEnumeratedRepresentationForConceptsByCondition(ServiceContextHolder.getCurrentServiceContext(),
                            conceptSchemeWebCriteria.getConceptUrn(), criteria);
                    break;
                }
                case CODELIST_THAT_CAN_BE_REPLACED: {
                    CodelistWebCriteria codelistWebCriteria = (CodelistWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getCodelistCriteriaRestriction(codelistWebCriteria));
                    result = srmCoreServiceFacade.findCodelistsCanReplaceToByCondition(ServiceContextHolder.getCurrentServiceContext(), codelistWebCriteria.getCodelisUrnToReplaceCodelist(), criteria);
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
                    result = srmCoreServiceFacade.findCategoriesCanBeCategorisationCategoryByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    break;
                }
                case CODELIST_WITH_VARIABLE_ELEMENT_GEOGRAPHICAL_GRANULARITY: {
                    CodelistWebCriteria codelistWebCriteria = (CodelistWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getCodelistCriteriaRestriction(codelistWebCriteria));
                    result = srmCoreServiceFacade.findCodelistsWhoseCodesCanBeVariableElementGeographicalGranularityByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    break;
                }
                case CODES_WITH_VARIABLE_ELEMENT_GEOGRAPHICAL_GRANULARITY: {
                    CodeWebCriteria codeWebCriteria = (CodeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getCodeCriteriaRestriction(codeWebCriteria));
                    result = srmCoreServiceFacade.findCodesCanBeVariableElementGeographicalGranularityByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
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
                case CONCEPT_SCHEME_WITH_DSD_MEASURE_ATTRIBUTE_ENUMERATED_REPRESENTATION: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesCanBeEnumeratedRepresentationForDsdMeasureAttributeByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    break;
                }
                case CONCEPT_SCHEMES_WITH_CONCEPT_ROLE: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeRoleByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    break;
                }
                case CONCEPTS_WITH_CONCEPT_ROLE: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    result = srmCoreServiceFacade.findConceptsCanBeRoleByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    break;
                }
                case CONCEPT_SCHEMES_WITH_CONCEPT_EXTENDS: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeExtendedByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    break;
                }
                case CONCEPT_WITH_CONCEPT_EXTENDS: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    result = srmCoreServiceFacade.findConceptsCanBeExtendedByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    break;
                }
                case CODELIST_WITH_QUANTITY_BASE_LOCATION: {
                    CodelistWebCriteria codelistWebCriteria = (CodelistWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getCodelistCriteriaRestriction(codelistWebCriteria));
                    result = srmCoreServiceFacade.findCodelistsWithCodesCanBeQuantityBaseLocationByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    break;
                }
                case CODE_WITH_QUANTITY_BASE_LOCATION: {
                    CodeWebCriteria codeWebCriteria = (CodeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getCodeCriteriaRestriction(codeWebCriteria));
                    result = srmCoreServiceFacade.findCodesCanBeQuantityBaseLocationByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
                    break;
                }
                case CONCEPT_SCHEMES_WITH_DSD_SPATIAL_DIMENSION: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdSpatialDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
                            conceptSchemeWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPT_WITH_DSD_SPATIAL_DIMENSION: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    result = srmCoreServiceFacade.findConceptsCanBeDsdSpatialDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria, conceptWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPT_SCHEMES_WITH_DSD_SPATIAL_ATTRIBUTE: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdSpatialAttributeByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
                            conceptSchemeWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPT_WITH_DSD_SPATIAL_ATTRIBUTE: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    result = srmCoreServiceFacade.findConceptsCanBeDsdSpatialAttributeByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria, conceptWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPT_SCHEMES_WITH_DSD_MEASURE_ATTRIBUTE: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdMeasureAttributeByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
                            conceptSchemeWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPT_WITH_DSD_MEASURE_ATTRIBUTE: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    result = srmCoreServiceFacade.findConceptsCanBeDsdMeasureAttributeByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria, conceptWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPT_SCHEMES_WITH_DSD_TIME_ATTRIBUTE: {
                    ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptSchemeCriteriaRestriction(conceptSchemeWebCriteria));
                    result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdTimeAttributeByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
                            conceptSchemeWebCriteria.getDsdUrn());
                    break;
                }
                case CONCEPT_WITH_DSD_TIME_ATTRIBUTE: {
                    ConceptWebCriteria conceptWebCriteria = (ConceptWebCriteria) action.getCriteria();
                    criteria.setRestriction(MetamacWebCriteriaUtils.getConceptCriteriaRestriction(conceptWebCriteria));
                    result = srmCoreServiceFacade.findConceptsCanBeDsdTimeAttributeByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria, conceptWebCriteria.getDsdUrn());
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
