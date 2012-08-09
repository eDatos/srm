package org.siemac.metamac.srm.web.server.handlers.concept;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.web.shared.concept.GetConceptAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetConceptActionHandler extends SecurityActionHandler<GetConceptAction, GetConceptResult> {

    public GetConceptActionHandler() {
        super(GetConceptAction.class);
    }

    @Override
    public GetConceptResult executeSecurityAction(GetConceptAction action) throws ActionException {
        InternationalStringDto name = new InternationalStringDto();
        LocalisedStringDto localisedStringDto = new LocalisedStringDto();
        localisedStringDto.setId(2L);
        localisedStringDto.setLabel("Concepto 0001");
        localisedStringDto.setLocale("es");
        name.addText(localisedStringDto);

        ConceptMetamacDto conceptDto = new ConceptMetamacDto();
        conceptDto.setId(1L);
        conceptDto.setCode("concept-0001");
        conceptDto.setName(name);
        conceptDto.setUri("URI");
        conceptDto.setUrn("URN");

        return new GetConceptResult(conceptDto);
    }

}
