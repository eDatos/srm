package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.SaveCategorySchemeAction;
import org.siemac.metamac.srm.web.shared.category.SaveCategorySchemeResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveCategorySchemeActionHandler extends SecurityActionHandler<SaveCategorySchemeAction, SaveCategorySchemeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveCategorySchemeActionHandler() {
        super(SaveCategorySchemeAction.class);
    }

    @Override
    public SaveCategorySchemeResult executeSecurityAction(SaveCategorySchemeAction action) throws ActionException {
        try {
            CategorySchemeMetamacDto categorySchemeToSave = action.getCategorySchemeToSave();
            CategorySchemeMetamacDto savedCategorySchemeDto = null;
            if (categorySchemeToSave.getId() == null) {
                // Create
                savedCategorySchemeDto = srmCoreServiceFacade.createCategoryScheme(ServiceContextHolder.getCurrentServiceContext(), categorySchemeToSave);
            } else {
                // Update
                savedCategorySchemeDto = srmCoreServiceFacade.updateCategoryScheme(ServiceContextHolder.getCurrentServiceContext(), categorySchemeToSave);
            }
            return new SaveCategorySchemeResult(savedCategorySchemeDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
