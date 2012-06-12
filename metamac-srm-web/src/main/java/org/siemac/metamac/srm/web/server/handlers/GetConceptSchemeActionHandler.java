package org.siemac.metamac.srm.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.srm.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.srm.web.shared.GetConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.GetConceptSchemeResult;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetConceptSchemeActionHandler extends AbstractActionHandler<GetConceptSchemeAction, GetConceptSchemeResult> {

    public GetConceptSchemeActionHandler() {
        super(GetConceptSchemeAction.class);
    }

    @Override
    public GetConceptSchemeResult execute(GetConceptSchemeAction action, ExecutionContext context) throws ActionException {
        try {
            ConceptSchemeDto scheme = ConceptSchemeService.retriveConceptSchemeByIdLogic(action.getIdLogic());
            return new GetConceptSchemeResult(scheme);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    public void undo(GetConceptSchemeAction action, GetConceptSchemeResult result, ExecutionContext context) throws ActionException {
        // NOTHING
    };

}
