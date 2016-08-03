package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.UpdateCodesInOpennessVisualisationAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodesInOpennessVisualisationResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateCodesInOpennessVisualisationActionHandler extends SecurityActionHandler<UpdateCodesInOpennessVisualisationAction, UpdateCodesInOpennessVisualisationResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public UpdateCodesInOpennessVisualisationActionHandler() {
        super(UpdateCodesInOpennessVisualisationAction.class);
    }

    @Override
    public UpdateCodesInOpennessVisualisationResult executeSecurityAction(UpdateCodesInOpennessVisualisationAction action) throws ActionException {
        try {
            srmCoreServiceFacade.updateCodesInOpennessVisualisation(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistOpennessVisualisationUrn(), action.getOpennessLevels());
            CodelistVisualisationDto codelistVisualisationDto = srmCoreServiceFacade.retrieveCodelistOpennessVisualisationByUrn(ServiceContextHolder.getCurrentServiceContext(),
                    action.getCodelistOpennessVisualisationUrn());
            return new UpdateCodesInOpennessVisualisationResult(codelistVisualisationDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
