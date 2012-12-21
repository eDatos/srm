package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.VersionCodelistAction;
import org.siemac.metamac.srm.web.shared.code.VersionCodelistResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class VersionCodelistActionHandler extends SecurityActionHandler<VersionCodelistAction, VersionCodelistResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public VersionCodelistActionHandler() {
        super(VersionCodelistAction.class);
    }

    @Override
    public VersionCodelistResult executeSecurityAction(VersionCodelistAction action) throws ActionException {
        try {
            CodelistMetamacDto codelistMetamacDto = srmCoreServiceFacade.versioningCodelist(ServiceContextHolder.getCurrentServiceContext(), action.getUrn(), action.getVersionType());
            return new VersionCodelistResult(codelistMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
