package org.siemac.metamac.srm.web.server.handlers.code;

import com.gwtplatform.dispatch.shared.ActionException;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.serviceimpl.result.SendStreamMessageResult;
import org.siemac.metamac.srm.web.server.rest.NoticesRestInternalFacade;
import org.siemac.metamac.srm.web.shared.code.ReSendCodelistStreamMessageAction;
import org.siemac.metamac.srm.web.shared.code.ReSendCodelistStreamMessageResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReSendCodelistStreamMessageActionHandler extends SecurityActionHandler<ReSendCodelistStreamMessageAction, ReSendCodelistStreamMessageResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    @Autowired
    private NoticesRestInternalFacade noticesRestInternalFacade;

    public ReSendCodelistStreamMessageActionHandler() {
        super(ReSendCodelistStreamMessageAction.class);
    }

    @Override
    public ReSendCodelistStreamMessageResult executeSecurityAction(ReSendCodelistStreamMessageAction action) throws ActionException {

        ServiceContext serviceContext = ServiceContextHolder.getCurrentServiceContext();

        SendStreamMessageResult result;
        CodelistMetamacDto codelistMetamacDto;
        MetamacWebException codelistStreamMessageException = null;
        try {
            result = srmCoreServiceFacade.resendCodelist(serviceContext, action.getCodelistUrn());
            codelistMetamacDto = srmCoreServiceFacade.retrieveCodelistByUrn(serviceContext, action.getCodelistUrn());
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
            codelistStreamMessageException = WebExceptionUtils.createMetamacWebException(e);
            try {
                noticesRestInternalFacade.createStreamMessageErrorNotification(serviceContext, codelistMetamacDto, TypeExternalArtefactsEnum.CODELIST);
            } catch (MetamacWebException noticeException) {
                codelistStreamMessageException.getWebExceptionItems().addAll(noticeException.getWebExceptionItems());
            }
        }

        return new ReSendCodelistStreamMessageResult(codelistMetamacDto, codelistStreamMessageException);
    }
}