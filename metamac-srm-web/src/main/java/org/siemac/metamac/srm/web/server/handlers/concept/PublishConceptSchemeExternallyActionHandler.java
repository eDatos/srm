package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.srm.web.shared.concept.PublishConceptSchemeExternallyAction;
import org.siemac.metamac.srm.web.shared.concept.PublishConceptSchemeExternallyResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.concept.ConceptSchemeDto;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class PublishConceptSchemeExternallyActionHandler extends SecurityActionHandler<PublishConceptSchemeExternallyAction, PublishConceptSchemeExternallyResult> {

    public PublishConceptSchemeExternallyActionHandler() {
        super(PublishConceptSchemeExternallyAction.class);
    }

    @Override
    public PublishConceptSchemeExternallyResult executeSecurityAction(PublishConceptSchemeExternallyAction action) throws ActionException {
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
