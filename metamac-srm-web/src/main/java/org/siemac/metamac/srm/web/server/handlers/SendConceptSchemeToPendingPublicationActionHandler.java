package org.siemac.metamac.srm.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.srm.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.srm.web.shared.SendConceptSchemeToPendingPublicationAction;
import org.siemac.metamac.srm.web.shared.SendConceptSchemeToPendingPublicationResult;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
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
        // NOTHING
    };

}
