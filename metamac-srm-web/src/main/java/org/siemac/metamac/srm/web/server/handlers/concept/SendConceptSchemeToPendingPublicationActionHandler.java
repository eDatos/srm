package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.srm.web.shared.concept.SendConceptSchemeToPendingPublicationAction;
import org.siemac.metamac.srm.web.shared.concept.SendConceptSchemeToPendingPublicationResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SendConceptSchemeToPendingPublicationActionHandler extends SecurityActionHandler<SendConceptSchemeToPendingPublicationAction, SendConceptSchemeToPendingPublicationResult> {

    public SendConceptSchemeToPendingPublicationActionHandler() {
        super(SendConceptSchemeToPendingPublicationAction.class);
    }

    @Override
    public SendConceptSchemeToPendingPublicationResult executeSecurityAction(SendConceptSchemeToPendingPublicationAction action) throws ActionException {
        return null;
    }

}
