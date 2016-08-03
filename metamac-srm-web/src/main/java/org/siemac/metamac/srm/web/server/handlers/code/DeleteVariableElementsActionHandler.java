package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableElementsAction;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableElementsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteVariableElementsActionHandler extends SecurityActionHandler<DeleteVariableElementsAction, DeleteVariableElementsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteVariableElementsActionHandler() {
        super(DeleteVariableElementsAction.class);
    }

    @Override
    public DeleteVariableElementsResult executeSecurityAction(DeleteVariableElementsAction action) throws ActionException {
        try {
            for (String urn : action.getUrns()) {
                srmCoreServiceFacade.deleteVariableElement(ServiceContextHolder.getCurrentServiceContext(), urn);
            }
            return new DeleteVariableElementsResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
