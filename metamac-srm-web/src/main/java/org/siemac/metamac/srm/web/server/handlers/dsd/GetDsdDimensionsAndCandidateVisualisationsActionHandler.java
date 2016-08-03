package org.siemac.metamac.srm.web.server.handlers.dsd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.shared.CommonSharedUtils;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdDimensionsAndCandidateVisualisationsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdDimensionsAndCandidateVisualisationsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetDsdDimensionsAndCandidateVisualisationsActionHandler extends SecurityActionHandler<GetDsdDimensionsAndCandidateVisualisationsAction, GetDsdDimensionsAndCandidateVisualisationsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetDsdDimensionsAndCandidateVisualisationsActionHandler() {
        super(GetDsdDimensionsAndCandidateVisualisationsAction.class);
    }

    @Override
    public GetDsdDimensionsAndCandidateVisualisationsResult executeSecurityAction(GetDsdDimensionsAndCandidateVisualisationsAction action) throws ActionException {

        try {
            List<DimensionComponentDto> dimensionComponentDtos = new ArrayList<DimensionComponentDto>();
            Map<String, List<RelatedResourceDto>> candidateOrdersByDimension = new HashMap<String, List<RelatedResourceDto>>();
            Map<String, List<RelatedResourceDto>> candidateOpennessLevelsByDimension = new HashMap<String, List<RelatedResourceDto>>();

            List<DescriptorDto> dimensionDescriptorDtos = srmCoreServiceFacade.findDescriptorsForDataStructureDefinition(ServiceContextHolder.getCurrentServiceContext(), action.getDsdUrn(),
                    TypeComponentList.DIMENSION_DESCRIPTOR);
            if (!dimensionDescriptorDtos.isEmpty()) {
                DescriptorDto dimensionDescriptorDto = dimensionDescriptorDtos.get(0);
                dimensionComponentDtos = CommonSharedUtils.getDimensionComponents(dimensionDescriptorDto);

                for (DimensionComponentDto dimensionComponentDto : dimensionComponentDtos) {
                    String dimensionUrn = dimensionComponentDto.getUrn();

                    List<RelatedResourceDto> candidateOrderVisualisations = getCandidateOrderVisualisationsForDimension(dimensionUrn);
                    candidateOrdersByDimension.put(dimensionUrn, candidateOrderVisualisations);

                    List<RelatedResourceDto> candidateOpennessLevelVisualisations = getCandidateOpennessLevelVisualisationsForDimesion(dimensionUrn);
                    candidateOpennessLevelsByDimension.put(dimensionUrn, candidateOpennessLevelVisualisations);
                }
            }
            return new GetDsdDimensionsAndCandidateVisualisationsResult(dimensionComponentDtos, candidateOrdersByDimension, candidateOpennessLevelsByDimension);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    private List<RelatedResourceDto> getCandidateOrderVisualisationsForDimension(String dimensionUrn) throws MetamacException {
        MetamacCriteria criteria = new MetamacCriteria();
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(0);
        criteria.getPaginator().setMaximumResultSize(SrmWebConstants.NO_LIMIT_IN_PAGINATION);
        criteria.getPaginator().setCountTotalResults(true);
        MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findOrderVisualisationCanBeDisplayOrderForDsdDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(),
                criteria, dimensionUrn);
        return result.getResults();
    }

    private List<RelatedResourceDto> getCandidateOpennessLevelVisualisationsForDimesion(String dimensionUrn) throws MetamacException {
        MetamacCriteria criteria = new MetamacCriteria();
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(0);
        criteria.getPaginator().setMaximumResultSize(SrmWebConstants.NO_LIMIT_IN_PAGINATION);
        criteria.getPaginator().setCountTotalResults(true);
        MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findOpennessVisualisationCanBeHierarchylevelopenForDsdDimensionByCondition(
                ServiceContextHolder.getCurrentServiceContext(), criteria, dimensionUrn);
        return result.getResults();
    }
}
