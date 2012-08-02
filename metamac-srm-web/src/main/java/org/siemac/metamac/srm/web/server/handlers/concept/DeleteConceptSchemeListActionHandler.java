package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptSchemeListAction;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptSchemeListResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteConceptSchemeListActionHandler extends SecurityActionHandler<DeleteConceptSchemeListAction, DeleteConceptSchemeListResult> {

    public DeleteConceptSchemeListActionHandler() {
        super(DeleteConceptSchemeListAction.class);
    }

    @Override
    public DeleteConceptSchemeListResult executeSecurityAction(DeleteConceptSchemeListAction action) throws ActionException {
        try {
            for (Long id : action.getIds()) {
                ConceptSchemeService.deleteConceptScheme(id);
            }
            return new DeleteConceptSchemeListResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
