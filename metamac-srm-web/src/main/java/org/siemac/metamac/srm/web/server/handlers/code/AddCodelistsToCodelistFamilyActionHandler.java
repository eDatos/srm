package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.AddCodelistsToCodelistFamilyAction;
import org.siemac.metamac.srm.web.shared.code.AddCodelistsToCodelistFamilyResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class AddCodelistsToCodelistFamilyActionHandler extends SecurityActionHandler<AddCodelistsToCodelistFamilyAction, AddCodelistsToCodelistFamilyResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public AddCodelistsToCodelistFamilyActionHandler() {
        super(AddCodelistsToCodelistFamilyAction.class);
    }

    @Override
    public AddCodelistsToCodelistFamilyResult executeSecurityAction(AddCodelistsToCodelistFamilyAction action) throws ActionException {
        try {
            srmCoreServiceFacade.addCodelistsToCodelistFamily(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrns(), action.getCodelistFamilyUrn());
            return new AddCodelistsToCodelistFamilyResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
