package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.ImportVariableElementCSVInBackgroundAction;
import org.siemac.metamac.srm.web.shared.code.ImportVariableElementCSVInBackgroundResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ImportVariableElementCSVInBackgroundActionHandler extends SecurityActionHandler<ImportVariableElementCSVInBackgroundAction, ImportVariableElementCSVInBackgroundResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public ImportVariableElementCSVInBackgroundActionHandler() {
        super(ImportVariableElementCSVInBackgroundAction.class);
    }

    @Override
    public ImportVariableElementCSVInBackgroundResult executeSecurityAction(ImportVariableElementCSVInBackgroundAction action) throws ActionException {
        try {
            srmCoreServiceFacade.importVariableElementsCsvInBackground(ServiceContextHolder.getCurrentServiceContext(), action.getVariableUrn(), action.getCsvStream(), action.getFileName(),
                    action.getUpdateAlreadyExisting());
            return new ImportVariableElementCSVInBackgroundResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
