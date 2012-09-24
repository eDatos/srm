package org.siemac.metamac.srm.web.server.handlers.organisation;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.server.mock.MockService;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeListAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeListResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetOrganisationSchemeListActionHandler extends SecurityActionHandler<GetOrganisationSchemeListAction, GetOrganisationSchemeListResult> {

    // @Autowired
    // private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetOrganisationSchemeListActionHandler() {
        super(GetOrganisationSchemeListAction.class);
    }

    @Override
    public GetOrganisationSchemeListResult executeSecurityAction(GetOrganisationSchemeListAction action) throws ActionException {
        List<OrganisationSchemeMetamacDto> organisationSchemeMetamacDtos = new ArrayList<OrganisationSchemeMetamacDto>();
        for (int i = 0; i < 10; i++) {
            OrganisationSchemeMetamacDto organisationSchemeMetamacDto = new OrganisationSchemeMetamacDto();
            organisationSchemeMetamacDto.setId(Long.valueOf(i));
            organisationSchemeMetamacDto.setCode("organisation-scheme-" + i);
            organisationSchemeMetamacDto.setProcStatus(ProcStatusEnum.DRAFT);
            organisationSchemeMetamacDto.setName(MockService.createInternationalStringDto("Esquema de organizaciones " + i, "Organisation scheme " + i));
            organisationSchemeMetamacDtos.add(organisationSchemeMetamacDto);
        }
        return new GetOrganisationSchemeListResult(organisationSchemeMetamacDtos, 1, 10);
    }

}
