package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.CreateCategorySchemeTemporalVersionAction;
import org.siemac.metamac.srm.web.shared.category.CreateCategorySchemeTemporalVersionResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CreateCategorySchemeTemporalVersionActionHandler extends SecurityActionHandler<CreateCategorySchemeTemporalVersionAction, CreateCategorySchemeTemporalVersionResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CreateCategorySchemeTemporalVersionActionHandler() {
        super(CreateCategorySchemeTemporalVersionAction.class);
    }

    @Override
    public CreateCategorySchemeTemporalVersionResult executeSecurityAction(CreateCategorySchemeTemporalVersionAction action) throws ActionException {
        try {
            CategorySchemeMetamacDto categorySchemeMetamacDto = srmCoreServiceFacade.createTemporalVersionCategoryScheme(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new CreateCategorySchemeTemporalVersionResult(categorySchemeMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
