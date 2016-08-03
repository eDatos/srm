package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistOpennessLevelsAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistOpennessLevelsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteCodelistOpennessLevelsActionHandler extends SecurityActionHandler<DeleteCodelistOpennessLevelsAction, DeleteCodelistOpennessLevelsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteCodelistOpennessLevelsActionHandler() {
        super(DeleteCodelistOpennessLevelsAction.class);
    }

    @Override
    public DeleteCodelistOpennessLevelsResult executeSecurityAction(DeleteCodelistOpennessLevelsAction action) throws ActionException {
        try {
            for (String levelUrn : action.getOpennessLevelUrns()) {
                srmCoreServiceFacade.deleteCodelistOpennessVisualisation(ServiceContextHolder.getCurrentServiceContext(), levelUrn);
            }
            return new DeleteCodelistOpennessLevelsResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
