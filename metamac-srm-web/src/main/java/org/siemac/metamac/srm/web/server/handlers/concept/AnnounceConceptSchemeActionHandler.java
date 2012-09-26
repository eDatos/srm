package org.siemac.metamac.srm.web.server.handlers.concept;

import java.util.UUID;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.srm.core.base.dto.LifeCycleDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.shared.concept.AnnounceConceptSchemeAction;
import org.siemac.metamac.srm.web.shared.concept.AnnounceConceptSchemeResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;
import com.ibm.icu.text.DecimalFormat;

@Component
public class AnnounceConceptSchemeActionHandler extends SecurityActionHandler<AnnounceConceptSchemeAction, AnnounceConceptSchemeResult> {

    public AnnounceConceptSchemeActionHandler() {
        super(AnnounceConceptSchemeAction.class);
    }

    @Override
    public AnnounceConceptSchemeResult executeSecurityAction(AnnounceConceptSchemeAction action) throws ActionException {
        ConceptSchemeMetamacDto conceptSchemeDto = new ConceptSchemeMetamacDto();
        conceptSchemeDto.setId(Long.valueOf(3 * 1));
        conceptSchemeDto.setUuid(UUID.randomUUID().toString());
        conceptSchemeDto.setUrn("prefix=" + UUID.randomUUID().toString());
        conceptSchemeDto.setUri(UUID.randomUUID().toString());
        conceptSchemeDto.setVersionLogic("01.000");
        conceptSchemeDto.setLifeCycle(new LifeCycleDto(ProcStatusEnum.DRAFT));
        DecimalFormat nformat = new DecimalFormat("0000");
        conceptSchemeDto.setCode("SCH" + nformat.format(5));

        InternationalStringDto intStr = new InternationalStringDto();
        LocalisedStringDto locStr = new LocalisedStringDto();
        locStr.setLocale("es");
        locStr.setLabel("SCH " + nformat.format(1));
        intStr.addText(locStr);
        conceptSchemeDto.setName(intStr);
        return new AnnounceConceptSchemeResult(conceptSchemeDto);
    }

}
