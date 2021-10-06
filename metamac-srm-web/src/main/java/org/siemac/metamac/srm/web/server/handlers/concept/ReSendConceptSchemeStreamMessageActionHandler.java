package org.siemac.metamac.srm.web.server.handlers.concept;

import com.gwtplatform.dispatch.shared.ActionException;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.serviceimpl.result.SendStreamMessageResult;
import org.siemac.metamac.srm.web.server.rest.NoticesRestInternalFacade;
import org.siemac.metamac.srm.web.shared.concept.ReSendConceptSchemeStreamMessageAction;
import org.siemac.metamac.srm.web.shared.concept.ReSendConceptSchemeStreamMessageResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReSendConceptSchemeStreamMessageActionHandler extends SecurityActionHandler<ReSendConceptSchemeStreamMessageAction, ReSendConceptSchemeStreamMessageResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    @Autowired
    private NoticesRestInternalFacade noticesRestInternalFacade;

    public ReSendConceptSchemeStreamMessageActionHandler() {
        super(ReSendConceptSchemeStreamMessageAction.class);
    }

    @Override
    public ReSendConceptSchemeStreamMessageResult executeSecurityAction(ReSendConceptSchemeStreamMessageAction action) throws ActionException {

        ServiceContext serviceContext = ServiceContextHolder.getCurrentServiceContext();

        SendStreamMessageResult result;
        ConceptSchemeMetamacDto conceptSchemeMetamacDto;
        MetamacWebException conceptSchemeStreamMessageException = null;
        try {
            result = srmCoreServiceFacade.resendConceptScheme(serviceContext, action.getConceptSchemeUrn());
            conceptSchemeMetamacDto = srmCoreServiceFacade.retrieveConceptSchemeByUrn(serviceContext, action.getConceptSchemeUrn());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }

        if (!result.isOk()) {
            MetamacException e = result.getMainException();
            List<MetamacExceptionItem> list = new ArrayList<>();
            for (MetamacException exception : result.getSecondaryExceptions()) {
                list.addAll(exception.getExceptionItems());
            }
            e.getExceptionItems().addAll(list);
            conceptSchemeStreamMessageException = WebExceptionUtils.createMetamacWebException(e);
            try {
                noticesRestInternalFacade.createStreamMessageErrorNotification(serviceContext, conceptSchemeMetamacDto, TypeExternalArtefactsEnum.CONCEPT_SCHEME);
            } catch (MetamacWebException noticeException) {
                conceptSchemeStreamMessageException.getWebExceptionItems().addAll(noticeException.getWebExceptionItems());
            }
        }

        return new ReSendConceptSchemeStreamMessageResult(conceptSchemeMetamacDto, conceptSchemeStreamMessageException);
    }
}