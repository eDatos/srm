package org.siemac.metamac.srm.web.server.handlers.category;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeVersionsAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeVersionsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCategorySchemeVersionsActionHandler extends SecurityActionHandler<GetCategorySchemeVersionsAction, GetCategorySchemeVersionsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCategorySchemeVersionsActionHandler() {
        super(GetCategorySchemeVersionsAction.class);
    }

    @Override
    public GetCategorySchemeVersionsResult executeSecurityAction(GetCategorySchemeVersionsAction action) throws ActionException {
        try {
            List<CategorySchemeMetamacBasicDto> versions = srmCoreServiceFacade.retrieveCategorySchemeVersions(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new GetCategorySchemeVersionsResult(versions);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
