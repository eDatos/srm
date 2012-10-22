package org.siemac.metamac.srm.web.server.handlers.category;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.GetCategoryListBySchemeAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoryListBySchemeResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCategoryListBySchemeActionHandler extends SecurityActionHandler<GetCategoryListBySchemeAction, GetCategoryListBySchemeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCategoryListBySchemeActionHandler() {
        super(GetCategoryListBySchemeAction.class);
    }

    @Override
    public GetCategoryListBySchemeResult executeSecurityAction(GetCategoryListBySchemeAction action) throws ActionException {
        try {
            List<ItemHierarchyDto> categories = srmCoreServiceFacade.retrieveCategoriesByCategorySchemeUrn(ServiceContextHolder.getCurrentServiceContext(), action.getSchemeUrn());
            return new GetCategoryListBySchemeResult(categories);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
