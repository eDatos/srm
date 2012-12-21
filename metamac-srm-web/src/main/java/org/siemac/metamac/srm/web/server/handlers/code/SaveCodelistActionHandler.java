package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveCodelistActionHandler extends SecurityActionHandler<SaveCodelistAction, SaveCodelistResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveCodelistActionHandler() {
        super(SaveCodelistAction.class);
    }

    @Override
    public SaveCodelistResult executeSecurityAction(SaveCodelistAction action) throws ActionException {
        try {
            CodelistMetamacDto codelistToSave = action.getCodelistDto();
            CodelistMetamacDto savedCodelistDto = null;
            if (codelistToSave.getId() == null) {
                // Create
                savedCodelistDto = srmCoreServiceFacade.createCodelist(ServiceContextHolder.getCurrentServiceContext(), codelistToSave);
                return new SaveCodelistResult(savedCodelistDto);
            } else {
                // Update
                savedCodelistDto = srmCoreServiceFacade.updateCodelist(ServiceContextHolder.getCurrentServiceContext(), codelistToSave);
            }
            return new SaveCodelistResult(savedCodelistDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
