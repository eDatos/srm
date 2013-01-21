package org.siemac.metamac.srm.web.shared.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class RelatedResourceUtils {

    private static RelatedResourceDto maintainer = null;

    //
    // ORGANISATIONS
    //

    public static RelatedResourceDto getDefaultMaintainerAsRelatedResourceDto() {
        if (maintainer == null) {
            OrganisationMetamacDto organisationMetamacDto = MetamacSrmWeb.getDefaultMaintainer();
            maintainer = new RelatedResourceDto(organisationMetamacDto.getCode(), organisationMetamacDto.getUrn(), TypeExternalArtefactsEnum.AGENCY);
        }
        return maintainer;
    }

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

    // Variables

    public static RelatedResourceDto getRelatedResourceDtoFromVariableDto(VariableDto codelist) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setCode(codelist.getCode());
        relatedResourceDto.setTitle(codelist.getName());
        relatedResourceDto.setUrn(codelist.getUrn());
        return relatedResourceDto;
    }

    public static List<RelatedResourceDto> getRelatedResourceDtosFromVariableDtos(List<VariableDto> variableDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>();
        for (VariableDto codelist : variableDtos) {
            relatedResourceDtos.add(getRelatedResourceDtoFromVariableDto(codelist));
        }
        return relatedResourceDtos;
    }

    // Variable families

    public static RelatedResourceDto getRelatedResourceDtoFromVariableFamilyDto(VariableFamilyDto variableFamily) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setCode(variableFamily.getCode());
        relatedResourceDto.setTitle(variableFamily.getName());
        relatedResourceDto.setUrn(variableFamily.getUrn());
        return relatedResourceDto;
    }

    public static List<RelatedResourceDto> getRelatedResourceDtosFromVariableFamilyDtos(List<VariableFamilyDto> variableFamilyDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>();
        for (VariableFamilyDto family : variableFamilyDtos) {
            relatedResourceDtos.add(getRelatedResourceDtoFromVariableFamilyDto(family));
        }
        return relatedResourceDtos;
    }

    // Variable elements

    public static RelatedResourceDto getRelatedResourceDtoFromVariableElementDto(VariableElementDto variableElement) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setCode(variableElement.getCode());
        relatedResourceDto.setTitle(variableElement.getName());
        relatedResourceDto.setUrn(variableElement.getUrn());
        return relatedResourceDto;
    }

    public static List<RelatedResourceDto> getRelatedResourceDtosFromVariableElementDtos(List<VariableElementDto> variableElementDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>();
        for (VariableElementDto element : variableElementDtos) {
            relatedResourceDtos.add(getRelatedResourceDtoFromVariableElementDto(element));
        }
        return relatedResourceDtos;
    }

    //
    // GENERIC RELATED RESOURCES
    //

    public static RelatedResourceDto createRelatedResourceDto(TypeExternalArtefactsEnum type, String urn) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setUrn(urn);
        return relatedResourceDto;
    }

    public static RelatedResourceDto createRelatedResourceDto(String urn) {
        if (!StringUtils.isBlank(urn)) {
            return createRelatedResourceDto(null, urn);
        } else {
            return null;
        }
    }

    public static List<String> getUrnsFromRelatedResourceDtos(List<RelatedResourceDto> relatedResourceDtos) {
        List<String> urns = new ArrayList<String>();
        for (RelatedResourceDto relatedResourceDto : relatedResourceDtos) {
            urns.add(relatedResourceDto.getUrn());
        }
        return urns;
    }
}
