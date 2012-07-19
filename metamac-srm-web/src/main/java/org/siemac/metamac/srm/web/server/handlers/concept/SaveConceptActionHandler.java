package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.srm.web.shared.concept.SaveConceptAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveConceptActionHandler extends SecurityActionHandler<SaveConceptAction, SaveConceptResult> {

    public SaveConceptActionHandler() {
        super(SaveConceptAction.class);
    }

    @Override
    public SaveConceptResult executeSecurityAction(SaveConceptAction action) throws ActionException {
        return new SaveConceptResult(action.getConceptToSave());
    }

}
