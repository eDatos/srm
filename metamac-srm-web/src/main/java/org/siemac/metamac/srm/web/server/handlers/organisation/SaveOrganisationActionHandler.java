package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationAction;
import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveOrganisationActionHandler extends SecurityActionHandler<SaveOrganisationAction, SaveOrganisationResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveOrganisationActionHandler() {
        super(SaveOrganisationAction.class);
    }

    @Override
    public SaveOrganisationResult executeSecurityAction(SaveOrganisationAction action) throws ActionException {
        return new SaveOrganisationResult(action.getOrganisationToSave());
    }

}
