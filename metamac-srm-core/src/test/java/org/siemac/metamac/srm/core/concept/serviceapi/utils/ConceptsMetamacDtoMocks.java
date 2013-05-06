package org.siemac.metamac.srm.core.concept.serviceapi.utils;

import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;

import com.arte.statistic.sdmx.srm.core.concept.serviceapi.utils.ConceptsDtoMocks;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;

public class ConceptsMetamacDtoMocks {

    // -----------------------------------------------------------------------------------
    // CONCEPT SCHEMES
    // -----------------------------------------------------------------------------------

    public static ConceptSchemeMetamacDto mockConceptSchemeDtoGlossaryType(String codeMaintainer, String urnMaintainer) {
        ConceptSchemeMetamacDto conceptSchemeDto = new ConceptSchemeMetamacDto();
        ConceptsDtoMocks.mockConceptSchemeDto(conceptSchemeDto);
        conceptSchemeDto.setType(ConceptSchemeTypeEnum.GLOSSARY);

        conceptSchemeDto.setMaintainer(new RelatedResourceDto(codeMaintainer, urnMaintainer, RelatedResourceTypeEnum.AGENCY));

        return conceptSchemeDto;
    }

    public static ConceptSchemeMetamacDto mockConceptSchemeDtoOperationType(String codeMaintainer, String urnMaintainer) {
        ConceptSchemeMetamacDto conceptSchemeDto = new ConceptSchemeMetamacDto();
        ConceptsDtoMocks.mockConceptSchemeDto(conceptSchemeDto);
        conceptSchemeDto.setType(ConceptSchemeTypeEnum.OPERATION);
        conceptSchemeDto.setRelatedOperation(MetamacMocks.mockExternalItemDto("urn:siemac:org.siemac.metamac.infomodel.statisticaloperations.Operation=operation",
                TypeExternalArtefactsEnum.STATISTICAL_OPERATION));
        RelatedResourceDto maintainerDto = new RelatedResourceDto(codeMaintainer, urnMaintainer, RelatedResourceTypeEnum.AGENCY);
        maintainerDto.setUrnProvider(urnMaintainer);
        conceptSchemeDto.setMaintainer(maintainerDto);

        return conceptSchemeDto;
    }

    // -----------------------------------------------------------------------------------
    // CONCEPTS
    // -----------------------------------------------------------------------------------

    public static ConceptMetamacDto mockConceptDto(RepresentationTypeEnum typeRepresentationEnum) {
        ConceptMetamacDto conceptMetamacDto = new ConceptMetamacDto();
        ConceptsDtoMocks.mockConceptDto(conceptMetamacDto, typeRepresentationEnum);
        conceptMetamacDto.setSdmxRelatedArtefact(ConceptRoleEnum.ATTRIBUTE);
        conceptMetamacDto.setConceptType(mockConceptTypeDto());
        return conceptMetamacDto;
    }

    public static RelatedResourceDto mockConceptRelatedResourceDto(String code, String urn) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setCode(code);
        relatedResourceDto.setUrn(urn);
        relatedResourceDto.setUrnProvider(urn);
        relatedResourceDto.setType(RelatedResourceTypeEnum.CONCEPT);
        return relatedResourceDto;
    }

    // -----------------------------------------------------------------------------------
    // CONCEPT TYPES
    // -----------------------------------------------------------------------------------

    public static ConceptTypeDto mockConceptTypeDto() {
        ConceptTypeDto conceptTypeDto = new ConceptTypeDto();
        conceptTypeDto.setIdentifier("DERIVED");
        conceptTypeDto.setDescription(new InternationalStringDto());
        LocalisedStringDto en = new LocalisedStringDto();
        en.setLabel("Derived");
        en.setLocale("en");
        conceptTypeDto.getDescription().addText(en);
        LocalisedStringDto es = new LocalisedStringDto();
        en.setLabel("Derivado");
        en.setLocale("es");
        conceptTypeDto.getDescription().addText(es);
        return conceptTypeDto;
    }
}
