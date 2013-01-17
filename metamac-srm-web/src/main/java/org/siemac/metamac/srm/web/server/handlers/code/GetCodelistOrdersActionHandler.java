package org.siemac.metamac.srm.web.server.handlers.code;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistOrderVisualisationDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.GetCodelistOrdersAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistOrdersResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCodelistOrdersActionHandler extends SecurityActionHandler<GetCodelistOrdersAction, GetCodelistOrdersResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCodelistOrdersActionHandler() {
        super(GetCodelistOrdersAction.class);
    }

    @Override
    public GetCodelistOrdersResult executeSecurityAction(GetCodelistOrdersAction action) throws ActionException {
        try {
            List<CodelistOrderVisualisationDto> orders = srmCoreServiceFacade.retrieveCodelistOrderVisualisationsByCodelist(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrn());
            return new GetCodelistOrdersResult(orders);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
