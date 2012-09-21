package org.siemac.metamac.srm.core.concept.serviceapi.utils;

import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;

import com.arte.statistic.sdmx.srm.core.concept.serviceapi.utils.ConceptsDtoMocks;

public class ConceptsMetamacDtoMocks {

    // -----------------------------------------------------------------------------------
    // CONCEPT SCHEMES
    // -----------------------------------------------------------------------------------

    public static ConceptSchemeMetamacDto mockConceptSchemeDtoGlossaryType() {
        ConceptSchemeMetamacDto conceptSchemeDto = new ConceptSchemeMetamacDto();
        ConceptsDtoMocks.mockConceptSchemeDto(conceptSchemeDto);
        conceptSchemeDto.setType(ConceptSchemeTypeEnum.GLOSSARY);
        conceptSchemeDto.setMaintainer(MetamacMocks.mockExternalItemDto("urn:maintainer", TypeExternalArtefactsEnum.AGENCY));

        return conceptSchemeDto;
    }

    public static ConceptSchemeMetamacDto mockConceptSchemeDtoOperationType() {
        ConceptSchemeMetamacDto conceptSchemeDto = new ConceptSchemeMetamacDto();
        ConceptsDtoMocks.mockConceptSchemeDto(conceptSchemeDto);
        conceptSchemeDto.setType(ConceptSchemeTypeEnum.OPERATION);
        conceptSchemeDto.setRelatedOperation(MetamacMocks.mockExternalItemDto("urn:operation", TypeExternalArtefactsEnum.STATISTICAL_OPERATION));
        conceptSchemeDto.setMaintainer(MetamacMocks.mockExternalItemDto("urn:maintainer", TypeExternalArtefactsEnum.AGENCY));

        return conceptSchemeDto;
    }

    // -----------------------------------------------------------------------------------
    // CONCEPTS
    // -----------------------------------------------------------------------------------

    public static ConceptMetamacDto mockConceptDto(Boolean enumerated) {
        ConceptMetamacDto conceptMetamacDto = new ConceptMetamacDto();
        ConceptsDtoMocks.mockConceptDto(conceptMetamacDto, enumerated);
        conceptMetamacDto.setSdmxRelatedArtefact(ConceptRoleEnum.ATTRIBUTE);
        conceptMetamacDto.setType(mockConceptTypeDto());
        return conceptMetamacDto;
    }

    public static ConceptTypeDto mockConceptTypeDto() {
        ConceptTypeDto conceptTypeDto = new ConceptTypeDto();
        conceptTypeDto.setIdentifier("DERIVED");
        return conceptTypeDto;
    }
}
