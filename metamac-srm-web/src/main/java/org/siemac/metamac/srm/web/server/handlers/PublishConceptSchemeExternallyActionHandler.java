package org.siemac.metamac.srm.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.srm.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.srm.web.shared.PublishConceptSchemeExternallyAction;
import org.siemac.metamac.srm.web.shared.PublishConceptSchemeExternallyResult;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class PublishConceptSchemeExternallyActionHandler extends AbstractActionHandler<PublishConceptSchemeExternallyAction, PublishConceptSchemeExternallyResult> {

    public PublishConceptSchemeExternallyActionHandler() {
        super(PublishConceptSchemeExternallyAction.class);
    }

    @Override
    public PublishConceptSchemeExternallyResult execute(PublishConceptSchemeExternallyAction action, ExecutionContext context) throws ActionException {
        try {
            ConceptSchemeDto scheme = ConceptSchemeService.publishExternally(action.getId());
            return new PublishConceptSchemeExternallyResult(scheme);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(PublishConceptSchemeExternallyAction action, PublishConceptSchemeExternallyResult result, ExecutionContext context) throws ActionException {
        // NOTHING
    }

}
