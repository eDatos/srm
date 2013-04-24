package org.siemac.metamac.srm.web.server.handlers.code;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.GetCodelistVersionsAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistVersionsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCodelistVersionsActionHandler extends SecurityActionHandler<GetCodelistVersionsAction, GetCodelistVersionsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCodelistVersionsActionHandler() {
        super(GetCodelistVersionsAction.class);
    }

    @Override
    public GetCodelistVersionsResult executeSecurityAction(GetCodelistVersionsAction action) throws ActionException {
        try {
            List<CodelistMetamacBasicDto> codelistMetamacDtos = srmCoreServiceFacade.retrieveCodelistVersions(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new GetCodelistVersionsResult(codelistMetamacDtos);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
