package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.ProcStatus;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.server.rest.StatisticalOperationsRestInternalFacade;
import org.siemac.metamac.srm.web.shared.WebMessageExceptionsConstants;
import org.siemac.metamac.srm.web.shared.concept.UpdateConceptSchemeProcStatusAction;
import org.siemac.metamac.srm.web.shared.concept.UpdateConceptSchemeProcStatusResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateConceptSchemeProcStatusActionHandler extends SecurityActionHandler<UpdateConceptSchemeProcStatusAction, UpdateConceptSchemeProcStatusResult> {

    @Autowired
    private SrmCoreServiceFacade                    srmCoreServiceFacade;

    @Autowired
    private StatisticalOperationsRestInternalFacade statisticalOperationsRestInternalFacade;

    public UpdateConceptSchemeProcStatusActionHandler() {
        super(UpdateConceptSchemeProcStatusAction.class);
    }

    @Override
    public UpdateConceptSchemeProcStatusResult executeSecurityAction(UpdateConceptSchemeProcStatusAction action) throws ActionException {
        try {
            ConceptSchemeMetamacDto conceptSchemeToUpdateStatus = action.getConceptSchemeMetamacDto();

            ConceptSchemeMetamacDto scheme = null;
            if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.sendConceptSchemeToProductionValidation(ServiceContextHolder.getCurrentServiceContext(), conceptSchemeToUpdateStatus.getUrn());
            } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.sendConceptSchemeToDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(), conceptSchemeToUpdateStatus.getUrn());
            } else if (ProcStatusEnum.VALIDATION_REJECTED.equals(action.getNextProcStatus())) {
                if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(conceptSchemeToUpdateStatus.getLifeCycle().getProcStatus())) {
                    scheme = srmCoreServiceFacade.rejectConceptSchemeProductionValidation(ServiceContextHolder.getCurrentServiceContext(), conceptSchemeToUpdateStatus.getUrn());
                } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(conceptSchemeToUpdateStatus.getLifeCycle().getProcStatus())) {
                    scheme = srmCoreServiceFacade.rejectConceptSchemeDiffusionValidation(ServiceContextHolder.getCurrentServiceContext(), conceptSchemeToUpdateStatus.getUrn());
                }
            } else if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.publishConceptSchemeInternally(ServiceContextHolder.getCurrentServiceContext(), conceptSchemeToUpdateStatus.getUrn(), true); // FIXME Set the forceFinal
                                                                                                                                                                           // parameter
            } else if (ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {
                // If the concept scheme type is OPERATION, check that the associated statistical operation is externally published
                if (ConceptSchemeTypeEnum.OPERATION.equals(action.getConceptSchemeMetamacDto().getType())) {
                    if (conceptSchemeToUpdateStatus.getRelatedOperation() != null) {
                        Operation operation = statisticalOperationsRestInternalFacade.retrieveOperation(conceptSchemeToUpdateStatus.getRelatedOperation().getCode());
                        if (!ProcStatus.PUBLISH_EXTERNALLY.equals(operation.getProcStatus())) {
                            throw new MetamacWebException(WebMessageExceptionsConstants.MAINTAINABLE_ARTEFACT_ERROR_RELATED_OPERATION_NOT_EXTERNALLY_PUBLISHED,
                                    "Concept scheme cannot be externally published because the related operation is not externally published");
                        }
                    }
                }
                scheme = srmCoreServiceFacade.publishConceptSchemeExternally(ServiceContextHolder.getCurrentServiceContext(), conceptSchemeToUpdateStatus.getUrn());
            }
            return new UpdateConceptSchemeProcStatusResult(scheme);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
