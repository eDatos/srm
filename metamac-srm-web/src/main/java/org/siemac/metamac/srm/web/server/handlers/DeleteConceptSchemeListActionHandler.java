package org.siemac.metamac.internal.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.internal.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.internal.web.shared.DeleteConceptSchemeListAction;
import org.siemac.metamac.internal.web.shared.DeleteConceptSchemeListResult;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class DeleteConceptSchemeListActionHandler extends AbstractActionHandler<DeleteConceptSchemeListAction, DeleteConceptSchemeListResult> {

    public DeleteConceptSchemeListActionHandler() {
        super(DeleteConceptSchemeListAction.class);
    }

    @Override
    public DeleteConceptSchemeListResult execute(DeleteConceptSchemeListAction action, ExecutionContext context) throws ActionException {
        try {
            for (String uuid : action.getUuids()) {
                ConceptSchemeService.deleteConceptScheme(uuid);
            }
            return new DeleteConceptSchemeListResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(DeleteConceptSchemeListAction action, DeleteConceptSchemeListResult result, ExecutionContext context) throws ActionException {
        // NOTHING
    }

}
