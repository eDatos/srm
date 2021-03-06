package org.siemac.metamac.srm.web.server.handlers.concept;

import static org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum.CONCEPT_SCHEME;

import java.util.Locale;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.lang.shared.LocaleConstants;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.ProcStatus;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.server.rest.NoticesRestInternalFacade;
import org.siemac.metamac.srm.web.server.rest.StatisticalOperationsRestInternalFacade;
import org.siemac.metamac.srm.web.shared.WebMessageExceptionsConstants;
import org.siemac.metamac.srm.web.shared.concept.UpdateConceptSchemeProcStatusAction;
import org.siemac.metamac.srm.web.shared.concept.UpdateConceptSchemeProcStatusResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.server.utils.WebTranslateExceptions;
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

    @Autowired
    private WebTranslateExceptions                  webTranslateExceptions;

    @Autowired
    private NoticesRestInternalFacade               noticesRestInternalFacade;

    public UpdateConceptSchemeProcStatusActionHandler() {
        super(UpdateConceptSchemeProcStatusAction.class);
    }

    @Override
    public UpdateConceptSchemeProcStatusResult executeSecurityAction(UpdateConceptSchemeProcStatusAction action) throws ActionException {
        try {

            ServiceContext serviceContext = ServiceContextHolder.getCurrentServiceContext();

            ConceptSchemeMetamacDto conceptSchemeToUpdateStatus = action.getConceptSchemeMetamacDto();

            ConceptSchemeMetamacDto scheme = null;
            if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.sendConceptSchemeToProductionValidation(serviceContext, conceptSchemeToUpdateStatus.getUrn());
            } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.sendConceptSchemeToDiffusionValidation(serviceContext, conceptSchemeToUpdateStatus.getUrn());
            } else if (ProcStatusEnum.VALIDATION_REJECTED.equals(action.getNextProcStatus())) {
                if (ProcStatusEnum.PRODUCTION_VALIDATION.equals(conceptSchemeToUpdateStatus.getLifeCycle().getProcStatus())) {
                    scheme = srmCoreServiceFacade.rejectConceptSchemeProductionValidation(serviceContext, conceptSchemeToUpdateStatus.getUrn());
                } else if (ProcStatusEnum.DIFFUSION_VALIDATION.equals(conceptSchemeToUpdateStatus.getLifeCycle().getProcStatus())) {
                    scheme = srmCoreServiceFacade.rejectConceptSchemeDiffusionValidation(serviceContext, conceptSchemeToUpdateStatus.getUrn());
                }
            } else if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {
                scheme = srmCoreServiceFacade.publishConceptSchemeInternally(serviceContext, conceptSchemeToUpdateStatus.getUrn(), action.getForceLatestFinal());

                try {
                    noticesRestInternalFacade.createInternalPublicationNotification(serviceContext, scheme, CONCEPT_SCHEME);
                } catch (MetamacWebException e) {
                    return new UpdateConceptSchemeProcStatusResult.Builder(scheme).notificationException(e).build();
                }

            } else if (ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(action.getNextProcStatus())) {
                // Check that the associated statistical operation is externally published
                if (conceptSchemeToUpdateStatus.getRelatedOperation() != null) {
                    Operation operation = statisticalOperationsRestInternalFacade.retrieveOperation(serviceContext, conceptSchemeToUpdateStatus.getRelatedOperation().getCode());
                    if (!ProcStatus.EXTERNALLY_PUBLISHED.equals(operation.getProcStatus())) {
                        throwStatisticalOperationNotExternallyPublishedException(serviceContext);
                    }
                }
                scheme = srmCoreServiceFacade.publishConceptSchemeExternally(serviceContext, conceptSchemeToUpdateStatus.getUrn());

                try {
                    noticesRestInternalFacade.createExternalPublicationNotification(serviceContext, scheme, CONCEPT_SCHEME);
                } catch (MetamacWebException e) {
                    return new UpdateConceptSchemeProcStatusResult.Builder(scheme).notificationException(e).build();
                }
            }
            return new UpdateConceptSchemeProcStatusResult(scheme);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    private void throwStatisticalOperationNotExternallyPublishedException(ServiceContext serviceContext) throws MetamacWebException {
        throwMetamacWebException(serviceContext, WebMessageExceptionsConstants.MAINTAINABLE_ARTEFACT_ERROR_RELATED_OPERATION_NOT_EXTERNALLY_PUBLISHED);
    }

    private void throwMetamacWebException(ServiceContext serviceContext, String exceptionCode) throws MetamacWebException {
        Locale locale = (Locale) serviceContext.getProperty(LocaleConstants.locale);
        String exceptionnMessage = webTranslateExceptions.getTranslatedMessage(exceptionCode, locale);

        throw new MetamacWebException(exceptionCode, exceptionnMessage);
    }
}
