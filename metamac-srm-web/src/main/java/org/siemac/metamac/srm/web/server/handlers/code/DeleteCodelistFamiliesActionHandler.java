package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistFamiliesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.shared.ActionException;

public class DeleteCodelistFamiliesActionHandler extends SecurityActionHandler<DeleteCodelistFamiliesAction, DeleteCodelistFamiliesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteCodelistFamiliesActionHandler() {
        super(DeleteCodelistFamiliesAction.class);
    }

    @Override
    public DeleteCodelistFamiliesResult executeSecurityAction(DeleteCodelistFamiliesAction action) throws ActionException {
        try {
            for (String urn : action.getUrns()) {
                srmCoreServiceFacade.deleteCodelistFamily(ServiceContextHolder.getCurrentServiceContext(), urn);
            }
            return new DeleteCodelistFamiliesResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
