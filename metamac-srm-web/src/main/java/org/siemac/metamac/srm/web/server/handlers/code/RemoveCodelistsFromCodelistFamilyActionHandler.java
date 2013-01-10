package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.RemoveCodelistsFromCodelistFamilyAction;
import org.siemac.metamac.srm.web.shared.code.RemoveCodelistsFromCodelistFamilyResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class RemoveCodelistsFromCodelistFamilyActionHandler extends SecurityActionHandler<RemoveCodelistsFromCodelistFamilyAction, RemoveCodelistsFromCodelistFamilyResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public RemoveCodelistsFromCodelistFamilyActionHandler() {
        super(RemoveCodelistsFromCodelistFamilyAction.class);
    }

    @Override
    public RemoveCodelistsFromCodelistFamilyResult executeSecurityAction(RemoveCodelistsFromCodelistFamilyAction action) throws ActionException {
        // TODO
        // try {
        // srmCoreServiceFacade.removeCodelistsFromCodelistFamily(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrns(), action.getCodelistFamilyUrn());
        return new RemoveCodelistsFromCodelistFamilyResult();
        // } catch (MetamacException e) {
        // throw WebExceptionUtils.createMetamacWebException(e);
        // }
    }
}
