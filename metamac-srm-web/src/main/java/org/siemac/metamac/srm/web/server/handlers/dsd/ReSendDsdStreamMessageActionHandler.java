package org.siemac.metamac.srm.web.server.handlers.dsd;

import com.gwtplatform.dispatch.shared.ActionException;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.serviceimpl.result.SendStreamMessageResult;
import org.siemac.metamac.srm.web.server.rest.NoticesRestInternalFacade;
import org.siemac.metamac.srm.web.shared.dsd.ReSendDsdStreamMessageAction;
import org.siemac.metamac.srm.web.shared.dsd.ReSendDsdStreamMessageResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReSendDsdStreamMessageActionHandler extends SecurityActionHandler<ReSendDsdStreamMessageAction, ReSendDsdStreamMessageResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    @Autowired
    private NoticesRestInternalFacade noticesRestInternalFacade;

    public ReSendDsdStreamMessageActionHandler() {
        super(ReSendDsdStreamMessageAction.class);
    }

    @Override
    public ReSendDsdStreamMessageResult executeSecurityAction(ReSendDsdStreamMessageAction action) throws ActionException {

        ServiceContext serviceContext = ServiceContextHolder.getCurrentServiceContext();

        SendStreamMessageResult result;
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto;
        MetamacWebException dsdStreamMessageException = null;
        try {
            result = srmCoreServiceFacade.resendDataStructureDefinition(serviceContext, action.getDsdUrn());
            dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(serviceContext, action.getDsdUrn());
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
            dsdStreamMessageException = WebExceptionUtils.createMetamacWebException(e);
            try {
                noticesRestInternalFacade.createStreamMessageErrorNotification(serviceContext, dataStructureDefinitionMetamacDto, TypeExternalArtefactsEnum.DATASTRUCTURE);
            } catch (MetamacWebException noticeException) {
                dsdStreamMessageException.getWebExceptionItems().addAll(noticeException.getWebExceptionItems());
            }
        }

        return new ReSendDsdStreamMessageResult(dataStructureDefinitionMetamacDto, dsdStreamMessageException);
    }
}