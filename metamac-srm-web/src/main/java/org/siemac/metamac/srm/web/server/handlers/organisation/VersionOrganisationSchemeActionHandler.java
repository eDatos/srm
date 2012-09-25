package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.server.mock.MockService;
import org.siemac.metamac.srm.web.shared.organisation.VersionOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.VersionOrganisationSchemeResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class VersionOrganisationSchemeActionHandler extends SecurityActionHandler<VersionOrganisationSchemeAction, VersionOrganisationSchemeResult> {

    // @Autowired
    // private SrmCoreServiceFacade srmCoreServiceFacade;

    public VersionOrganisationSchemeActionHandler() {
        super(VersionOrganisationSchemeAction.class);
    }

    @Override
    public VersionOrganisationSchemeResult executeSecurityAction(VersionOrganisationSchemeAction action) throws ActionException {
        OrganisationSchemeMetamacDto organisationSchemeMetamacDto = MockService.getOrganisationSchemeMetamacDto();
        organisationSchemeMetamacDto.setVersionLogic("02.000");
        return new VersionOrganisationSchemeResult(organisationSchemeMetamacDto);
    }

}
