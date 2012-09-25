package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationSchemeResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveOrganisationSchemeActionHandler extends SecurityActionHandler<SaveOrganisationSchemeAction, SaveOrganisationSchemeResult> {

    // @Autowired
    // private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveOrganisationSchemeActionHandler() {
        super(SaveOrganisationSchemeAction.class);
    }

    @Override
    public SaveOrganisationSchemeResult executeSecurityAction(SaveOrganisationSchemeAction action) throws ActionException {
        return new SaveOrganisationSchemeResult(action.getOrganisationSchemeToSave());
    }

}
