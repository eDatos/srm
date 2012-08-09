package org.siemac.metamac.srm.web.server.handlers.concept;

import java.util.UUID;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ItemSchemeMetamacProcStatusEnum;
import org.siemac.metamac.srm.web.shared.concept.CancelConceptSchemeValidityAction;
import org.siemac.metamac.srm.web.shared.concept.CancelConceptSchemeValidityResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;
import com.ibm.icu.text.DecimalFormat;

@Component
public class CancelConceptSchemeValidityActionHandler extends SecurityActionHandler<CancelConceptSchemeValidityAction, CancelConceptSchemeValidityResult> {

    public CancelConceptSchemeValidityActionHandler() {
        super(CancelConceptSchemeValidityAction.class);
    }

    @Override
    public CancelConceptSchemeValidityResult executeSecurityAction(CancelConceptSchemeValidityAction action) throws ActionException {
        ConceptSchemeMetamacDto conceptSchemeDto = new ConceptSchemeMetamacDto();
        conceptSchemeDto.setId(Long.valueOf(3 * 1));
        conceptSchemeDto.setUuid(UUID.randomUUID().toString());
        conceptSchemeDto.setUrn("prefix=" + UUID.randomUUID().toString());
        conceptSchemeDto.setUri(UUID.randomUUID().toString());
        conceptSchemeDto.setVersionLogic("01.000");
        conceptSchemeDto.setProcStatus(ItemSchemeMetamacProcStatusEnum.DRAFT);
        DecimalFormat nformat = new DecimalFormat("0000");
        conceptSchemeDto.setCode("SCH" + nformat.format(5));

        InternationalStringDto intStr = new InternationalStringDto();
        LocalisedStringDto locStr = new LocalisedStringDto();
        locStr.setLocale("es");
        locStr.setLabel("SCH " + nformat.format(1));
        intStr.addText(locStr);
        conceptSchemeDto.setName(intStr);
        return new CancelConceptSchemeValidityResult(conceptSchemeDto);
    }

}
