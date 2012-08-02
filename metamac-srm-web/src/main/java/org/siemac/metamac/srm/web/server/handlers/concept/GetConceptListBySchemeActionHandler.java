package org.siemac.metamac.srm.web.server.handlers.concept;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.srm.web.shared.concept.GetConceptListBySchemeAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptListBySchemeResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.concept.ConceptDto;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetConceptListBySchemeActionHandler extends SecurityActionHandler<GetConceptListBySchemeAction, GetConceptListBySchemeResult> {

    public GetConceptListBySchemeActionHandler() {
        super(GetConceptListBySchemeAction.class);
    }

    @Override
    public GetConceptListBySchemeResult executeSecurityAction(GetConceptListBySchemeAction action) throws ActionException {

        InternationalStringDto name = new InternationalStringDto();
        LocalisedStringDto localisedStringDto = new LocalisedStringDto();
        localisedStringDto.setId(2L);
        localisedStringDto.setLabel("Concepto 0001");
        localisedStringDto.setLocale("es");
        name.addText(localisedStringDto);

        ConceptDto conceptDto = new ConceptDto();
        conceptDto.setId(1L);
        conceptDto.setCode("concept-0001");
        conceptDto.setName(name);

        List<ConceptDto> conceptDtos = new ArrayList<ConceptDto>();
        conceptDtos.add(conceptDto);

        return new GetConceptListBySchemeResult(conceptDtos);
    }

}
