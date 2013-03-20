package org.siemac.metamac.srm.web.server.handlers.code;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistOrderVisualisationDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.GetCodesByCodelistAction;
import org.siemac.metamac.srm.web.shared.code.GetCodesByCodelistResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCodesByCodelistActionHandler extends SecurityActionHandler<GetCodesByCodelistAction, GetCodesByCodelistResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCodesByCodelistActionHandler() {
        super(GetCodesByCodelistAction.class);
    }

    @Override
    public GetCodesByCodelistResult executeSecurityAction(GetCodesByCodelistAction action) throws ActionException {
        try {

            // Codes
            List<CodeMetamacVisualisationResult> codes = srmCoreServiceFacade.retrieveCodesByCodelistUrn(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrn(), action.getLocale(),
                    action.getCodelistOrderUrn(), null); // FIXME Set the openness visualisation

            // Order
            CodelistOrderVisualisationDto codelistOrderVisualisationDto = null;
            if (action.getCodelistOrderUrn() != null) {
                codelistOrderVisualisationDto = srmCoreServiceFacade.retrieveCodelistOrderVisualisationByUrn(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistOrderUrn());
            }

            return new GetCodesByCodelistResult(codes, codelistOrderVisualisationDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
