package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.UpdateConceptInOrderAction;
import org.siemac.metamac.srm.web.shared.concept.UpdateConceptInOrderResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateConceptInOrderActionHandler extends SecurityActionHandler<UpdateConceptInOrderAction, UpdateConceptInOrderResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public UpdateConceptInOrderActionHandler() {
        super(UpdateConceptInOrderAction.class);
    }

    @Override
    public UpdateConceptInOrderResult executeSecurityAction(UpdateConceptInOrderAction action) throws ActionException {
        try {
            srmCoreServiceFacade.updateConceptInOrder(ServiceContextHolder.getCurrentServiceContext(), action.getConceptUrn(), action.getUrn(), action.getNewCodeIndex());
            return new UpdateConceptInOrderResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
