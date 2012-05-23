package org.siemac.metamac.srm.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.srm.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.srm.web.shared.RejectConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.RejectConceptSchemeResult;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class RejectConceptSchemeActionHandler extends AbstractActionHandler<RejectConceptSchemeAction, RejectConceptSchemeResult> {

    public RejectConceptSchemeActionHandler() {
        super(RejectConceptSchemeAction.class);
    }

    @Override
    public RejectConceptSchemeResult execute(RejectConceptSchemeAction action, ExecutionContext context) throws ActionException {
        try {
            ConceptSchemeDto scheme = ConceptSchemeService.reject(action.getConceptSchemeUuid());
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
