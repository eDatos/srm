package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.DeleteCategoryAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategoryResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.shared.ActionException;

public class DeleteCategoryActionHandler extends SecurityActionHandler<DeleteCategoryAction, DeleteCategoryResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteCategoryActionHandler() {
        super(DeleteCategoryAction.class);
    }

    @Override
    public DeleteCategoryResult executeSecurityAction(DeleteCategoryAction action) throws ActionException {
        try {
            srmCoreServiceFacade.deleteCategory(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new DeleteCategoryResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
