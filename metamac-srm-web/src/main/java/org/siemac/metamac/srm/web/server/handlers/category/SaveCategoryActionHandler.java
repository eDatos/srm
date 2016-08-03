package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.SaveCategoryAction;
import org.siemac.metamac.srm.web.shared.category.SaveCategoryResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveCategoryActionHandler extends SecurityActionHandler<SaveCategoryAction, SaveCategoryResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveCategoryActionHandler() {
        super(SaveCategoryAction.class);
    }

    @Override
    public SaveCategoryResult executeSecurityAction(SaveCategoryAction action) throws ActionException {
        try {
            CategoryMetamacDto categoryToSave = action.getCategoryToSave();
            CategoryMetamacDto savedCategoryDto = null;
            if (categoryToSave.getId() == null) {
                // Create
                savedCategoryDto = srmCoreServiceFacade.createCategory(ServiceContextHolder.getCurrentServiceContext(), categoryToSave);
            } else {
                // Update
                savedCategoryDto = srmCoreServiceFacade.updateCategory(ServiceContextHolder.getCurrentServiceContext(), categoryToSave);
            }
            return new SaveCategoryResult(savedCategoryDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
