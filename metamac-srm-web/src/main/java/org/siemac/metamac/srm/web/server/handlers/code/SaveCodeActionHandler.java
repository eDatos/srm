package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.SaveCodeAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodeResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveCodeActionHandler extends SecurityActionHandler<SaveCodeAction, SaveCodeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveCodeActionHandler() {
        super(SaveCodeAction.class);
    }

    @Override
    public SaveCodeResult executeSecurityAction(SaveCodeAction action) throws ActionException {
        try {
            CodeMetamacDto codeToSave = action.getCodeToSave();
            CodeMetamacDto savedCode = null;
            if (codeToSave.getId() == null) {
                // Create
                savedCode = srmCoreServiceFacade.createCode(ServiceContextHolder.getCurrentServiceContext(), codeToSave);
            } else {
                // Update
                savedCode = srmCoreServiceFacade.updateCode(ServiceContextHolder.getCurrentServiceContext(), codeToSave);
            }
            return new SaveCodeResult(savedCode);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
