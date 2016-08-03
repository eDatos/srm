package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCategorySchemeActionHandler extends SecurityActionHandler<GetCategorySchemeAction, GetCategorySchemeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCategorySchemeActionHandler() {
        super(GetCategorySchemeAction.class);
    }

    @Override
    public GetCategorySchemeResult executeSecurityAction(GetCategorySchemeAction action) throws ActionException {
        try {
            CategorySchemeMetamacDto categorySchemeMetamacDto = srmCoreServiceFacade.retrieveCategorySchemeByUrn(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new GetCategorySchemeResult(categorySchemeMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
