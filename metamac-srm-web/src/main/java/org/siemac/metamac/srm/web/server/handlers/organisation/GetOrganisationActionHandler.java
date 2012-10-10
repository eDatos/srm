package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;
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
        {
            organisationDto.setUrn("urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=ISTAC:ORGANISATIONSCHEME03(01.000).ORGANISATION01");
            organisationDto.setCode("ORGANISATION01");
            organisationDto.setType(OrganisationTypeEnum.AGENCY);
            InternationalStringDto name = new InternationalStringDto();
            LocalisedStringDto localisedStringDto = new LocalisedStringDto();
            localisedStringDto.setLocale("es");
            localisedStringDto.setLabel("Organización");
            name.addText(localisedStringDto);
            organisationDto.setName(name);
        }
        {
            for (int i = 0; i < 3; i++) {
                ContactDto contactDto = new ContactDto();
                contactDto.setId(Long.valueOf(i));
                {
                    InternationalStringDto name = new InternationalStringDto();
                    LocalisedStringDto localisedStringDto = new LocalisedStringDto();
                    localisedStringDto.setLocale("es");
                    localisedStringDto.setLabel("Contacto " + i);
                    name.addText(localisedStringDto);
                    contactDto.setName(name);
                }
                {
                    InternationalStringDto organisationUnit = new InternationalStringDto();
                    LocalisedStringDto localisedStringDto = new LocalisedStringDto();
                    localisedStringDto.setLocale("es");
                    localisedStringDto.setLabel("Unidad organizativa " + i);
                    organisationUnit.addText(localisedStringDto);
                    contactDto.setOrganisationUnit(organisationUnit);
                }
                {
                    InternationalStringDto resposibility = new InternationalStringDto();
                    LocalisedStringDto localisedStringDto = new LocalisedStringDto();
                    localisedStringDto.setLocale("es");
                    localisedStringDto.setLabel("Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, "
                            + "totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. ");
                    resposibility.addText(localisedStringDto);
                    contactDto.setResponsibility(resposibility);
                }
                contactDto.getTelephones().add("12345678");
                contactDto.getFaxes().add("+00125678");
                contactDto.getEmails().add("nombre@email.com");
                contactDto.setUrl("http://www.url" + i + ".com");
                organisationDto.getContacts().add(contactDto);
            }
        }
        return new GetOrganisationResult(organisationDto);
    }
}
