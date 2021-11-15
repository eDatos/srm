package org.siemac.metamac.srm.web.server.handlers.organisation;

import com.gwtplatform.dispatch.shared.ActionException;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.core.serviceimpl.result.SendStreamMessageResult;
import org.siemac.metamac.srm.web.server.rest.NoticesRestInternalFacade;
import org.siemac.metamac.srm.web.shared.organisation.ReSendOrganisationSchemeStreamMessageAction;
import org.siemac.metamac.srm.web.shared.organisation.ReSendOrganisationSchemeStreamMessageResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReSendOrganisationSchemeStreamMessageActionHandler extends SecurityActionHandler<ReSendOrganisationSchemeStreamMessageAction, ReSendOrganisationSchemeStreamMessageResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    @Autowired
    private NoticesRestInternalFacade noticesRestInternalFacade;

    public ReSendOrganisationSchemeStreamMessageActionHandler() {
        super(ReSendOrganisationSchemeStreamMessageAction.class);
    }

    @Override
    public ReSendOrganisationSchemeStreamMessageResult executeSecurityAction(ReSendOrganisationSchemeStreamMessageAction action) throws ActionException {

        ServiceContext serviceContext = ServiceContextHolder.getCurrentServiceContext();

        SendStreamMessageResult result;
        OrganisationSchemeMetamacDto organisationSchemeMetamacDto;
        MetamacWebException organisationSchemeStreamMessageException = null;
        try {
            result = srmCoreServiceFacade.resendOrganisationScheme(serviceContext, action.getOrganisationSchemeUrn());
            organisationSchemeMetamacDto = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(serviceContext, action.getOrganisationSchemeUrn());
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
            organisationSchemeStreamMessageException = WebExceptionUtils.createMetamacWebException(e);
            try {
                noticesRestInternalFacade.createStreamMessageErrorNotification(serviceContext, organisationSchemeMetamacDto, TypeExternalArtefactsEnum.ORGANISATION_SCHEME);
            } catch (MetamacWebException noticeException) {
                organisationSchemeStreamMessageException.getWebExceptionItems().addAll(noticeException.getWebExceptionItems());
            }
        }

        return new ReSendOrganisationSchemeStreamMessageResult(organisationSchemeMetamacDto, organisationSchemeStreamMessageException);
    }
}