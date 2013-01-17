package org.siemac.metamac.srm.web.server.handlers.code;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodeHierarchyDto;
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
            List<CodeHierarchyDto> itemHierarchyDtos = srmCoreServiceFacade.retrieveCodesByCodelistUrn(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrn(),
                    action.getCodelistOrderIdentifier());

            // Order
            CodelistOrderVisualisationDto codelistOrderVisualisationDto = null;
            if (action.getCodelistOrderIdentifier() == null) {
                // TODO Default codelist order
            } else {
                codelistOrderVisualisationDto = srmCoreServiceFacade.retrieveCodelistOrderVisualisationByIdentifier(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrn(),
                        action.getCodelistOrderIdentifier());
            }

            return new GetCodesByCodelistResult(itemHierarchyDtos, codelistOrderVisualisationDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
