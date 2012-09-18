package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.UpdateConceptSchemeProcStatusAction;
import org.siemac.metamac.srm.web.shared.concept.UpdateConceptSchemeProcStatusResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateConceptSchemeProcStatusActionHandler extends SecurityActionHandler<UpdateConceptSchemeProcStatusAction, UpdateConceptSchemeProcStatusResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public UpdateConceptSchemeProcStatusActionHandler() {
        super(UpdateConceptSchemeProcStatusAction.class);
    }

    @Override
    public UpdateConceptSchemeProcStatusResult executeSecurityAction(UpdateConceptSchemeProcStatusAction action) throws ActionException {
        try {
            ConceptSchemeMetamacDto scheme = null;
            if (ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.sendConceptSchemeToProductionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            } else if (ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.sendConceptSchemeToDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            } else if (ItemSchemeMetamacProcStatusEnum.VALIDATION_REJECTED.equals(action.getNextProcStatus())) {
                if (ItemSchemeMetamacProcStatusEnum.PRODUCTION_VALIDATION.equals(action.getCurrentProcStatus())) {
                    scheme = srmCoreServiceFacade.rejectConceptSchemeProductionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
                } else if (ItemSchemeMetamacProcStatusEnum.DIFFUSION_VALIDATION.equals(action.getCurrentProcStatus())) {
                    scheme = srmCoreServiceFacade.rejectConceptSchemeDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
                }
            } else if (ItemSchemeMetamacProcStatusEnum.INTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.publishConceptSchemeInternally(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            } else if (ItemSchemeMetamacProcStatusEnum.EXTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.publishConceptSchemeExternally(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            }
            return new UpdateConceptSchemeProcStatusResult(scheme);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
