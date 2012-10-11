package org.siemac.metamac.srm.web.server.handlers.organisation;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationListBySchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationListBySchemeResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetOrganisationListBySchemeActionHandler extends SecurityActionHandler<GetOrganisationListBySchemeAction, GetOrganisationListBySchemeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetOrganisationListBySchemeActionHandler() {
        super(GetOrganisationListBySchemeAction.class);
    }

    @Override
    public GetOrganisationListBySchemeResult executeSecurityAction(GetOrganisationListBySchemeAction action) throws ActionException {
        List<OrganisationMetamacDto> organisationDtos = new ArrayList<OrganisationMetamacDto>();
        OrganisationMetamacDto organisationDto = new OrganisationMetamacDto();
        organisationDto.setUrn("urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=ISTAC:ORGANISATIONSCHEME03(01.000).ORGANISATION01");
        organisationDto.setCode("ORGANISATION01");
        organisationDto.setType(OrganisationTypeEnum.AGENCY);
        organisationDto.setName(null);
        organisationDtos.add(organisationDto);
        return new GetOrganisationListBySchemeResult(organisationDtos);
    }

}
