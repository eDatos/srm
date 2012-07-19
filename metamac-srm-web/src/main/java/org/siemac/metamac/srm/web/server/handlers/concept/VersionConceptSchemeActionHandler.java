package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.srm.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.srm.web.shared.VersionConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.VersionConceptSchemeResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class VersionConceptSchemeActionHandler extends SecurityActionHandler<VersionConceptSchemeAction, VersionConceptSchemeResult> {

    public VersionConceptSchemeActionHandler() {
        super(VersionConceptSchemeAction.class);
    }

    @Override
    public VersionConceptSchemeResult executeSecurityAction(VersionConceptSchemeAction action) throws ActionException {
        try {
            ConceptSchemeDto scheme = ConceptSchemeService.versioning(action.getId());
            return new VersionConceptSchemeResult(scheme);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(VersionConceptSchemeAction action, VersionConceptSchemeResult result, ExecutionContext context) throws ActionException {
        // NOTHING
    }

}
