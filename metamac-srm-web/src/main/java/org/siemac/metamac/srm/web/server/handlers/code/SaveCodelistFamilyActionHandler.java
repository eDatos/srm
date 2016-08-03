package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistFamilyAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistFamilyResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveCodelistFamilyActionHandler extends SecurityActionHandler<SaveCodelistFamilyAction, SaveCodelistFamilyResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveCodelistFamilyActionHandler() {
        super(SaveCodelistFamilyAction.class);
    }

    @Override
    public SaveCodelistFamilyResult executeSecurityAction(SaveCodelistFamilyAction action) throws ActionException {
        try {
            CodelistFamilyDto familyToSave = action.getCodelistFamilyDto();
            CodelistFamilyDto savedFamily = null;
            if (familyToSave.getId() == null) {
                // Create
                savedFamily = srmCoreServiceFacade.createCodelistFamily(ServiceContextHolder.getCurrentServiceContext(), familyToSave);
                return new SaveCodelistFamilyResult(savedFamily);
            } else {
                // Update
                savedFamily = srmCoreServiceFacade.updateCodelistFamily(ServiceContextHolder.getCurrentServiceContext(), familyToSave);
            }
            return new SaveCodelistFamilyResult(savedFamily);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
