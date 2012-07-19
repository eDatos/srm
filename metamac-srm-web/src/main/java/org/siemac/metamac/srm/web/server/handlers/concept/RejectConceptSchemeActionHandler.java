package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.srm.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.srm.web.shared.RejectConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.RejectConceptSchemeResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class RejectConceptSchemeActionHandler extends SecurityActionHandler<RejectConceptSchemeAction, RejectConceptSchemeResult> {

    public RejectConceptSchemeActionHandler() {
        super(RejectConceptSchemeAction.class);
    }

    @Override
    public RejectConceptSchemeResult executeSecurityAction(RejectConceptSchemeAction action) throws ActionException {
        try {
            ConceptSchemeDto scheme = ConceptSchemeService.reject(action.getId());
            return new RejectConceptSchemeResult(scheme);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(RejectConceptSchemeAction action, RejectConceptSchemeResult result, ExecutionContext context) throws ActionException {
        // NOTHING
    }

}
