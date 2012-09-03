package org.siemac.metamac.srm.web.server.handlers.concept;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.GetConceptListBySchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptListBySchemeResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetConceptListBySchemeActionHandler extends SecurityActionHandler<GetConceptListBySchemeAction, GetConceptListBySchemeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetConceptListBySchemeActionHandler() {
        super(GetConceptListBySchemeAction.class);
    }

    @Override
    public GetConceptListBySchemeResult executeSecurityAction(GetConceptListBySchemeAction action) throws ActionException {
        try {
            List<ItemHierarchyDto> itemHierarchyDtos = srmCoreServiceFacade.retrieveConceptsByConceptSchemeUrn(ServiceContextHolder.getCurrentServiceContext(), action.getConceptSchemeUrn());
            return new GetConceptListBySchemeResult(itemHierarchyDtos);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
