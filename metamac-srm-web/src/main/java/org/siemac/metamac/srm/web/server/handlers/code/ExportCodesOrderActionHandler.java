package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.ExportCodesOrderAction;
import org.siemac.metamac.srm.web.shared.code.ExportCodesOrderResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ExportCodesOrderActionHandler extends SecurityActionHandler<ExportCodesOrderAction, ExportCodesOrderResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public ExportCodesOrderActionHandler() {
        super(ExportCodesOrderAction.class);
    }

    @Override
    public ExportCodesOrderResult executeSecurityAction(ExportCodesOrderAction action) throws ActionException {
        try {
            String fileName = srmCoreServiceFacade.exportCodeOrdersTsv(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrn());
            return new ExportCodesOrderResult(fileName);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
