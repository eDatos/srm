package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.srm.web.shared.concept.DeleteConceptListAction;
import org.siemac.metamac.srm.web.shared.concept.DeleteConceptListResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteConceptListActionHandler extends SecurityActionHandler<DeleteConceptListAction, DeleteConceptListResult> {

    public DeleteConceptListActionHandler() {
        super(DeleteConceptListAction.class);
    }

    @Override
    public DeleteConceptListResult executeSecurityAction(DeleteConceptListAction action) throws ActionException {
        // TODO
        return new DeleteConceptListResult();
    }

}
