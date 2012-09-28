package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.server.mock.MockService;
import org.siemac.metamac.srm.web.shared.organisation.UpdateOrganisationSchemeProcStatusAction;
import org.siemac.metamac.srm.web.shared.organisation.UpdateOrganisationSchemeProcStatusResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateOrganisationSchemeProcStatusActionHandler extends SecurityActionHandler<UpdateOrganisationSchemeProcStatusAction, UpdateOrganisationSchemeProcStatusResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public UpdateOrganisationSchemeProcStatusActionHandler() {
        super(UpdateOrganisationSchemeProcStatusAction.class);
    }

    @Override
    public UpdateOrganisationSchemeProcStatusResult executeSecurityAction(UpdateOrganisationSchemeProcStatusAction action) throws ActionException {
        try {
            OrganisationSchemeMetamacDto scheme = MockService.getOrganisationSchemeMetamacDto();
            if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.sendOrganisationSchemeToProductionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.sendOrganisationSchemeToDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            } else if (ProcStatusEnum.VALIDATION_REJECTED.equals(action.getNextProcStatus())) {
                if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(action.getCurrentProcStatus())) {
                    scheme = srmCoreServiceFacade.rejectOrganisationSchemeProductionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
                } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(action.getCurrentProcStatus())) {
                    scheme = srmCoreServiceFacade.rejectOrganisationSchemeDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
                }
            } else if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.publishOrganisationSchemeInternally(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            } else if (ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.publishOrganisationSchemeExternally(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            }
            return new UpdateOrganisationSchemeProcStatusResult(scheme);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
