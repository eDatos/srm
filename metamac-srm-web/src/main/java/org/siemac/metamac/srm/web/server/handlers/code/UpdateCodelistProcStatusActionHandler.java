package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.UpdateCodelistProcStatusAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodelistProcStatusResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateCodelistProcStatusActionHandler extends SecurityActionHandler<UpdateCodelistProcStatusAction, UpdateCodelistProcStatusResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public UpdateCodelistProcStatusActionHandler() {
        super(UpdateCodelistProcStatusAction.class);
    }

    @Override
    public UpdateCodelistProcStatusResult executeSecurityAction(UpdateCodelistProcStatusAction action) throws ActionException {
        try {
            CodelistMetamacDto scheme = null;
            if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.sendCodelistToProductionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.sendCodelistToDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            } else if (ProcStatusEnum.VALIDATION_REJECTED.equals(action.getNextProcStatus())) {
                if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(action.getCurrentProcStatus())) {
                    scheme = srmCoreServiceFacade.rejectCodelistProductionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
                } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(action.getCurrentProcStatus())) {
                    scheme = srmCoreServiceFacade.rejectCodelistDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
                }
            } else if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.publishCodelistInternally(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            } else if (ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.publishCodelistExternally(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            }
            return new UpdateCodelistProcStatusResult(scheme);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
