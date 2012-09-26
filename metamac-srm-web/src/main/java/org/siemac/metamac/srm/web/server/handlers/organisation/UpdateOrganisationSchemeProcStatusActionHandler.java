package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.server.mock.MockService;
import org.siemac.metamac.srm.web.shared.organisation.UpdateOrganisationSchemeProcStatusAction;
import org.siemac.metamac.srm.web.shared.organisation.UpdateOrganisationSchemeProcStatusResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateOrganisationSchemeProcStatusActionHandler extends SecurityActionHandler<UpdateOrganisationSchemeProcStatusAction, UpdateOrganisationSchemeProcStatusResult> {

    // @Autowired
    // private SrmCoreServiceFacade srmCoreServiceFacade;

    public UpdateOrganisationSchemeProcStatusActionHandler() {
        super(UpdateOrganisationSchemeProcStatusAction.class);
    }

    @Override
    public UpdateOrganisationSchemeProcStatusResult executeSecurityAction(UpdateOrganisationSchemeProcStatusAction action) throws ActionException {
        // try {
        OrganisationSchemeMetamacDto scheme = MockService.getOrganisationSchemeMetamacDto();
        if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(action.getNextProcStatus())) {
            scheme.getLifeCycle().setProcStatus(ProcStatusEnum.PRODUCTION_VALIDATION);
            // scheme = srmCoreServiceFacade.sendOrganisationSchemeToProductionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
        } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(action.getNextProcStatus())) {
            scheme.getLifeCycle().setProcStatus(ProcStatusEnum.DIFFUSION_VALIDATION);
            // scheme = srmCoreServiceFacade.sendOrganisationSchemeToDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
        } else if (ProcStatusEnum.VALIDATION_REJECTED.equals(action.getNextProcStatus())) {
            if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(action.getCurrentProcStatus())) {
                scheme.getLifeCycle().setProcStatus(ProcStatusEnum.VALIDATION_REJECTED);
                // scheme = srmCoreServiceFacade.rejectOrganisationSchemeProductionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(action.getCurrentProcStatus())) {
                scheme.getLifeCycle().setProcStatus(ProcStatusEnum.VALIDATION_REJECTED);
                // scheme = srmCoreServiceFacade.rejectOrganisationSchemeDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            }
        } else if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {
            scheme.getLifeCycle().setProcStatus(ProcStatusEnum.INTERNALLY_PUBLISHED);
            // scheme = srmCoreServiceFacade.publishOrganisationSchemeInternally(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
        } else if (ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {
            scheme.getLifeCycle().setProcStatus(ProcStatusEnum.EXTERNALLY_PUBLISHED);
            // scheme = srmCoreServiceFacade.publishOrganisationSchemeExternally(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
        }
        return new UpdateOrganisationSchemeProcStatusResult(scheme);
        // } catch (MetamacException e) {
        // throw WebExceptionUtils.createMetamacWebException(e);
        // }
    }

}
