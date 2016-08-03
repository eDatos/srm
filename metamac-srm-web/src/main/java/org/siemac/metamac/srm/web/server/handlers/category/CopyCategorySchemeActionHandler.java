package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.concept.CopyCategorySchemeAction;
import org.siemac.metamac.srm.web.shared.concept.CopyCategorySchemeResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CopyCategorySchemeActionHandler extends SecurityActionHandler<CopyCategorySchemeAction, CopyCategorySchemeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CopyCategorySchemeActionHandler() {
        super(CopyCategorySchemeAction.class);
    }

    @Override
    public CopyCategorySchemeResult executeSecurityAction(CopyCategorySchemeAction action) throws ActionException {
        try {
            TaskInfo taskInfo = srmCoreServiceFacade.copyCategoryScheme(ServiceContextHolder.getCurrentServiceContext(), action.getCategorySchemeUrn(), action.getCode());
            // Category scheme will never be copied in background
            CategorySchemeMetamacDto categorySchemeMetamacDto = srmCoreServiceFacade.retrieveCategorySchemeByUrn(ServiceContextHolder.getCurrentServiceContext(), taskInfo.getUrnResult());
            return new CopyCategorySchemeResult(categorySchemeMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
