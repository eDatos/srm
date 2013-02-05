package org.siemac.metamac.srm.web.server.handlers.category;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeVersionListAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemeVersionListResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCategorySchemeVersionListActionHandler extends SecurityActionHandler<GetCategorySchemeVersionListAction, GetCategorySchemeVersionListResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCategorySchemeVersionListActionHandler() {
        super(GetCategorySchemeVersionListAction.class);
    }

    @Override
    public GetCategorySchemeVersionListResult executeSecurityAction(GetCategorySchemeVersionListAction action) throws ActionException {
        try {
            List<CategorySchemeMetamacDto> versions = srmCoreServiceFacade.retrieveCategorySchemeVersions(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new GetCategorySchemeVersionListResult(versions);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
