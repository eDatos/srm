package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.dto.MetamacConceptSchemeDto;
import org.siemac.metamac.srm.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.srm.web.shared.concept.VersionConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.VersionConceptSchemeResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class VersionConceptSchemeActionHandler extends SecurityActionHandler<VersionConceptSchemeAction, VersionConceptSchemeResult> {

    public VersionConceptSchemeActionHandler() {
        super(VersionConceptSchemeAction.class);
    }

    @Override
    public VersionConceptSchemeResult executeSecurityAction(VersionConceptSchemeAction action) throws ActionException {
        try {
            MetamacConceptSchemeDto scheme = ConceptSchemeService.versioning(action.getId());
            return new VersionConceptSchemeResult(scheme);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
