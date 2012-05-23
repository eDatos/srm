package org.siemac.metamac.internal.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.internal.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.internal.web.shared.VersioningConceptSchemeAction;
import org.siemac.metamac.internal.web.shared.VersioningConceptSchemeResult;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class VersioningConceptSchemeActionHandler extends AbstractActionHandler<VersioningConceptSchemeAction, VersioningConceptSchemeResult> {

    public VersioningConceptSchemeActionHandler() {
        super(VersioningConceptSchemeAction.class);
    }

    @Override
    public VersioningConceptSchemeResult execute(VersioningConceptSchemeAction action, ExecutionContext context) throws ActionException {
        try {
            ConceptSchemeDto scheme = ConceptSchemeService.versioning(action.getConceptSchemeUuid());
            return new VersioningConceptSchemeResult(scheme);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(VersioningConceptSchemeAction action, VersioningConceptSchemeResult result, ExecutionContext context) throws ActionException {
        // NOTHING
    }

}
