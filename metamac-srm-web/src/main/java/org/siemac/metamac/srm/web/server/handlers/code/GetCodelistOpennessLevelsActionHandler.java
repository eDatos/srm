package org.siemac.metamac.srm.web.server.handlers.code;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistOpennessVisualisationDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.GetCodelistOpennessLevelsAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistOpennessLevelsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCodelistOpennessLevelsActionHandler extends SecurityActionHandler<GetCodelistOpennessLevelsAction, GetCodelistOpennessLevelsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCodelistOpennessLevelsActionHandler() {
        super(GetCodelistOpennessLevelsAction.class);
    }

    @Override
    public GetCodelistOpennessLevelsResult executeSecurityAction(GetCodelistOpennessLevelsAction action) throws ActionException {
        try {
            List<CodelistOpennessVisualisationDto> opennessLevels = srmCoreServiceFacade.retrieveCodelistOpennessVisualisationsByCodelist(ServiceContextHolder.getCurrentServiceContext(),
                    action.getCodelistUrn());
            return new GetCodelistOpennessLevelsResult(opennessLevels);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
