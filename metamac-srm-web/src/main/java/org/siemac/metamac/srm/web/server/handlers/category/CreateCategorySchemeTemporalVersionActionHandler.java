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

import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
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
            TaskInfo result = srmCoreServiceFacade.createTemporalVersionCategoryScheme(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            // Category schemes will always be version synchronously (not in background!)
            CategorySchemeMetamacDto categorySchemeMetamacDto = srmCoreServiceFacade.retrieveCategorySchemeByUrn(ServiceContextHolder.getCurrentServiceContext(), result.getUrnResult());
            return new CreateCategorySchemeTemporalVersionResult(categorySchemeMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
