package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.SaveVariableAction;
import org.siemac.metamac.srm.web.shared.code.SaveVariableResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveVariableActionHandler extends SecurityActionHandler<SaveVariableAction, SaveVariableResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveVariableActionHandler() {
        super(SaveVariableAction.class);
    }

    @Override
    public SaveVariableResult executeSecurityAction(SaveVariableAction action) throws ActionException {
        try {
            VariableDto variableToSave = action.getVariableDto();
            VariableDto savedVariableDto = null;
            if (variableToSave.getId() == null) {
                // Create
                savedVariableDto = srmCoreServiceFacade.createVariable(ServiceContextHolder.getCurrentServiceContext(), variableToSave);
            } else {
                // Update
                savedVariableDto = srmCoreServiceFacade.updateVariable(ServiceContextHolder.getCurrentServiceContext(), variableToSave);
            }
            return new SaveVariableResult(savedVariableDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
