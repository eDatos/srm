package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.ExportVariableElementsAction;
import org.siemac.metamac.srm.web.shared.code.ExportVariableElementsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ExportVariableElementsActionHandler extends SecurityActionHandler<ExportVariableElementsAction, ExportVariableElementsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public ExportVariableElementsActionHandler() {
        super(ExportVariableElementsAction.class);
    }

    @Override
    public ExportVariableElementsResult executeSecurityAction(ExportVariableElementsAction action) throws ActionException {
        try {
            String fileName = srmCoreServiceFacade.exportVariableElementsTsv(ServiceContextHolder.getCurrentServiceContext(), action.getVariableUrn());
            return new ExportVariableElementsResult(fileName);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
