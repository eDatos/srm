package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.SaveOrganisationSchemeResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveOrganisationSchemeActionHandler extends SecurityActionHandler<SaveOrganisationSchemeAction, SaveOrganisationSchemeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveOrganisationSchemeActionHandler() {
        super(SaveOrganisationSchemeAction.class);
    }

    @Override
    public SaveOrganisationSchemeResult executeSecurityAction(SaveOrganisationSchemeAction action) throws ActionException {
        try {
            OrganisationSchemeMetamacDto organisationSchemeToSave = action.getOrganisationSchemeToSave();
            OrganisationSchemeMetamacDto savedOrganisationSchemeDto = null;
            if (organisationSchemeToSave.getId() == null) {
                // Create
                savedOrganisationSchemeDto = srmCoreServiceFacade.createOrganisationScheme(ServiceContextHolder.getCurrentServiceContext(), organisationSchemeToSave);
                return new SaveOrganisationSchemeResult(savedOrganisationSchemeDto);
            } else {
                // Update
                // TODO savedOrganisationSchemeDto = srmCoreServiceFacade.updateOrganisationScheme(ServiceContextHolder.getCurrentServiceContext(), organisationSchemeToSave);
            }
            return new SaveOrganisationSchemeResult(savedOrganisationSchemeDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
