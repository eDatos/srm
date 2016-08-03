package org.siemac.metamac.srm.web.server.handlers.category;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.CancelCategorySchemeValidityAction;
import org.siemac.metamac.srm.web.shared.category.CancelCategorySchemeValidityResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CancelCategorySchemeValidityActionHandler extends SecurityActionHandler<CancelCategorySchemeValidityAction, CancelCategorySchemeValidityResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CancelCategorySchemeValidityActionHandler() {
        super(CancelCategorySchemeValidityAction.class);
    }

    @Override
    public CancelCategorySchemeValidityResult executeSecurityAction(CancelCategorySchemeValidityAction action) throws ActionException {
        List<CategorySchemeMetamacDto> categorySchemeMetamacDtos = new ArrayList<CategorySchemeMetamacDto>();
        for (String urn : action.getUrns()) {
            try {
                CategorySchemeMetamacDto categorySchemeMetamacDto = srmCoreServiceFacade.endCategorySchemeValidity(ServiceContextHolder.getCurrentServiceContext(), urn);
                categorySchemeMetamacDtos.add(categorySchemeMetamacDto);
            } catch (MetamacException e) {
                throw WebExceptionUtils.createMetamacWebException(e);
            }
        }
        return new CancelCategorySchemeValidityResult(categorySchemeMetamacDtos);
    }
}
