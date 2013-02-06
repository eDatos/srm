package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.UpdateCodeVariableElementAction;
import org.siemac.metamac.srm.web.shared.concept.UpdateCodeVariableElementResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateCodeVariableElementActionHandler extends SecurityActionHandler<UpdateCodeVariableElementAction, UpdateCodeVariableElementResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public UpdateCodeVariableElementActionHandler() {
        super(UpdateCodeVariableElementAction.class);
    }

    @Override
    public UpdateCodeVariableElementResult executeSecurityAction(UpdateCodeVariableElementAction action) throws ActionException {
        try {
            CodeMetamacDto codeMetamacDto = srmCoreServiceFacade.updateCodeVariableElement(ServiceContextHolder.getCurrentServiceContext(), action.getCodeUrn(), action.getVariableElementUrn());
            return new UpdateCodeVariableElementResult(codeMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}