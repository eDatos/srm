package org.siemac.metamac.internal.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.internal.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.internal.web.shared.GetConceptSchemeByIdLogicAction;
import org.siemac.metamac.internal.web.shared.GetConceptSchemeByIdLogicResult;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;


public class GetConceptSchemeByIdLogicActionHandler extends AbstractActionHandler<GetConceptSchemeByIdLogicAction, GetConceptSchemeByIdLogicResult> {
    
    
    public GetConceptSchemeByIdLogicActionHandler() {
        super(GetConceptSchemeByIdLogicAction.class);
    }
    
    @Override
    public GetConceptSchemeByIdLogicResult execute(GetConceptSchemeByIdLogicAction action, ExecutionContext context) throws ActionException {
        try  {
            ConceptSchemeDto scheme = ConceptSchemeService.retriveConceptSchemeByIdLogic(action.getIdLogic());
            return new GetConceptSchemeByIdLogicResult(scheme);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
    
    public void undo(GetConceptSchemeByIdLogicAction action, GetConceptSchemeByIdLogicResult result, ExecutionContext context) throws ActionException {
        //NOTHING
    };
    
    

}
