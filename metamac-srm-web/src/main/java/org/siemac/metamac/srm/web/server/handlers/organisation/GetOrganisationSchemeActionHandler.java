package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.server.mock.MockService;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetOrganisationSchemeActionHandler extends SecurityActionHandler<GetOrganisationSchemeAction, GetOrganisationSchemeResult> {

    // @Autowired
    // private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetOrganisationSchemeActionHandler() {
        super(GetOrganisationSchemeAction.class);
    }

    @Override
    public GetOrganisationSchemeResult executeSecurityAction(GetOrganisationSchemeAction action) throws ActionException {
        OrganisationSchemeMetamacDto organisationSchemeMetamacDto = MockService.getOrganisationSchemeMetamacDto();
        return new GetOrganisationSchemeResult(organisationSchemeMetamacDto);
    }

}
