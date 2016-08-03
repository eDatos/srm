package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.GetCodeAction;
import org.siemac.metamac.srm.web.shared.code.GetCodeResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCodeActionHandler extends SecurityActionHandler<GetCodeAction, GetCodeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCodeActionHandler() {
        super(GetCodeAction.class);
    }

    @Override
    public GetCodeResult executeSecurityAction(GetCodeAction action) throws ActionException {
        try {
            CodeMetamacDto codeMetamacDto = srmCoreServiceFacade.retrieveCodeByUrn(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            CodelistMetamacDto codelistMetamacDto = srmCoreServiceFacade.retrieveCodelistByUrn(ServiceContextHolder.getCurrentServiceContext(), codeMetamacDto.getItemSchemeVersionUrn());
            return new GetCodeResult(codeMetamacDto, codelistMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
