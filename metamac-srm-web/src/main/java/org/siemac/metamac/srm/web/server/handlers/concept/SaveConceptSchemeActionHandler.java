package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.SaveConceptSchemeResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveConceptSchemeActionHandler extends SecurityActionHandler<SaveConceptSchemeAction, SaveConceptSchemeResult> {

    public SaveConceptSchemeActionHandler() {
        super(SaveConceptSchemeAction.class);
    }

    @Override
    public SaveConceptSchemeResult executeSecurityAction(SaveConceptSchemeAction action) throws ActionException {
        try {
            ConceptSchemeMetamacDto schemeDto = ConceptSchemeService.saveConceptScheme(action.getConceptSchemeDto());
            return new SaveConceptSchemeResult(schemeDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
