package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.GetCodelistAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCodelistActionHandler extends SecurityActionHandler<GetCodelistAction, GetCodelistResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCodelistActionHandler() {
        super(GetCodelistAction.class);
    }

    @Override
    public GetCodelistResult executeSecurityAction(GetCodelistAction action) throws ActionException {
        try {
            CodelistMetamacDto codelistMetamacDto = srmCoreServiceFacade.retrieveCodelistByUrn(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new GetCodelistResult(codelistMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
