package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.OrganisationDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetOrganisationActionHandler extends SecurityActionHandler<GetOrganisationAction, GetOrganisationResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetOrganisationActionHandler() {
        super(GetOrganisationAction.class);
    }

    @Override
    public GetOrganisationResult executeSecurityAction(GetOrganisationAction action) throws ActionException {
        OrganisationDto organisationDto = new OrganisationDto();
        organisationDto.setUrn("urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=ISTAC:ORGANISATIONSCHEME03(01.000).ORGANISATION01");
        organisationDto.setCode("ORGANISATION01");
        organisationDto.setType(OrganisationTypeEnum.AGENCY);

        InternationalStringDto name = new InternationalStringDto();
        LocalisedStringDto localisedStringDto = new LocalisedStringDto();
        localisedStringDto.setLocale("es");
        localisedStringDto.setLabel("Organización");
        name.addText(localisedStringDto);

        organisationDto.setName(name);

        return new GetOrganisationResult(organisationDto);
    }

}
