package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.srm.core.concept.dto.MetamacConceptSchemeDto;
import org.siemac.metamac.srm.core.enume.domain.MaintainableArtefactProcStatusEnum;
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

        MetamacConceptSchemeDto scheme = new MetamacConceptSchemeDto();
        scheme.setName(name);
        scheme.setUrn("urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX:CROSS_DOMAIN_CONCEPTS(1.0)");
        scheme.setCode("CROSS_DOMAIN_CONCEPTS");
        scheme.setVersionLogic("1.0");
        scheme.setProcStatus(MaintainableArtefactProcStatusEnum.DRAFT);
        return new GetConceptSchemeResult(scheme);
    }

}
