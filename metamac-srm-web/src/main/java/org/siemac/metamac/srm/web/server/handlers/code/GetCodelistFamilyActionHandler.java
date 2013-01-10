package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.GetCodelistFamilyAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistFamilyResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCodelistFamilyActionHandler extends SecurityActionHandler<GetCodelistFamilyAction, GetCodelistFamilyResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCodelistFamilyActionHandler() {
        super(GetCodelistFamilyAction.class);
    }

    @Override
    public GetCodelistFamilyResult executeSecurityAction(GetCodelistFamilyAction action) throws ActionException {
        try {
            CodelistFamilyDto codelistFamilyDto = srmCoreServiceFacade.retrieveCodelistFamilyByUrn(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new GetCodelistFamilyResult(codelistFamilyDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
