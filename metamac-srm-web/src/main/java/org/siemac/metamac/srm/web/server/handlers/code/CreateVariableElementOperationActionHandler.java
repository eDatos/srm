package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.VariableElementOperationDto;
import org.siemac.metamac.srm.core.code.enume.domain.VariableElementOperationTypeEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.CreateVariableElementOperationAction;
import org.siemac.metamac.srm.web.shared.code.CreateVariableElementOperationResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CreateVariableElementOperationActionHandler extends SecurityActionHandler<CreateVariableElementOperationAction, CreateVariableElementOperationResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CreateVariableElementOperationActionHandler() {
        super(CreateVariableElementOperationAction.class);
    }

    @Override
    public CreateVariableElementOperationResult executeSecurityAction(CreateVariableElementOperationAction action) throws ActionException {
        try {
            VariableElementOperationDto variableElementOperationDto = null;
            if (VariableElementOperationTypeEnum.FUSION.equals(action.getOperationType())) {
                variableElementOperationDto = srmCoreServiceFacade.createVariableElementFusionOperation(ServiceContextHolder.getCurrentServiceContext(), action.getVariableElementUrns(),
                        action.getVariableElementUrn());
            } else if (VariableElementOperationTypeEnum.SEGREGATION.equals(action.getOperationType())) {
                variableElementOperationDto = srmCoreServiceFacade.createVariableElementSegregationOperation(ServiceContextHolder.getCurrentServiceContext(), action.getVariableElementUrn(),
                        action.getVariableElementUrns());
            }
            return new CreateVariableElementOperationResult(variableElementOperationDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
