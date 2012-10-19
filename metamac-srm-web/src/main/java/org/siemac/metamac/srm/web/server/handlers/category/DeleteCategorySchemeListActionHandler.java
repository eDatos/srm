package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorySchemeListAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategorySchemeListResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteCategorySchemeListActionHandler extends SecurityActionHandler<DeleteCategorySchemeListAction, DeleteCategorySchemeListResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteCategorySchemeListActionHandler() {
        super(DeleteCategorySchemeListAction.class);
    }

    @Override
    public DeleteCategorySchemeListResult executeSecurityAction(DeleteCategorySchemeListAction action) throws ActionException {
        try {
            for (String urn : action.getUrns()) {
                srmCoreServiceFacade.deleteCategoryScheme(ServiceContextHolder.getCurrentServiceContext(), urn);
            }
            return new DeleteCategorySchemeListResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
