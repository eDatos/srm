package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.ImportCodeOrdersCsvInBackgroundAction;
import org.siemac.metamac.srm.web.shared.code.ImportCodeOrdersCsvInBackgroundResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ImportCodeOrdersCsvInBackgroundActionHandler extends SecurityActionHandler<ImportCodeOrdersCsvInBackgroundAction, ImportCodeOrdersCsvInBackgroundResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public ImportCodeOrdersCsvInBackgroundActionHandler() {
        super(ImportCodeOrdersCsvInBackgroundAction.class);
    }

    @Override
    public ImportCodeOrdersCsvInBackgroundResult executeSecurityAction(ImportCodeOrdersCsvInBackgroundAction action) throws ActionException {
        try {
            srmCoreServiceFacade.importCodeOrdersCsvInBackground(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrn(), action.getCsvStream(), action.getFileName());
            return new ImportCodeOrdersCsvInBackgroundResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
