package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.SaveVariableElementAction;
import org.siemac.metamac.srm.web.shared.code.SaveVariableElementResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveVariableElementActionHandler extends SecurityActionHandler<SaveVariableElementAction, SaveVariableElementResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveVariableElementActionHandler() {
        super(SaveVariableElementAction.class);
    }

    @Override
    public SaveVariableElementResult executeSecurityAction(SaveVariableElementAction action) throws ActionException {
        try {
            VariableElementDto variableElementToSave = action.getVariableElementDto();
            VariableElementDto savedVariableElementDto = null;
            if (variableElementToSave.getId() == null) {
                // Create
                savedVariableElementDto = srmCoreServiceFacade.createVariableElement(ServiceContextHolder.getCurrentServiceContext(), variableElementToSave);
            } else {
                // Update
                savedVariableElementDto = srmCoreServiceFacade.updateVariableElement(ServiceContextHolder.getCurrentServiceContext(), variableElementToSave);
            }
            return new SaveVariableElementResult(savedVariableElementDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
