package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.srm.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.srm.web.shared.concept.PublishConceptSchemeInternallyAction;
import org.siemac.metamac.srm.web.shared.concept.PublishConceptSchemeInternallyResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class PublishConceptSchemeInternallyActionHandler extends SecurityActionHandler<PublishConceptSchemeInternallyAction, PublishConceptSchemeInternallyResult> {

    public PublishConceptSchemeInternallyActionHandler() {
        super(PublishConceptSchemeInternallyAction.class);
    }

    @Override
    public PublishConceptSchemeInternallyResult executeSecurityAction(PublishConceptSchemeInternallyAction action) throws ActionException {
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
