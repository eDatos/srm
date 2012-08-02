package org.siemac.metamac.srm.web.server.handlers.concept;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.srm.core.concept.dto.MetamacConceptSchemeDto;
import org.siemac.metamac.srm.core.enume.domain.MaintainableArtefactProcStatusEnum;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeHistoryListAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemeHistoryListResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetConceptSchemeHistoryListActionHandler extends SecurityActionHandler<GetConceptSchemeHistoryListAction, GetConceptSchemeHistoryListResult> {

    public GetConceptSchemeHistoryListActionHandler() {
        super(GetConceptSchemeHistoryListAction.class);
    }

    @Override
    public GetConceptSchemeHistoryListResult executeSecurityAction(GetConceptSchemeHistoryListAction action) throws ActionException {
        List<MetamacConceptSchemeDto> conceptSchemeDtos = new ArrayList<MetamacConceptSchemeDto>();
        conceptSchemeDtos.add(getConceptSchemeDto1());
        conceptSchemeDtos.add(getConceptSchemeDto2());
        return new GetConceptSchemeHistoryListResult(conceptSchemeDtos);
    }

    private MetamacConceptSchemeDto getConceptSchemeDto1() {
        InternationalStringDto name = new InternationalStringDto();
        LocalisedStringDto localisedStringDto = new LocalisedStringDto();
        localisedStringDto.setId(2L);
        localisedStringDto.setLabel("Esquema de conceptos 0001");
        localisedStringDto.setLocale("es");
        name.addText(localisedStringDto);

        MetamacConceptSchemeDto conceptSchemeDto = new MetamacConceptSchemeDto();
        conceptSchemeDto.setId(1L);
        conceptSchemeDto.setCode("scheme-0001");
        conceptSchemeDto.setName(name);
        conceptSchemeDto.setVersionLogic("1.000");
        conceptSchemeDto.setUri("URI");
        conceptSchemeDto.setUrn("prefix=URN");
        conceptSchemeDto.setProcStatus(MaintainableArtefactProcStatusEnum.DRAFT);
        return conceptSchemeDto;
    }

    private MetamacConceptSchemeDto getConceptSchemeDto2() {
        InternationalStringDto name = new InternationalStringDto();
        LocalisedStringDto localisedStringDto = new LocalisedStringDto();
        localisedStringDto.setId(2L);
        localisedStringDto.setLabel("Esquema de conceptos 0001");
        localisedStringDto.setLocale("es");
        name.addText(localisedStringDto);

        MetamacConceptSchemeDto conceptSchemeDto = new MetamacConceptSchemeDto();
        conceptSchemeDto.setId(1L);
        conceptSchemeDto.setCode("scheme-0002");
        conceptSchemeDto.setName(name);
        conceptSchemeDto.setVersionLogic("1.001");
        conceptSchemeDto.setUri("URI");
        conceptSchemeDto.setUrn("prefix=URN");
        conceptSchemeDto.setProcStatus(MaintainableArtefactProcStatusEnum.DRAFT);
        return conceptSchemeDto;
    }

}
