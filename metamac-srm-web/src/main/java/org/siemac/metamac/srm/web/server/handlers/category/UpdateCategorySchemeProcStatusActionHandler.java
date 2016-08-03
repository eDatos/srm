package org.siemac.metamac.srm.web.server.handlers.category;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.server.rest.NoticesRestInternalFacade;
import org.siemac.metamac.srm.web.shared.category.UpdateCategorySchemeProcStatusAction;
import org.siemac.metamac.srm.web.shared.category.UpdateCategorySchemeProcStatusResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateCategorySchemeProcStatusActionHandler extends SecurityActionHandler<UpdateCategorySchemeProcStatusAction, UpdateCategorySchemeProcStatusResult> {

    @Autowired
    private SrmCoreServiceFacade      srmCoreServiceFacade;

    @Autowired
    private NoticesRestInternalFacade noticesRestInternalFacade;

    public UpdateCategorySchemeProcStatusActionHandler() {
        super(UpdateCategorySchemeProcStatusAction.class);
    }

    @Override
    public UpdateCategorySchemeProcStatusResult executeSecurityAction(UpdateCategorySchemeProcStatusAction action) throws ActionException {

        ServiceContext serviceContext = ServiceContextHolder.getCurrentServiceContext();
        TypeExternalArtefactsEnum resourceType = TypeExternalArtefactsEnum.CATEGORY_SCHEME;

        try {
            CategorySchemeMetamacDto scheme = null;
            if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.sendCategorySchemeToProductionValidation(serviceContext, action.getUrn());
            } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.sendCategorySchemeToDiffusionValidation(serviceContext, action.getUrn());
            } else if (ProcStatusEnum.VALIDATION_REJECTED.equals(action.getNextProcStatus())) {
                if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(action.getCurrentProcStatus())) {
                    scheme = srmCoreServiceFacade.rejectCategorySchemeProductionValidation(serviceContext, action.getUrn());
                } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(action.getCurrentProcStatus())) {
                    scheme = srmCoreServiceFacade.rejectCategorySchemeDiffusionValidation(serviceContext, action.getUrn());
                }
            } else if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.publishCategorySchemeInternally(serviceContext, action.getUrn(), action.getForceLatestFinal());

                try {
                    noticesRestInternalFacade.createInternalPublicationNotification(serviceContext, scheme, resourceType);
                } catch (MetamacWebException e) {
                    return new UpdateCategorySchemeProcStatusResult.Builder(scheme).notificationException(e).build();
                }

            } else if (ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.publishCategorySchemeExternally(serviceContext, action.getUrn());

                try {
                    noticesRestInternalFacade.createExternalPublicationNotification(serviceContext, scheme, resourceType);
                } catch (MetamacWebException e) {
                    return new UpdateCategorySchemeProcStatusResult.Builder(scheme).notificationException(e).build();
                }

            }
            return new UpdateCategorySchemeProcStatusResult(scheme);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
