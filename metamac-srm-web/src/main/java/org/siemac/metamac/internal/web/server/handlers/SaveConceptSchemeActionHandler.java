package org.siemac.metamac.internal.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.internal.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.internal.web.shared.SaveConceptSchemeAction;
import org.siemac.metamac.internal.web.shared.SaveConceptSchemeResult;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;


public class SaveConceptSchemeActionHandler extends AbstractActionHandler<SaveConceptSchemeAction, SaveConceptSchemeResult>{

    public SaveConceptSchemeActionHandler() {
        super(SaveConceptSchemeAction.class);
    }
    
    @Override
    public SaveConceptSchemeResult execute(SaveConceptSchemeAction action, ExecutionContext context) throws ActionException {
        try {
            ConceptSchemeDto schemeDto = ConceptSchemeService.saveConceptScheme(action.getConceptSchemeDto());
            return new SaveConceptSchemeResult(schemeDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
    
    @Override
    public void undo(SaveConceptSchemeAction action, SaveConceptSchemeResult result, ExecutionContext context) throws ActionException {
        //NOTHING
    }
}
