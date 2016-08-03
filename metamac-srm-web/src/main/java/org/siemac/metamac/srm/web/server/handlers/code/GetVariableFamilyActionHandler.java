package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamilyAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamilyResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetVariableFamilyActionHandler extends SecurityActionHandler<GetVariableFamilyAction, GetVariableFamilyResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetVariableFamilyActionHandler() {
        super(GetVariableFamilyAction.class);
    }

    @Override
    public GetVariableFamilyResult executeSecurityAction(GetVariableFamilyAction action) throws ActionException {
        try {
            VariableFamilyDto variableFamilyDto = srmCoreServiceFacade.retrieveVariableFamilyByUrn(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new GetVariableFamilyResult(variableFamilyDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
