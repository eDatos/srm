package org.siemac.metamac.srm.web.server.handlers.code;

import org.apache.commons.lang.BooleanUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.server.rest.NoticesRestInternalFacade;
import org.siemac.metamac.srm.web.shared.code.UpdateCodelistProcStatusAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodelistProcStatusResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateCodelistProcStatusActionHandler extends SecurityActionHandler<UpdateCodelistProcStatusAction, UpdateCodelistProcStatusResult> {

    @Autowired
    private SrmCoreServiceFacade      srmCoreServiceFacade;

    @Autowired
    private NoticesRestInternalFacade noticesRestInternalFacade;

    public UpdateCodelistProcStatusActionHandler() {
        super(UpdateCodelistProcStatusAction.class);
    }

    @Override
    public UpdateCodelistProcStatusResult executeSecurityAction(UpdateCodelistProcStatusAction action) throws ActionException {

        ServiceContext serviceContext = ServiceContextHolder.getCurrentServiceContext();

        try {
            CodelistMetamacDto codelistMetamacDto = null;
            if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(action.getNextProcStatus())) {
                codelistMetamacDto = srmCoreServiceFacade.sendCodelistToProductionValidation(serviceContext, action.getUrn());
            } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(action.getNextProcStatus())) {
                codelistMetamacDto = srmCoreServiceFacade.sendCodelistToDiffusionValidation(serviceContext, action.getUrn());
            } else if (ProcStatusEnum.VALIDATION_REJECTED.equals(action.getNextProcStatus())) {
                if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(action.getCurrentProcStatus())) {
                    codelistMetamacDto = srmCoreServiceFacade.rejectCodelistProductionValidation(serviceContext, action.getUrn());
                } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(action.getCurrentProcStatus())) {
                    codelistMetamacDto = srmCoreServiceFacade.rejectCodelistDiffusionValidation(serviceContext, action.getUrn());
                }
            } else if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {

                // The internal publication may be planned in background

                TaskInfo result = srmCoreServiceFacade.publishCodelistInternally(serviceContext, action.getUrn(), action.getForceLatestFinal());
                if (BooleanUtils.isTrue(result.getIsPlannedInBackground())) {
                    codelistMetamacDto = srmCoreServiceFacade.retrieveCodelistByUrn(serviceContext, action.getUrn());
                } else {
                    codelistMetamacDto = srmCoreServiceFacade.retrieveCodelistByUrn(serviceContext, result.getUrnResult());

                    try {
                        noticesRestInternalFacade.createInternalPublicationNotification(serviceContext, codelistMetamacDto, TypeExternalArtefactsEnum.CODELIST);
                    } catch (MetamacWebException e) {
                        return new UpdateCodelistProcStatusResult.Builder(codelistMetamacDto).notificationException(e).build();
                    }
                }

            } else if (ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {
                codelistMetamacDto = srmCoreServiceFacade.publishCodelistExternally(serviceContext, action.getUrn());

                try {
                    noticesRestInternalFacade.createExternalPublicationNotification(serviceContext, codelistMetamacDto, TypeExternalArtefactsEnum.CODELIST);
                } catch (MetamacWebException e) {
                    return new UpdateCodelistProcStatusResult.Builder(codelistMetamacDto).notificationException(e).build();
                }
            }
            return new UpdateCodelistProcStatusResult(codelistMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
