package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.srm.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetConceptSchemeActionHandler extends SecurityActionHandler<GetConceptSchemeAction, GetConceptSchemeResult> {

    public GetConceptSchemeActionHandler() {
        super(GetConceptSchemeAction.class);
    }

    @Override
    public GetConceptSchemeResult executeSecurityAction(GetConceptSchemeAction action) throws ActionException {
        try {
            ConceptSchemeDto scheme = ConceptSchemeService.retriveConceptSchemeByIdLogic(action.getUrn());
            return new GetConceptSchemeResult(scheme);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
