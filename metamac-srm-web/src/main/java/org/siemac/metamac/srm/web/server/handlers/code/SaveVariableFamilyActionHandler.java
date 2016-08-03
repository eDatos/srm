package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.SaveVariableFamilyAction;
import org.siemac.metamac.srm.web.shared.code.SaveVariableFamilyResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveVariableFamilyActionHandler extends SecurityActionHandler<SaveVariableFamilyAction, SaveVariableFamilyResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveVariableFamilyActionHandler() {
        super(SaveVariableFamilyAction.class);
    }

    @Override
    public SaveVariableFamilyResult executeSecurityAction(SaveVariableFamilyAction action) throws ActionException {
        try {
            VariableFamilyDto familyToSave = action.getVariableFamilyDto();
            VariableFamilyDto savedFamily = null;
            if (familyToSave.getId() == null) {
                // Create
                savedFamily = srmCoreServiceFacade.createVariableFamily(ServiceContextHolder.getCurrentServiceContext(), familyToSave);
                return new SaveVariableFamilyResult(savedFamily);
            } else {
                // Update
                savedFamily = srmCoreServiceFacade.updateVariableFamily(ServiceContextHolder.getCurrentServiceContext(), familyToSave);
            }
            return new SaveVariableFamilyResult(savedFamily);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
