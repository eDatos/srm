package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.server.mock.MockService;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationDto;
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
        OrganisationSchemeMetamacDto organisationSchemeMetamacDto = new OrganisationSchemeMetamacDto();
        organisationSchemeMetamacDto.setId(1L);
        organisationSchemeMetamacDto.setCode("organisation-scheme-1");
        organisationSchemeMetamacDto.setUrn("urn:sdmx:org.sdmx.infomodel.organisationscheme.OrganisationScheme=agency_CODE:scheme0001(01.000)");
        organisationSchemeMetamacDto.setProcStatus(ProcStatusEnum.DRAFT);
        organisationSchemeMetamacDto.setName(MockService.createInternationalStringDto("Esquema de organizaciones 1", "Organisation scheme 1"));
        organisationSchemeMetamacDto.setUrn("urn:organisation:1");
        organisationSchemeMetamacDto.setDescription(MockService.createInternationalStringDto("Descripción", "Description"));
        organisationSchemeMetamacDto.setIsExternalReference(false);
        ExternalItemDto maintainer = new ExternalItemDto("agency", "uri", "urn:agency", TypeExternalArtefactsEnum.AGENCY, MockService.createInternationalStringDto("Agencia", "Agency"));
        organisationSchemeMetamacDto.setMaintainer(maintainer);
        AnnotationDto annotationDto = new AnnotationDto();
        annotationDto.setId(1L);
        annotationDto.setText(MockService.createInternationalStringDto("anotación", "anotation"));
        organisationSchemeMetamacDto.getAnnotations().add(annotationDto);
        return new GetOrganisationSchemeResult(organisationSchemeMetamacDto);
    }

}
