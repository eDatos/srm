package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.CopyCodesInCodelistAction;
import org.siemac.metamac.srm.web.shared.code.CopyCodesInCodelistResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CopyCodesInCodelistActionHandler extends SecurityActionHandler<CopyCodesInCodelistAction, CopyCodesInCodelistResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CopyCodesInCodelistActionHandler() {
        super(CopyCodesInCodelistAction.class);
    }

    @Override
    public CopyCodesInCodelistResult executeSecurityAction(CopyCodesInCodelistAction action) throws ActionException {
        try {
            srmCoreServiceFacade.copyCodesInCodelist(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistTargetUrn(), action.getParentTargetUrn(), action.getCodesToCopy());
            return new CopyCodesInCodelistResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
