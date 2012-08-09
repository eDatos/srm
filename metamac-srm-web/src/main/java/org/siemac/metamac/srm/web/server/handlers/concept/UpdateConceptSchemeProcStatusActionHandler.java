package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.siemac.metamac.srm.web.server.mock.ConceptSchemeService;
import org.siemac.metamac.srm.web.shared.concept.UpdateConceptSchemeProcStatusAction;
import org.siemac.metamac.srm.web.shared.concept.UpdateConceptSchemeProcStatusResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateConceptSchemeProcStatusActionHandler extends SecurityActionHandler<UpdateConceptSchemeProcStatusAction, UpdateConceptSchemeProcStatusResult> {

    public UpdateConceptSchemeProcStatusActionHandler() {
        super(UpdateConceptSchemeProcStatusAction.class);
    }

    @Override
    public UpdateConceptSchemeProcStatusResult executeSecurityAction(UpdateConceptSchemeProcStatusAction action) throws ActionException {
        try {
            ConceptSchemeMetamacDto scheme = null;
            if (ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION.equals(action.getProcStatus())) {
                scheme = ConceptSchemeService.sendToProductionValidation(action.getId());
            } else if (ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION.equals(action.getProcStatus())) {
                scheme = ConceptSchemeService.sendToDiffusionValidation(action.getId());
            } else if (ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED.equals(action.getProcStatus())) {
                scheme = ConceptSchemeService.reject(action.getId());
            } else if (ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED.equals(action.getProcStatus())) {
                scheme = ConceptSchemeService.publishInternally(action.getId());
            } else if (ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED.equals(action.getProcStatus())) {
                scheme = ConceptSchemeService.publishExternally(action.getId());
            }
            return new UpdateConceptSchemeProcStatusResult(scheme);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
