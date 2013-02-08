package org.siemac.metamac.srm.web.shared.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.IdentifiableArtefactDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.NameableArtefactDto;

public class RelatedResourceUtils {

    private static RelatedResourceDto maintainer = null;

    // -------------------------------------------------------------------------------------------------------------
    // IDENTIFIABLE ARTEFACTS
    // -------------------------------------------------------------------------------------------------------------

    public static RelatedResourceDto getIdentifiableArtefactDtoAsRelatedResourceDto(IdentifiableArtefactDto identifiableArtefactDto) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setCode(identifiableArtefactDto.getCode());
        relatedResourceDto.setUrn(identifiableArtefactDto.getUrn());
        return relatedResourceDto;
    }

    // -------------------------------------------------------------------------------------------------------------
    // NAMEABLE ARTEFACTS
    // -------------------------------------------------------------------------------------------------------------

    public static RelatedResourceDto getNameableArtefactDtoAsRelatedResourceDto(NameableArtefactDto nameableArtefactDto) {
        RelatedResourceDto relatedResourceDto = getIdentifiableArtefactDtoAsRelatedResourceDto(nameableArtefactDto);
        relatedResourceDto.setTitle(nameableArtefactDto.getName());
        return relatedResourceDto;
    }

    // -------------------------------------------------------------------------------------------------------------
    // DSDs
    // -------------------------------------------------------------------------------------------------------------

    public static RelatedResourceDto getDimensionComponentDtoAsRelatedResourceDto(DimensionComponentDto dimensionComponentDto) {
        RelatedResourceDto relatedResourceDto = getIdentifiableArtefactDtoAsRelatedResourceDto(dimensionComponentDto);
        relatedResourceDto.setType(TypeExternalArtefactsEnum.COMPONENT);
        return relatedResourceDto;
    }

    public static List<RelatedResourceDto> getDimensionComponentDtosAsRelatedResourceDtos(List<DimensionComponentDto> dimensionComponentDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(dimensionComponentDtos.size());
        for (DimensionComponentDto dimension : dimensionComponentDtos) {
            relatedResourceDtos.add(getDimensionComponentDtoAsRelatedResourceDto(dimension));
        }
        return relatedResourceDtos;
    }

    // -------------------------------------------------------------------------------------------------------------
    // ORGANISATIONS
    // -------------------------------------------------------------------------------------------------------------

    public static RelatedResourceDto getDefaultMaintainerAsRelatedResourceDto() {
        if (maintainer == null) {
            OrganisationMetamacDto organisationMetamacDto = MetamacSrmWeb.getDefaultMaintainer();
            maintainer = new RelatedResourceDto();
            if (organisationMetamacDto != null) {
                maintainer.setCode(organisationMetamacDto.getCode());
                maintainer.setUrn(organisationMetamacDto.getUrn());
                maintainer.setType(TypeExternalArtefactsEnum.AGENCY);
            }
        }
        return maintainer;
    }

    // -------------------------------------------------------------------------------------------------------------
    // CONCEPTS
    // -------------------------------------------------------------------------------------------------------------

    // Concepts

    public static RelatedResourceDto getConceptMetamacDtoAsRelatedResourceDto(ConceptMetamacDto concept) {
        RelatedResourceDto relatedResourceDto = getNameableArtefactDtoAsRelatedResourceDto(concept);
        relatedResourceDto.setType(TypeExternalArtefactsEnum.CONCEPT);
        return relatedResourceDto;
    }

    public static List<RelatedResourceDto> getConceptMetamacDtosAsRelatedResourceDtos(List<ConceptMetamacDto> conceptMetamacDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(conceptMetamacDtos.size());
        for (ConceptMetamacDto concept : conceptMetamacDtos) {
            relatedResourceDtos.add(getConceptMetamacDtoAsRelatedResourceDto(concept));
        }
        return relatedResourceDtos;
    }

    // -------------------------------------------------------------------------------------------------------------
    // CATEGORIES
    // -------------------------------------------------------------------------------------------------------------

    // Category schemes

    public static RelatedResourceDto getCategorySchemeMetamacDtoAsRelatedResourceDto(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        RelatedResourceDto relatedResourceDto = getNameableArtefactDtoAsRelatedResourceDto(categorySchemeMetamacDto);
        relatedResourceDto.setType(TypeExternalArtefactsEnum.CATEGORY_SCHEME);
        return relatedResourceDto;
    }

    public static List<RelatedResourceDto> geCategorySchemeMetamacDtosAsRelatedResourceDtos(List<CategorySchemeMetamacDto> categorySchemeMetamacDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(categorySchemeMetamacDtos.size());
        for (CategorySchemeMetamacDto categoryScheme : categorySchemeMetamacDtos) {
            relatedResourceDtos.add(getCategorySchemeMetamacDtoAsRelatedResourceDto(categoryScheme));
        }
        return relatedResourceDtos;
    }

    // Categories

    public static RelatedResourceDto getCategoryMetamacDtoAsRelatedResourceDto(CategoryMetamacDto categoryMetamacDto) {
        RelatedResourceDto relatedResourceDto = getNameableArtefactDtoAsRelatedResourceDto(categoryMetamacDto);
        relatedResourceDto.setType(TypeExternalArtefactsEnum.CATEGORY);
        return relatedResourceDto;
    }

    public static List<RelatedResourceDto> getCategoryMetamacDtosAsRelatedResourceDtos(List<CategoryMetamacDto> categoryMetamacDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(categoryMetamacDtos.size());
        for (CategoryMetamacDto category : categoryMetamacDtos) {
            relatedResourceDtos.add(getCategoryMetamacDtoAsRelatedResourceDto(category));
        }
        return relatedResourceDtos;
    }

    // -------------------------------------------------------------------------------------------------------------
    // CODES
    // -------------------------------------------------------------------------------------------------------------

    // Codelists

    public static RelatedResourceDto getCodelistDtoAsRelatedResourceDto(CodelistMetamacDto codelist) {
        RelatedResourceDto relatedResourceDto = getNameableArtefactDtoAsRelatedResourceDto(codelist);
        relatedResourceDto.setType(TypeExternalArtefactsEnum.CODELIST);
        return relatedResourceDto;
    }

    public static List<RelatedResourceDto> getCodelistDtosAsRelatedResourceDtos(List<CodelistMetamacDto> codelistDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(codelistDtos.size());
        for (CodelistMetamacDto codelist : codelistDtos) {
            relatedResourceDtos.add(getCodelistDtoAsRelatedResourceDto(codelist));
        }
        return relatedResourceDtos;
    }

    // Codelist families

    public static RelatedResourceDto getCodelistFamilyDtoAsRelatedResourceDto(CodelistFamilyDto codelistFamily) {
        return getNameableArtefactDtoAsRelatedResourceDto(codelistFamily);
    }

    public static List<RelatedResourceDto> getCodelistFamilyDtosAsRelatedResourceDtos(List<CodelistFamilyDto> codelistFamilyDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(codelistFamilyDtos.size());
        for (CodelistFamilyDto family : codelistFamilyDtos) {
            relatedResourceDtos.add(getCodelistFamilyDtoAsRelatedResourceDto(family));
        }
        return relatedResourceDtos;
    }

    // Variables

    public static RelatedResourceDto getVariableDtoAsRelatedResourceDto(VariableDto variable) {
        return getNameableArtefactDtoAsRelatedResourceDto(variable);
    }

    public static List<RelatedResourceDto> getVariableDtosAsRelatedResourceDtos(List<VariableDto> variableDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>();
        for (VariableDto codelist : variableDtos) {
            relatedResourceDtos.add(getVariableDtoAsRelatedResourceDto(codelist));
        }
        return relatedResourceDtos;
    }

    // Variable families

    public static RelatedResourceDto getVariableFamilyDtoAsRelatedResourceDto(VariableFamilyDto variableFamily) {
        return getNameableArtefactDtoAsRelatedResourceDto(variableFamily);
    }

    public static List<RelatedResourceDto> getVariableFamilyDtosAsRelatedResourceDtos(List<VariableFamilyDto> variableFamilyDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(variableFamilyDtos.size());
        for (VariableFamilyDto family : variableFamilyDtos) {
            relatedResourceDtos.add(getVariableFamilyDtoAsRelatedResourceDto(family));
        }
        return relatedResourceDtos;
    }

    // Variable elements

    public static RelatedResourceDto getVariableElementDtoAsRelatedResourceDto(VariableElementDto variableElement) {
        return getNameableArtefactDtoAsRelatedResourceDto(variableElement);
    }

    public static List<RelatedResourceDto> getVariableElementDtosAsRelatedResourceDtos(List<VariableElementDto> variableElementDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(variableElementDtos.size());
        for (VariableElementDto element : variableElementDtos) {
            relatedResourceDtos.add(getVariableElementDtoAsRelatedResourceDto(element));
        }
        return relatedResourceDtos;
    }

    // -------------------------------------------------------------------------------------------------------------
    // GENERIC RELATED RESOURCES
    // -------------------------------------------------------------------------------------------------------------

    public static RelatedResourceDto createRelatedResourceDto(TypeExternalArtefactsEnum type, String urn) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setType(type);
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
        List<String> urns = new ArrayList<String>(relatedResourceDtos.size());
        for (RelatedResourceDto relatedResourceDto : relatedResourceDtos) {
            urns.add(relatedResourceDto.getUrn());
        }
        return urns;
    }

    public static List<String> getCodesFromRelatedResourceDtos(List<RelatedResourceDto> relatedResourceDtos) {
        List<String> codes = new ArrayList<String>(relatedResourceDtos.size());
        for (RelatedResourceDto relatedResourceDto : relatedResourceDtos) {
            codes.add(relatedResourceDto.getCode());
        }
        return codes;
    }

    public static boolean isRelatedResourceInList(List<RelatedResourceDto> relatedResourceDtos, RelatedResourceDto relatedResourceDto) {
        for (RelatedResourceDto r : relatedResourceDtos) {
            if (StringUtils.equals(relatedResourceDto.getUrn(), r.getUrn())) {
                return true;
            }
        }
        return false;
    }

    public static List<RelatedResourceDto> substractLists(List<RelatedResourceDto> list1, List<RelatedResourceDto> list2) {
        List<RelatedResourceDto> result = new ArrayList<RelatedResourceDto>();
        for (RelatedResourceDto r : list1) {
            if (!isRelatedResourceInList(list2, r)) {
                result.add(r);
            }
        }
        return result;
    }

    // TODO REMOVE THIS METHOD: RelatedResourceDto instead of ExternalItemDto
    public static ExternalItemDto createExternalItemDto(TypeExternalArtefactsEnum type, String urn) {
        ExternalItemDto externalItemDto = new ExternalItemDto();
        externalItemDto.setCode(urn);
        externalItemDto.setUri("dummy");
        externalItemDto.setType(type);
        externalItemDto.setUrn(urn);
        return externalItemDto;
    }

    // TODO REMOVE THIS METHOD: RelatedResourceDto instead of ExternalItemDto
    public static List<ExternalItemDto> createExternalItemDtosFromRelatedResourceDtos(List<RelatedResourceDto> relatedResourceDtos) {
        List<ExternalItemDto> externalItemDtos = new ArrayList<ExternalItemDto>();
        for (RelatedResourceDto relatedResourceDto : relatedResourceDtos) {
            externalItemDtos.add(createExternalItemDto(relatedResourceDto.getType(), relatedResourceDto.getUrn()));
        }
        return externalItemDtos;
    }

    // TODO REMOVE THIS METHOD: RelatedResourceDto instead of ExternalItemDto
    public static List<RelatedResourceDto> createRelatedResourceDtosFromExternalItemDtos(Set<ExternalItemDto> externalItemDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>();
        for (ExternalItemDto externalItemDto : externalItemDtos) {
            relatedResourceDtos.add(createRelatedResourceDto(externalItemDto.getType(), externalItemDto.getUrn()));
        }
        return relatedResourceDtos;
    }

    public static LinkedHashMap<String, String> getRelatedResourceHashMap(List<RelatedResourceDto> relatedResourceDtos) {
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<String, String>();
        hashMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
        for (RelatedResourceDto relatedResourceDto : relatedResourceDtos) {
            hashMap.put(relatedResourceDto.getUrn(), RelatedResourceUtils.getRelatedResourceName(relatedResourceDto));
        }
        return hashMap;
    }

    public static String getRelatedResourceName(RelatedResourceDto relatedResourceDto) {
        if (relatedResourceDto != null) {
            return CommonWebUtils.getElementName(relatedResourceDto.getCode(), relatedResourceDto.getTitle());
        } else {
            return StringUtils.EMPTY;
        }
    }

    public static String getRelatedResourcesName(List<RelatedResourceDto> relatedResourceDtos) {
        List<String> names = new ArrayList<String>(relatedResourceDtos.size());
        for (RelatedResourceDto relatedResourceDto : relatedResourceDtos) {
            names.add(getRelatedResourceName(relatedResourceDto));
        }
        return CommonWebUtils.getStringListToString(names);
    }
}
