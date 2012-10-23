package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.GetCategoryAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoryResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCategoryActionHandler extends SecurityActionHandler<GetCategoryAction, GetCategoryResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCategoryActionHandler() {
        super(GetCategoryAction.class);
    }

    @Override
    public GetCategoryResult executeSecurityAction(GetCategoryAction action) throws ActionException {
        try {
            CategoryMetamacDto categoryMetamacDto = srmCoreServiceFacade.retrieveCategoryByUrn(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new GetCategoryResult(categoryMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
