package org.siemac.metamac.internal.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.internal.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.internal.web.shared.SendConceptSchemeToPendingPublicationAction;
import org.siemac.metamac.internal.web.shared.SendConceptSchemeToPendingPublicationResult;
import org.siemac.metamac.internal.web.shared.VersioningConceptSchemeResult;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;


public class SendConceptSchemeToPendingPublicationActionHandler extends AbstractActionHandler<SendConceptSchemeToPendingPublicationAction, SendConceptSchemeToPendingPublicationResult> {
    

    public SendConceptSchemeToPendingPublicationActionHandler() {
        super(SendConceptSchemeToPendingPublicationAction.class);
    }
    
    @Override
    public SendConceptSchemeToPendingPublicationResult execute(SendConceptSchemeToPendingPublicationAction action, ExecutionContext context) throws ActionException {
        try {
            ConceptSchemeDto scheme = ConceptSchemeService.sendToPendingPublication(action.getConceptSchemeUuid());
            return new SendConceptSchemeToPendingPublicationResult(scheme);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
    
    public void undo(SendConceptSchemeToPendingPublicationAction action, SendConceptSchemeToPendingPublicationResult result, ExecutionContext context) throws ActionException {
        //NOTHING
    };

}
