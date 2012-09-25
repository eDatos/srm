package org.siemac.metamac.srm.web.server.mock;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public class MockService {

    public static InternationalStringDto createInternationalStringDto(String text_es, String text_en) {
        InternationalStringDto internationalStringDto = new InternationalStringDto();
        LocalisedStringDto locale_es = new LocalisedStringDto();
        locale_es.setLocale("es");
        locale_es.setLabel(text_es);
        LocalisedStringDto locale_en = new LocalisedStringDto();
        locale_en.setLocale("en");
        locale_en.setLabel(text_en);
        internationalStringDto.addText(locale_es);
        internationalStringDto.addText(locale_en);
        return internationalStringDto;
    }

    public static OrganisationSchemeMetamacDto getOrganisationSchemeMetamacDto() {
        OrganisationSchemeMetamacDto organisationSchemeMetamacDto = new OrganisationSchemeMetamacDto();
        organisationSchemeMetamacDto.setId(1L);
        organisationSchemeMetamacDto.setCode("organisation-scheme-1");
        organisationSchemeMetamacDto.setType(OrganisationSchemeTypeEnum.AGENCY_SCHEME);
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
        return organisationSchemeMetamacDto;
    }

}
