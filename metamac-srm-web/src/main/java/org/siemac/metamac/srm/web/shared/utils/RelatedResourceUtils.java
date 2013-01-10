package org.siemac.metamac.srm.web.shared.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class RelatedResourceUtils {

    //
    // CONCEPTS
    //

    // Concepts

    public static RelatedResourceDto getRelatedResourceDtoFromConceptMetamacDto(ConceptMetamacDto concept) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setCode(concept.getCode());
        relatedResourceDto.setTitle(concept.getName());
        relatedResourceDto.setType(TypeExternalArtefactsEnum.CONCEPT);
        relatedResourceDto.setUrn(concept.getUrn());
        return relatedResourceDto;
    }

    public static List<RelatedResourceDto> getRelatedResourceDtosFromConceptMetamacDtos(List<ConceptMetamacDto> conceptMetamacDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>();
        for (ConceptMetamacDto concept : conceptMetamacDtos) {
            relatedResourceDtos.add(getRelatedResourceDtoFromConceptMetamacDto(concept));
        }
        return relatedResourceDtos;
    }

    //
    // CODES
    //

    // Codelists

    public static RelatedResourceDto getRelatedResourceDtoFromCodelistDto(CodelistMetamacDto codelist) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setCode(codelist.getCode());
        relatedResourceDto.setTitle(codelist.getName());
        relatedResourceDto.setUrn(codelist.getUrn());
        relatedResourceDto.setType(TypeExternalArtefactsEnum.CODELIST);
        return relatedResourceDto;
    }

    public static List<RelatedResourceDto> getRelatedResourceDtosFromCodelistDtos(List<CodelistMetamacDto> codelistDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>();
        for (CodelistMetamacDto codelist : codelistDtos) {
            relatedResourceDtos.add(getRelatedResourceDtoFromCodelistDto(codelist));
        }
        return relatedResourceDtos;
    }

    // Codelist families

    public static RelatedResourceDto getRelatedResourceDtoFromCodelistFamilyDto(CodelistFamilyDto codelistFamily) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setCode(codelistFamily.getCode());
        relatedResourceDto.setTitle(codelistFamily.getName());
        relatedResourceDto.setUrn(codelistFamily.getUrn());
        return relatedResourceDto;
    }

    public static List<RelatedResourceDto> getRelatedResourceDtosFromCodelistFamilyDtos(List<CodelistFamilyDto> codelistFamilyDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>();
        for (CodelistFamilyDto family : codelistFamilyDtos) {
            relatedResourceDtos.add(getRelatedResourceDtoFromCodelistFamilyDto(family));
        }
        return relatedResourceDtos;
    }

    //
    // GENERIC RELATED RESOURCES
    //

    public static RelatedResourceDto createRelatedResourceDto(String urn) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setUrn(urn);
        return relatedResourceDto;
    }

    public static List<String> getUrnsFromRelatedResourceDtos(List<RelatedResourceDto> relatedResourceDtos) {
        List<String> urns = new ArrayList<String>();
        for (RelatedResourceDto relatedResourceDto : relatedResourceDtos) {
            urns.add(relatedResourceDto.getUrn());
        }
        return urns;
    }
}
