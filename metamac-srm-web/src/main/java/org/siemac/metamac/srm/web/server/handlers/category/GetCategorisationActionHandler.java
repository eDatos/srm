package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.GetCategorisationAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorisationResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCategorisationActionHandler extends SecurityActionHandler<GetCategorisationAction, GetCategorisationResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCategorisationActionHandler() {
        super(GetCategorisationAction.class);
    }

    @Override
    public GetCategorisationResult executeSecurityAction(GetCategorisationAction action) throws ActionException {
        try {
            CategorisationDto categorisationDto = srmCoreServiceFacade.retrieveCategorisationByUrn(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new GetCategorisationResult(categorisationDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
