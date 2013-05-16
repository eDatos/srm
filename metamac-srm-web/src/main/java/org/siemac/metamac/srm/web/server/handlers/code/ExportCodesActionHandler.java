package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.ExportCodesAction;
import org.siemac.metamac.srm.web.shared.code.ExportCodesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ExportCodesActionHandler extends SecurityActionHandler<ExportCodesAction, ExportCodesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public ExportCodesActionHandler() {
        super(ExportCodesAction.class);
    }

    @Override
    public ExportCodesResult executeSecurityAction(ExportCodesAction action) throws ActionException {
        try {
            String fileName = srmCoreServiceFacade.exportCodesTsv(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrn());
            return new ExportCodesResult(fileName);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
