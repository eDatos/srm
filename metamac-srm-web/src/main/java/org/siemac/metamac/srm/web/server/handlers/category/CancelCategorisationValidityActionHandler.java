package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.CancelCategorisationValidityAction;
import org.siemac.metamac.srm.web.shared.category.CancelCategorisationValidityResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.shared.ActionException;

public class CancelCategorisationValidityActionHandler extends SecurityActionHandler<CancelCategorisationValidityAction, CancelCategorisationValidityResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CancelCategorisationValidityActionHandler() {
        super(CancelCategorisationValidityAction.class);
    }

    @Override
    public CancelCategorisationValidityResult executeSecurityAction(CancelCategorisationValidityAction action) throws ActionException {
        // try {
        // TODO
        // } catch (MetamacException e) {
        // throw WebExceptionUtils.createMetamacWebException(e);
        // }
        return null;
    }
}
