package org.siemac.metamac.srm.web.server.handlers.category;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.category.CreateCategorisationAction;
import org.siemac.metamac.srm.web.shared.category.CreateCategorisationResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CreateCategorisationActionHandler extends SecurityActionHandler<CreateCategorisationAction, CreateCategorisationResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CreateCategorisationActionHandler() {
        super(CreateCategorisationAction.class);
    }

    @Override
    public CreateCategorisationResult executeSecurityAction(CreateCategorisationAction action) throws ActionException {
        try {
            for (String categoryUrn : action.getCategoryUrns()) {
                srmCoreServiceFacade.createCategorisation(ServiceContextHolder.getCurrentServiceContext(), categoryUrn, action.getArtefactUrnToCategorise(), action.getMaintainerUrn());
            }
            return new CreateCategorisationResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
