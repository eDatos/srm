package org.siemac.metamac.srm.core.concept.serviceapi.utils;

import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;

public class ConceptsMetamacDtoMocks {

    public static ConceptSchemeMetamacDto mockConceptSchemeDtoGlossaryType() {
        ConceptSchemeMetamacDto conceptSchemeDto = new ConceptSchemeMetamacDto();
        conceptSchemeDto.setCode("code-" + MetamacMocks.mockString(10));
        conceptSchemeDto.setName(MetamacMocks.mockInternationalString());
        conceptSchemeDto.setType(ConceptSchemeTypeEnum.GLOSSARY);
        conceptSchemeDto.setMaintainer(MetamacMocks.mockExternalItemDto("urn:maintiner", TypeExternalArtefactsEnum.AGENCY));

        return conceptSchemeDto;
    }

    public static ConceptSchemeMetamacDto mockConceptSchemeDtoOperationType() {
        ConceptSchemeMetamacDto conceptSchemeDto = new ConceptSchemeMetamacDto();
        conceptSchemeDto.setCode("code-" + MetamacMocks.mockString(10));
        conceptSchemeDto.setName(MetamacMocks.mockInternationalString());
        conceptSchemeDto.setType(ConceptSchemeTypeEnum.OPERATION);
        conceptSchemeDto.setRelatedOperation(MetamacMocks.mockExternalItemDto("urn:operation", TypeExternalArtefactsEnum.STATISTICAL_OPERATION));
        conceptSchemeDto.setMaintainer(MetamacMocks.mockExternalItemDto("urn:maintiner", TypeExternalArtefactsEnum.AGENCY));

        return conceptSchemeDto;
    }

}
