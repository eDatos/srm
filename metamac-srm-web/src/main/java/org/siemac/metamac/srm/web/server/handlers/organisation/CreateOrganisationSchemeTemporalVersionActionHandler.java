package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.shared.organisation.CreateOrganisationSchemeTemporalVersionAction;
import org.siemac.metamac.srm.web.shared.organisation.CreateOrganisationSchemeTemporalVersionResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CreateOrganisationSchemeTemporalVersionActionHandler extends SecurityActionHandler<CreateOrganisationSchemeTemporalVersionAction, CreateOrganisationSchemeTemporalVersionResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public CreateOrganisationSchemeTemporalVersionActionHandler() {
        super(CreateOrganisationSchemeTemporalVersionAction.class);
    }

    @Override
    public CreateOrganisationSchemeTemporalVersionResult executeSecurityAction(CreateOrganisationSchemeTemporalVersionAction action) throws ActionException {
        try {
            OrganisationSchemeMetamacDto organisationSchemeMetamacDto = srmCoreServiceFacade.createTemporalVersionOrganisationScheme(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new CreateOrganisationSchemeTemporalVersionResult(organisationSchemeMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}