package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetConceptSchemeActionHandler extends SecurityActionHandler<GetConceptSchemeAction, GetConceptSchemeResult> {

    public GetConceptSchemeActionHandler() {
        super(GetConceptSchemeAction.class);
    }

    @Override
    public GetConceptSchemeResult executeSecurityAction(GetConceptSchemeAction action) throws ActionException {
        InternationalStringDto name = new InternationalStringDto();
        LocalisedStringDto localisedStringDto = new LocalisedStringDto();
        localisedStringDto.setId(2L);
        localisedStringDto.setLabel("Esquema de conceptos 0001");
        localisedStringDto.setLocale("es");
        name.addText(localisedStringDto);

        ConceptSchemeMetamacDto scheme = new ConceptSchemeMetamacDto();
        scheme.setName(name);
        scheme.setUrn("urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX:CROSS_DOMAIN_CONCEPTS(1.0)");
        scheme.setCode("CROSS_DOMAIN_CONCEPTS");
        scheme.setVersionLogic("1.0");
        scheme.setProcStatus(ItemSchemeMetamacProcStatusEnum.DRAFT);
        return new GetConceptSchemeResult(scheme);
    }

}
