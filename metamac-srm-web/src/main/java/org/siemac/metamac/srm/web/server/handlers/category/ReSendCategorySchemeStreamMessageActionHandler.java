package org.siemac.metamac.srm.web.server.handlers.category;

import com.gwtplatform.dispatch.shared.ActionException;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.serviceimpl.result.SendStreamMessageResult;
import org.siemac.metamac.srm.web.server.rest.NoticesRestInternalFacade;
import org.siemac.metamac.srm.web.shared.category.ReSendCategorySchemeStreamMessageAction;
import org.siemac.metamac.srm.web.shared.category.ReSendCategorySchemeStreamMessageResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReSendCategorySchemeStreamMessageActionHandler extends SecurityActionHandler<ReSendCategorySchemeStreamMessageAction, ReSendCategorySchemeStreamMessageResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    @Autowired
    private NoticesRestInternalFacade noticesRestInternalFacade;

    public ReSendCategorySchemeStreamMessageActionHandler() {
        super(ReSendCategorySchemeStreamMessageAction.class);
    }

    @Override
    public ReSendCategorySchemeStreamMessageResult executeSecurityAction(ReSendCategorySchemeStreamMessageAction action) throws ActionException {

        ServiceContext serviceContext = ServiceContextHolder.getCurrentServiceContext();

        SendStreamMessageResult result;
        CategorySchemeMetamacDto categorySchemeMetamacDto;
        MetamacWebException categorySchemeStreamMessageException = null;
        try {
            result = srmCoreServiceFacade.resendCategoryScheme(serviceContext, action.getCategorySchemeUrn());
            categorySchemeMetamacDto = srmCoreServiceFacade.retrieveCategorySchemeByUrn(serviceContext, action.getCategorySchemeUrn());
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
            categorySchemeStreamMessageException = WebExceptionUtils.createMetamacWebException(e);
            try {
                noticesRestInternalFacade.createStreamMessageErrorNotification(serviceContext, categorySchemeMetamacDto, TypeExternalArtefactsEnum.CATEGORY_SCHEME);
            } catch (MetamacWebException noticeException) {
                categorySchemeStreamMessageException.getWebExceptionItems().addAll(noticeException.getWebExceptionItems());
            }
        }

        return new ReSendCategorySchemeStreamMessageResult(categorySchemeMetamacDto, categorySchemeStreamMessageException);
    }
}