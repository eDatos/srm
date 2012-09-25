package org.siemac.metamac.srm.web.server.handlers.organisation;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeVersionListAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeVersionListResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetOrganisationSchemeVersionListActionHandler extends SecurityActionHandler<GetOrganisationSchemeVersionListAction, GetOrganisationSchemeVersionListResult> {

    // @Autowired
    // private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetOrganisationSchemeVersionListActionHandler() {
        super(GetOrganisationSchemeVersionListAction.class);
    }

    @Override
    public GetOrganisationSchemeVersionListResult executeSecurityAction(GetOrganisationSchemeVersionListAction action) throws ActionException {
        List<OrganisationSchemeMetamacDto> versions = new ArrayList<OrganisationSchemeMetamacDto>();
        return new GetOrganisationSchemeVersionListResult(versions);
    }

}
