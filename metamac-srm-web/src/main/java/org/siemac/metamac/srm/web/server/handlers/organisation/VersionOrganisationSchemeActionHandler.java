package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.shared.organisation.VersionOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.VersionOrganisationSchemeResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class VersionOrganisationSchemeActionHandler extends SecurityActionHandler<VersionOrganisationSchemeAction, VersionOrganisationSchemeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public VersionOrganisationSchemeActionHandler() {
        super(VersionOrganisationSchemeAction.class);
    }

    @Override
    public VersionOrganisationSchemeResult executeSecurityAction(VersionOrganisationSchemeAction action) throws ActionException {
        try {
            OrganisationSchemeMetamacDto organisationSchemeMetamacDto = srmCoreServiceFacade.versioningOrganisationScheme(ServiceContextHolder.getCurrentServiceContext(), action.getUrn(),
                    action.getVersionType());
            return new VersionOrganisationSchemeResult(organisationSchemeMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
