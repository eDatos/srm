package org.siemac.metamac.srm.web.server.handlers.dsd;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.UpdateDsdProcStatusAction;
import org.siemac.metamac.srm.web.shared.dsd.UpdateDsdProcStatusResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateDsdProcStatusActionHandlder extends SecurityActionHandler<UpdateDsdProcStatusAction, UpdateDsdProcStatusResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public UpdateDsdProcStatusActionHandlder() {
        super(UpdateDsdProcStatusAction.class);
    }

    @Override
    public UpdateDsdProcStatusResult executeSecurityAction(UpdateDsdProcStatusAction action) throws ActionException {
        try {
            DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;
            if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(action.getNextProcStatus())) {
                dataStructureDefinitionMetamacDto = srmCoreServiceFacade.sendDataStructureDefinitionToProductionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(action.getNextProcStatus())) {
                dataStructureDefinitionMetamacDto = srmCoreServiceFacade.sendDataStructureDefinitionToDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            } else if (ProcStatusEnum.VALIDATION_REJECTED.equals(action.getNextProcStatus())) {
                if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(action.getCurrentProcStatus())) {
                    dataStructureDefinitionMetamacDto = srmCoreServiceFacade.rejectDataStructureDefinitionProductionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
                } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(action.getCurrentProcStatus())) {
                    dataStructureDefinitionMetamacDto = srmCoreServiceFacade.rejectDataStructureDefinitionDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
                }
            } else if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {
                dataStructureDefinitionMetamacDto = srmCoreServiceFacade.publishDataStructureDefinitionInternally(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            } else if (ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {
                dataStructureDefinitionMetamacDto = srmCoreServiceFacade.publishDataStructureDefinitionExternally(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            }
            return new UpdateDsdProcStatusResult(dataStructureDefinitionMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
