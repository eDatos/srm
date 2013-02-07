package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptSchemesAction;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptSchemesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteConceptSchemesActionHandler extends SecurityActionHandler<DeleteConceptSchemesAction, DeleteConceptSchemesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteConceptSchemesActionHandler() {
        super(DeleteConceptSchemesAction.class);
    }

    @Override
    public DeleteConceptSchemesResult executeSecurityAction(DeleteConceptSchemesAction action) throws ActionException {
        try {
            for (String urn : action.getUrns()) {
                srmCoreServiceFacade.deleteConceptScheme(ServiceContextHolder.getCurrentServiceContext(), urn);
            }
            return new DeleteConceptSchemesResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
