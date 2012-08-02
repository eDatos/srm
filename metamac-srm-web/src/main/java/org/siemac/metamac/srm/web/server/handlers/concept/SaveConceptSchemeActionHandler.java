package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptSchemeResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.concept.ConceptSchemeDto;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveConceptSchemeActionHandler extends SecurityActionHandler<SaveConceptSchemeAction, SaveConceptSchemeResult> {

    public SaveConceptSchemeActionHandler() {
        super(SaveConceptSchemeAction.class);
    }

    @Override
    public SaveConceptSchemeResult executeSecurityAction(SaveConceptSchemeAction action) throws ActionException {
        try {
            ConceptSchemeDto schemeDto = ConceptSchemeService.saveConceptScheme(action.getConceptSchemeDto());
            return new SaveConceptSchemeResult(schemeDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(SaveConceptSchemeAction action, SaveConceptSchemeResult result, ExecutionContext context) throws ActionException {
        // NOTHING
    }
}
