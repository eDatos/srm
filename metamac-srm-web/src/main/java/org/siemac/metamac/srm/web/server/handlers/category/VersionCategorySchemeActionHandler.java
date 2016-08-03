package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.VersionCategorySchemeAction;
import org.siemac.metamac.srm.web.shared.category.VersionCategorySchemeResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class VersionCategorySchemeActionHandler extends SecurityActionHandler<VersionCategorySchemeAction, VersionCategorySchemeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public VersionCategorySchemeActionHandler() {
        super(VersionCategorySchemeAction.class);
    }

    @Override
    public VersionCategorySchemeResult executeSecurityAction(VersionCategorySchemeAction action) throws ActionException {
        try {
            TaskInfo result = srmCoreServiceFacade.versioningCategoryScheme(ServiceContextHolder.getCurrentServiceContext(), action.getUrn(), action.getVersionType());
            // Category schemes will always be version synchronously (not in background!)
            CategorySchemeMetamacDto categorySchemeMetamacDto = srmCoreServiceFacade.retrieveCategorySchemeByUrn(ServiceContextHolder.getCurrentServiceContext(), result.getUrnResult());
            return new VersionCategorySchemeResult(categorySchemeMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
