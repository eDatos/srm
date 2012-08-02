package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.dto.MetamacConceptSchemeDto;
import org.siemac.metamac.srm.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.srm.web.shared.concept.RejectConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.RejectConceptSchemeResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class RejectConceptSchemeActionHandler extends SecurityActionHandler<RejectConceptSchemeAction, RejectConceptSchemeResult> {

    public RejectConceptSchemeActionHandler() {
        super(RejectConceptSchemeAction.class);
    }

    @Override
    public RejectConceptSchemeResult executeSecurityAction(RejectConceptSchemeAction action) throws ActionException {
        try {
            MetamacConceptSchemeDto scheme = ConceptSchemeService.reject(action.getId());
            return new RejectConceptSchemeResult(scheme);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
