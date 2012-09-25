package org.siemac.metamac.srm.web.server.handlers.organisation;

import java.util.Date;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.server.mock.MockService;
import org.siemac.metamac.srm.web.shared.organisation.CancelOrganisationSchemeValidityAction;
import org.siemac.metamac.srm.web.shared.organisation.CancelOrganisationSchemeValidityResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class CancelOrganisationSchemeValidityActionHandler extends SecurityActionHandler<CancelOrganisationSchemeValidityAction, CancelOrganisationSchemeValidityResult> {

    // @Autowired
    // private SrmCoreServiceFacade srmCoreServiceFacade;

    public CancelOrganisationSchemeValidityActionHandler() {
        super(CancelOrganisationSchemeValidityAction.class);
    }

    @Override
    public CancelOrganisationSchemeValidityResult executeSecurityAction(CancelOrganisationSchemeValidityAction action) throws ActionException {
        OrganisationSchemeMetamacDto organisationSchemeMetamacDto = MockService.getOrganisationSchemeMetamacDto();
        organisationSchemeMetamacDto.setValidFrom(new Date());
        organisationSchemeMetamacDto.setValidTo(new Date());
        return new CancelOrganisationSchemeValidityResult(organisationSchemeMetamacDto);
    }

}
