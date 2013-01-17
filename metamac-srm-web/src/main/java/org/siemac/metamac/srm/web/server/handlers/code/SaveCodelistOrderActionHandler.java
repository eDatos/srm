package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistOrderVisualisationDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistOrderAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistOrderResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveCodelistOrderActionHandler extends SecurityActionHandler<SaveCodelistOrderAction, SaveCodelistOrderResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveCodelistOrderActionHandler() {
        super(SaveCodelistOrderAction.class);
    }

    @Override
    public SaveCodelistOrderResult executeSecurityAction(SaveCodelistOrderAction action) throws ActionException {
        try {
            CodelistOrderVisualisationDto codelistToSave = action.getCodelistOrderVisualisationDto();
            CodelistOrderVisualisationDto savedCodelistDto = null;
            if (codelistToSave.getId() == null) {
                // Create
                savedCodelistDto = srmCoreServiceFacade.createCodelistOrderVisualisation(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrn(), codelistToSave);
            } else {
                // Update
                savedCodelistDto = srmCoreServiceFacade.updateCodelistOrderVisualisation(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrn(), codelistToSave);
            }
            return new SaveCodelistOrderResult(savedCodelistDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
