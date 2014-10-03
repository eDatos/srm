package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.server.rest.NoticesRestInternalFacade;
import org.siemac.metamac.srm.web.server.utils.ExternalItemUtils;
import org.siemac.metamac.srm.web.shared.organisation.UpdateOrganisationSchemeProcStatusAction;
import org.siemac.metamac.srm.web.shared.organisation.UpdateOrganisationSchemeProcStatusResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateOrganisationSchemeProcStatusActionHandler extends SecurityActionHandler<UpdateOrganisationSchemeProcStatusAction, UpdateOrganisationSchemeProcStatusResult> {

    @Autowired
    private NoticesRestInternalFacade noticesRestInternalFacade;

    @Autowired
    private SrmCoreServiceFacade      srmCoreServiceFacade;

    public UpdateOrganisationSchemeProcStatusActionHandler() {
        super(UpdateOrganisationSchemeProcStatusAction.class);
    }

    @Override
    public UpdateOrganisationSchemeProcStatusResult executeSecurityAction(UpdateOrganisationSchemeProcStatusAction action) throws ActionException {

        ServiceContext serviceContext = ServiceContextHolder.getCurrentServiceContext();
        TypeExternalArtefactsEnum organisationSchemeType = ExternalItemUtils.getOrganisationSchemeType(action.getOrganisationSchemeType());

        try {
            OrganisationSchemeMetamacDto scheme = null;
            if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.sendOrganisationSchemeToProductionValidation(serviceContext, action.getUrn());
            } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.sendOrganisationSchemeToDiffusionValidation(serviceContext, action.getUrn());
            } else if (ProcStatusEnum.VALIDATION_REJECTED.equals(action.getNextProcStatus())) {
                if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(action.getCurrentProcStatus())) {
                    scheme = srmCoreServiceFacade.rejectOrganisationSchemeProductionValidation(serviceContext, action.getUrn());
                } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(action.getCurrentProcStatus())) {
                    scheme = srmCoreServiceFacade.rejectOrganisationSchemeDiffusionValidation(serviceContext, action.getUrn());
                }
            } else if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {

                scheme = srmCoreServiceFacade.publishOrganisationSchemeInternally(serviceContext, action.getUrn(), action.getForceLastestFinal());
                try {
                    noticesRestInternalFacade.createInternalPublicationNotification(serviceContext, scheme, organisationSchemeType);
                } catch (MetamacWebException e) {
                    return new UpdateOrganisationSchemeProcStatusResult.Builder(scheme).notificationException(e).build();
                }

            } else if (ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {

                scheme = srmCoreServiceFacade.publishOrganisationSchemeExternally(serviceContext, action.getUrn());
                try {
                    noticesRestInternalFacade.createExternalPublicationNotification(serviceContext, scheme, organisationSchemeType);
                } catch (MetamacWebException e) {
                    return new UpdateOrganisationSchemeProcStatusResult.Builder(scheme).notificationException(e).build();
                }
            }
            return new UpdateOrganisationSchemeProcStatusResult(scheme);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
