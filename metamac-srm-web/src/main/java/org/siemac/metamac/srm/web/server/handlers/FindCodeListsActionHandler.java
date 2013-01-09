package org.siemac.metamac.srm.web.server.handlers;

import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.FindCodeListsAction;
import org.siemac.metamac.srm.web.shared.FindCodeListsResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class FindCodeListsActionHandler extends SecurityActionHandler<FindCodeListsAction, FindCodeListsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public FindCodeListsActionHandler() {
        super(FindCodeListsAction.class);
    }

    @Override
    public FindCodeListsResult executeSecurityAction(FindCodeListsAction action) throws ActionException {
        // TODO

        // try {
        // List<ExternalItemDto> codeLists = srmCoreServiceFacade.findCodelists(ServiceContextHolder.getCurrentServiceContext(), action.getUriConcept());
        // return new FindCodeListsResult(codeLists);
        // } catch (MetamacException e) {
        // throw WebExceptionUtils.createMetamacWebException(e);
        // }
        return null;
    }
}
