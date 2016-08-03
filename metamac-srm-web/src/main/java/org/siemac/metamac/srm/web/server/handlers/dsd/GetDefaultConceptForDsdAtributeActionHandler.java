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
import org.siemac.metamac.srm.web.shared.dsd.GetDefaultConceptForDsdAtributeAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDefaultConceptForDsdAtributeResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.SpecialAttributeTypeEnum;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetDefaultConceptForDsdAtributeActionHandler extends SecurityActionHandler<GetDefaultConceptForDsdAtributeAction, GetDefaultConceptForDsdAtributeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    @Autowired
    private SrmConfiguration     configurationService;

    public GetDefaultConceptForDsdAtributeActionHandler() {
        super(GetDefaultConceptForDsdAtributeAction.class);
    }

    @Override
    public GetDefaultConceptForDsdAtributeResult executeSecurityAction(GetDefaultConceptForDsdAtributeAction action) throws ActionException {
        RelatedResourceDto defaultConcept = null;
        SpecialAttributeTypeEnum specialAttributeTypeEnum = action.getSpecialAttributeTypeEnum();
        if (SpecialAttributeTypeEnum.TIME_EXTENDS.equals(specialAttributeTypeEnum)) {
            defaultConcept = getDefaultTimeAttributeConcept(action.getDsdUrn());
        } else if (SpecialAttributeTypeEnum.MEASURE_EXTENDS.equals(specialAttributeTypeEnum)) {
            defaultConcept = getDefaultMeasureAttributeConcept(action.getDsdUrn());
        }
        return new GetDefaultConceptForDsdAtributeResult(defaultConcept);
    }

    private RelatedResourceDto getDefaultTimeAttributeConcept(String dsdUrn) throws MetamacWebException {
        try {
            String defaultConceptUrn = configurationService.retrieveDsdTimeDimensionOrAttributeDefaultConceptIdUrn();
            if (StringUtils.isNotBlank(defaultConceptUrn)) {
                MetamacCriteria criteria = new MetamacCriteria();
                criteria.setPaginator(new MetamacCriteriaPaginator());
                criteria.getPaginator().setFirstResult(0);
                criteria.getPaginator().setMaximumResultSize(SrmWebConstants.NO_LIMIT_IN_PAGINATION);
                criteria.getPaginator().setCountTotalResults(true);
                criteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.URN.name(), defaultConceptUrn, OperationType.EQ));

                MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptsCanBeDsdTimeAttributeByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria, dsdUrn);
                if (result.getResults() != null && !result.getResults().isEmpty()) {
                    return result.getResults().get(0);
                }
            }
            return null;
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    private RelatedResourceDto getDefaultMeasureAttributeConcept(String dsdUrn) throws MetamacWebException {
        try {
            String defaultConceptUrn = configurationService.retrieveDsdMeasureDimensionOrAttributeDefaultConceptIdUrn();
            if (StringUtils.isNotBlank(defaultConceptUrn)) {
                MetamacCriteria criteria = new MetamacCriteria();
                criteria.setPaginator(new MetamacCriteriaPaginator());
                criteria.getPaginator().setFirstResult(0);
                criteria.getPaginator().setMaximumResultSize(SrmWebConstants.NO_LIMIT_IN_PAGINATION);
                criteria.getPaginator().setCountTotalResults(true);
                criteria.setRestriction(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.URN.name(), defaultConceptUrn, OperationType.EQ));

                MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptsCanBeDsdMeasureAttributeByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria,
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
