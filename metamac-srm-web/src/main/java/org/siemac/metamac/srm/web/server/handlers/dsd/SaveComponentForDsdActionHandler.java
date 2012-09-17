package org.siemac.metamac.srm.web.server.handlers.dsd;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.SaveComponentForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveComponentForDsdResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveComponentForDsdActionHandler extends SecurityActionHandler<SaveComponentForDsdAction, SaveComponentForDsdResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveComponentForDsdActionHandler() {
        super(SaveComponentForDsdAction.class);
    }

    @Override
    public SaveComponentForDsdResult executeSecurityAction(SaveComponentForDsdAction action) throws ActionException {
        try {
            ComponentDto componentDto = srmCoreServiceFacade.saveComponentForDsd(ServiceContextHolder.getCurrentServiceContext(), action.getDsdUrn(), action.getComponentDto(),
                    action.getTypeComponentList());
            return new SaveComponentForDsdResult(componentDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(SaveComponentForDsdAction action, SaveComponentForDsdResult result, ExecutionContext context) throws ActionException {

    }

}
