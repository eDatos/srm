package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.CancelCategorisationValidityAction;
import org.siemac.metamac.srm.web.shared.category.CancelCategorisationValidityResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.gwtplatform.dispatch.shared.ActionException;

public class CancelCategorisationValidityActionHandler extends SecurityActionHandler<CancelCategorisationValidityAction, CancelCategorisationValidityResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CancelCategorisationValidityActionHandler() {
        super(CancelCategorisationValidityAction.class);
    }

    @Override
    public CancelCategorisationValidityResult executeSecurityAction(CancelCategorisationValidityAction action) throws ActionException {
        try {
            CategorisationDto categorisationDto = srmCoreServiceFacade.endCategorisationValidity(ServiceContextHolder.getCurrentServiceContext(), action.getUrn(), action.getValidTo());
            return new CancelCategorisationValidityResult(categorisationDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
