package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.ExportCategoriesAction;
import org.siemac.metamac.srm.web.shared.category.ExportCategoriesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class ExportCategoriesActionHandler extends SecurityActionHandler<ExportCategoriesAction, ExportCategoriesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public ExportCategoriesActionHandler() {
        super(ExportCategoriesAction.class);
    }

    @Override
    public ExportCategoriesResult executeSecurityAction(ExportCategoriesAction action) throws ActionException {
        try {
            String fileName = srmCoreServiceFacade.exportCategoriesTsv(ServiceContextHolder.getCurrentServiceContext(), action.getCategorySchemeUrn());
            return new ExportCategoriesResult(fileName);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
