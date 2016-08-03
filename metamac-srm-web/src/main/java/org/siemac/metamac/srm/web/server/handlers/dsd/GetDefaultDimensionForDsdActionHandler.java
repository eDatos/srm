package org.siemac.metamac.srm.web.server.handlers.dsd;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.criteria.ConceptMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.shared.dsd.GetDefaultDimensionForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDefaultDimensionForDsdResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponent;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDimensionComponent;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetDefaultDimensionForDsdActionHandler extends SecurityActionHandler<GetDefaultDimensionForDsdAction, GetDefaultDimensionForDsdResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    @Autowired
    private SrmConfiguration     configurationService;

    public GetDefaultDimensionForDsdActionHandler() {
        super(GetDefaultDimensionForDsdAction.class);
    }

    @Override
    public GetDefaultDimensionForDsdResult executeSecurityAction(GetDefaultDimensionForDsdAction action) throws ActionException {
        DimensionComponentDto dimensionComponentDto = new DimensionComponentDto();
        dimensionComponentDto.setTypeComponent(TypeComponent.DIMENSION_COMPONENT);
        dimensionComponentDto.setTypeDimensionComponent(action.getDimensionType());
        dimensionComponentDto.setSpecialDimensionType(action.getSpecialDimensionType());
        if (TypeDimensionComponent.TIMEDIMENSION.equals(action.getDimensionType())) {
            dimensionComponentDto.setCptIdRef(getDefaultTimeDimensionConcept(action.getDsdUrn()));
        } else if (TypeDimensionComponent.MEASUREDIMENSION.equals(action.getDimensionType())) {
            dimensionComponentDto.setCptIdRef(getDefaultMeasureDimensionConcept(action.getDsdUrn()));
        }
        return new GetDefaultDimensionForDsdResult(dimensionComponentDto);
    }

    private RelatedResourceDto getDefaultTimeDimensionConcept(String dsdUrn) throws MetamacWebException {
        try {
            String defaultConceptUrn = configurationService.retrieveDsdTimeDimensionOrAttributeDefaultConceptIdUrn();
            if (StringUtils.isNotBlank(defaultConceptUrn)) {
                MetamacCriteria criteria = new MetamacCriteria();
                criteria.setPaginator(new MetamacCriteriaPaginator());
                criteria.getPaginator().setFirstResult(0);
                criteria.getPaginator().setMaximumResultSize(SrmWebConstants.NO_LIMIT_IN_PAGINATION);
                criteria.getPaginator().setCountTotalResults(true);
                criteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.URN.name(), defaultConceptUrn, OperationType.EQ));

                MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptsCanBeDsdTimeDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria, dsdUrn);
                if (result.getResults() != null && !result.getResults().isEmpty()) {
                    return result.getResults().get(0);
                }
            }
            return null;
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    private RelatedResourceDto getDefaultMeasureDimensionConcept(String dsdUrn) throws MetamacWebException {
        try {
            String defaultConceptUrn = configurationService.retrieveDsdMeasureDimensionOrAttributeDefaultConceptIdUrn();
            if (StringUtils.isNotBlank(defaultConceptUrn)) {
                MetamacCriteria criteria = new MetamacCriteria();
                criteria.setPaginator(new MetamacCriteriaPaginator());
                criteria.getPaginator().setFirstResult(0);
                criteria.getPaginator().setMaximumResultSize(SrmWebConstants.NO_LIMIT_IN_PAGINATION);
                criteria.getPaginator().setCountTotalResults(true);
                criteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.URN.name(), defaultConceptUrn, OperationType.EQ));

                MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptsCanBeDsdMeasureDimensionByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
                        dsdUrn);
                if (result.getResults() != null && !result.getResults().isEmpty()) {
                    return result.getResults().get(0);
                }
            }
            return null;
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
