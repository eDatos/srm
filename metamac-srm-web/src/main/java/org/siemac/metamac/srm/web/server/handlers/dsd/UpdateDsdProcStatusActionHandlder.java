package org.siemac.metamac.srm.web.server.handlers.dsd;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.ProcStatus;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.server.rest.StatisticalOperationsRestInternalFacade;
import org.siemac.metamac.srm.web.shared.WebMessageExceptionsConstants;
import org.siemac.metamac.srm.web.shared.dsd.UpdateDsdProcStatusAction;
import org.siemac.metamac.srm.web.shared.dsd.UpdateDsdProcStatusResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateDsdProcStatusActionHandlder extends SecurityActionHandler<UpdateDsdProcStatusAction, UpdateDsdProcStatusResult> {

    @Autowired
    private SrmCoreServiceFacade                    srmCoreServiceFacade;

    @Autowired
    private StatisticalOperationsRestInternalFacade statisticalOperationsRestInternalFacade;

    public UpdateDsdProcStatusActionHandlder() {
        super(UpdateDsdProcStatusAction.class);
    }

    @Override
    public UpdateDsdProcStatusResult executeSecurityAction(UpdateDsdProcStatusAction action) throws ActionException {
        try {
            DataStructureDefinitionMetamacDto dsdToUpdateStatus = action.getDataStructureDefinitionMetamacDtoToUpdateStatus();

            DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;
            if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(action.getNextProcStatus())) {
                dataStructureDefinitionMetamacDto = srmCoreServiceFacade.sendDataStructureDefinitionToProductionValidation(ServiceContextHolder.getCurrentServiceContext(), dsdToUpdateStatus.getUrn());
            } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(action.getNextProcStatus())) {
                dataStructureDefinitionMetamacDto = srmCoreServiceFacade.sendDataStructureDefinitionToDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(), dsdToUpdateStatus.getUrn());
            } else if (ProcStatusEnum.VALIDATION_REJECTED.equals(action.getNextProcStatus())) {
                if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(dsdToUpdateStatus.getLifeCycle().getProcStatus())) {
                    dataStructureDefinitionMetamacDto = srmCoreServiceFacade.rejectDataStructureDefinitionProductionValidation(ServiceContextHolder.getCurrentServiceContext(),
                            dsdToUpdateStatus.getUrn());
                } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(dsdToUpdateStatus.getLifeCycle().getProcStatus())) {
                    dataStructureDefinitionMetamacDto = srmCoreServiceFacade.rejectDataStructureDefinitionDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(),
                            dsdToUpdateStatus.getUrn());
                }
            } else if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {
                dataStructureDefinitionMetamacDto = srmCoreServiceFacade.publishDataStructureDefinitionInternally(ServiceContextHolder.getCurrentServiceContext(), dsdToUpdateStatus.getUrn());
            } else if (ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {
                // Check that the associated statistical operation is externally published
                if (dsdToUpdateStatus.getStatisticalOperation() != null) {
                    Operation operation = statisticalOperationsRestInternalFacade.retrieveOperation(dsdToUpdateStatus.getStatisticalOperation().getCode());
                    if (!ProcStatus.PUBLISH_EXTERNALLY.equals(operation.getProcStatus())) {
                        throw new MetamacWebException(WebMessageExceptionsConstants.MAINTAINABLE_ARTEFACT_ERROR_RELATED_OPERATION_NOT_EXTERNALLY_PUBLISHED,
                                "DSD cannot be externally published because the related operation is not externally published");
                    }
                }
                dataStructureDefinitionMetamacDto = srmCoreServiceFacade.publishDataStructureDefinitionExternally(ServiceContextHolder.getCurrentServiceContext(), dsdToUpdateStatus.getUrn());
            }
            return new UpdateDsdProcStatusResult(dataStructureDefinitionMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
