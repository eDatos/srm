package org.siemac.metamac.srm.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.srm.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.srm.web.shared.PublishConceptSchemeInternallyAction;
import org.siemac.metamac.srm.web.shared.PublishConceptSchemeInternallyResult;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class PublishConceptSchemeInternallyActionHandler extends AbstractActionHandler<PublishConceptSchemeInternallyAction, PublishConceptSchemeInternallyResult> {

    public PublishConceptSchemeInternallyActionHandler() {
        super(PublishConceptSchemeInternallyAction.class);
    }

    @Override
    public PublishConceptSchemeInternallyResult execute(PublishConceptSchemeInternallyAction action, ExecutionContext context) throws ActionException {
        try {
            ConceptSchemeDto scheme = ConceptSchemeService.publishInternally(action.getId());
            return new PublishConceptSchemeInternallyResult(scheme);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(PublishConceptSchemeInternallyAction action, PublishConceptSchemeInternallyResult result, ExecutionContext context) throws ActionException {
        // NOTHING
    }

}
