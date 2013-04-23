package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.ImportCodesCsvInBackgroundAction;
import org.siemac.metamac.srm.web.shared.code.ImportCodesCsvInBackgroundResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ImportCodesCsvInBackgroundActionHandler extends SecurityActionHandler<ImportCodesCsvInBackgroundAction, ImportCodesCsvInBackgroundResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public ImportCodesCsvInBackgroundActionHandler() {
        super(ImportCodesCsvInBackgroundAction.class);
    }

    @Override
    public ImportCodesCsvInBackgroundResult executeSecurityAction(ImportCodesCsvInBackgroundAction action) throws ActionException {
        try {
            srmCoreServiceFacade.importCodesCsvInBackground(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrn(), action.getCsvStream(), action.getFileName(),
                    action.getUpdateAlreadyExisting());
            return new ImportCodesCsvInBackgroundResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
