package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.UpdateConceptParentAction;
import org.siemac.metamac.srm.web.shared.concept.UpdateConceptParentResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateConceptParentActionHandler extends SecurityActionHandler<UpdateConceptParentAction, UpdateConceptParentResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public UpdateConceptParentActionHandler() {
        super(UpdateConceptParentAction.class);
    }

    @Override
    public UpdateConceptParentResult executeSecurityAction(UpdateConceptParentAction action) throws ActionException {
        try {
            srmCoreServiceFacade.updateConceptParent(ServiceContextHolder.getCurrentServiceContext(), action.getConceptUrn(), action.getNewParentUrn(), action.getNewConceptIndex());
            return new UpdateConceptParentResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
