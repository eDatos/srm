package org.siemac.metamac.srm.web.shared.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.base.dto.IdentifiableArtefactMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.NameableArtefactMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.VariableBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.web.common.shared.RelatedResourceBaseUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.IdentifiableArtefactDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.NameableArtefactDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;

public class RelatedResourceUtils extends RelatedResourceBaseUtils {

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

    public static RelatedResourceDto getIdentifiableArtefactMetamacBasicDtoAsRelatedResourceDto(IdentifiableArtefactMetamacBasicDto identifiableArtefactMetamacBasicDto) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setCode(identifiableArtefactMetamacBasicDto.getCode());
        relatedResourceDto.setUrn(identifiableArtefactMetamacBasicDto.getUrn());
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

    public static RelatedResourceDto getNameableArtefactMetamacBasicDto(NameableArtefactMetamacBasicDto nameableArtefactMetamacBasicDto) {
        RelatedResourceDto relatedResourceDto = getIdentifiableArtefactMetamacBasicDtoAsRelatedResourceDto(nameableArtefactMetamacBasicDto);
        relatedResourceDto.setTitle(nameableArtefactMetamacBasicDto.getName());
        return relatedResourceDto;
    }

    // -------------------------------------------------------------------------------------------------------------
    // DSDs
    // -------------------------------------------------------------------------------------------------------------

    public static RelatedResourceDto getDimensionComponentDtoAsRelatedResourceDto(DimensionComponentDto dimensionComponentDto) {
        RelatedResourceDto relatedResourceDto = getIdentifiableArtefactDtoAsRelatedResourceDto(dimensionComponentDto);
        relatedResourceDto.setType(RelatedResourceTypeEnum.DIMENSION);
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
                maintainer.setType(RelatedResourceTypeEnum.AGENCY);
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
        relatedResourceDto.setType(RelatedResourceTypeEnum.CONCEPT);
        return relatedResourceDto;
    }

    public static RelatedResourceDto getConceptMetamacBasicDtoAsRelatedResourceDto(ConceptMetamacBasicDto concept) {
        RelatedResourceDto relatedResourceDto = getNameableArtefactMetamacBasicDto(concept);
        relatedResourceDto.setType(RelatedResourceTypeEnum.CONCEPT);
        return relatedResourceDto;
    }

    public static List<RelatedResourceDto> getConceptMetamacDtosAsRelatedResourceDtos(List<ConceptMetamacDto> conceptMetamacDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(conceptMetamacDtos.size());
        for (ConceptMetamacDto concept : conceptMetamacDtos) {
            relatedResourceDtos.add(getConceptMetamacDtoAsRelatedResourceDto(concept));
        }
        return relatedResourceDtos;
    }

    public static List<RelatedResourceDto> getConceptMetamacBasicDtosAsRelatedResourceDtos(List<ConceptMetamacBasicDto> conceptMetamacDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(conceptMetamacDtos.size());
        for (ConceptMetamacBasicDto concept : conceptMetamacDtos) {
            relatedResourceDtos.add(getConceptMetamacBasicDtoAsRelatedResourceDto(concept));
        }
        return relatedResourceDtos;
    }

    // -------------------------------------------------------------------------------------------------------------
    // CATEGORIES
    // -------------------------------------------------------------------------------------------------------------

    // Category schemes

    public static RelatedResourceDto getCategorySchemeMetamacDtoAsRelatedResourceDto(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        RelatedResourceDto relatedResourceDto = getNameableArtefactDtoAsRelatedResourceDto(categorySchemeMetamacDto);
        relatedResourceDto.setType(RelatedResourceTypeEnum.CATEGORY_SCHEME);
        return relatedResourceDto;
    }

    public static RelatedResourceDto getCategorySchemeMetamacBasicDtoAsRelatedResourceDto(CategorySchemeMetamacBasicDto categorySchemeMetamacDto) {
        RelatedResourceDto relatedResourceDto = getNameableArtefactMetamacBasicDto(categorySchemeMetamacDto);
        relatedResourceDto.setType(RelatedResourceTypeEnum.CATEGORY_SCHEME);
        return relatedResourceDto;
    }

    public static List<RelatedResourceDto> geCategorySchemeMetamacDtosAsRelatedResourceDtos(List<CategorySchemeMetamacDto> categorySchemeMetamacDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(categorySchemeMetamacDtos.size());
        for (CategorySchemeMetamacDto categoryScheme : categorySchemeMetamacDtos) {
            relatedResourceDtos.add(getCategorySchemeMetamacDtoAsRelatedResourceDto(categoryScheme));
        }
        return relatedResourceDtos;
    }

    public static List<RelatedResourceDto> geCategorySchemeMetamacBasicDtosAsRelatedResourceDtos(List<CategorySchemeMetamacBasicDto> categorySchemeMetamacDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(categorySchemeMetamacDtos.size());
        for (CategorySchemeMetamacBasicDto categoryScheme : categorySchemeMetamacDtos) {
            relatedResourceDtos.add(getCategorySchemeMetamacBasicDtoAsRelatedResourceDto(categoryScheme));
        }
        return relatedResourceDtos;
    }

    // Categories

    public static RelatedResourceDto getCategoryMetamacDtoAsRelatedResourceDto(CategoryMetamacDto categoryMetamacDto) {
        RelatedResourceDto relatedResourceDto = getNameableArtefactDtoAsRelatedResourceDto(categoryMetamacDto);
        relatedResourceDto.setType(RelatedResourceTypeEnum.CATEGORY);
        return relatedResourceDto;
    }

    public static RelatedResourceDto getCategoryMetamacBasicDtoAsRelatedResourceDto(CategoryMetamacBasicDto categoryMetamacDto) {
        RelatedResourceDto relatedResourceDto = getNameableArtefactMetamacBasicDto(categoryMetamacDto);
        relatedResourceDto.setType(RelatedResourceTypeEnum.CATEGORY);
        return relatedResourceDto;
    }

    public static List<RelatedResourceDto> getCategoryMetamacDtosAsRelatedResourceDtos(List<CategoryMetamacDto> categoryMetamacDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(categoryMetamacDtos.size());
        for (CategoryMetamacDto category : categoryMetamacDtos) {
            relatedResourceDtos.add(getCategoryMetamacDtoAsRelatedResourceDto(category));
        }
        return relatedResourceDtos;
    }

    public static List<RelatedResourceDto> getCategoryMetamacBasicDtosAsRelatedResourceDtos(List<CategoryMetamacBasicDto> categoryMetamacDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(categoryMetamacDtos.size());
        for (CategoryMetamacBasicDto category : categoryMetamacDtos) {
            relatedResourceDtos.add(getCategoryMetamacBasicDtoAsRelatedResourceDto(category));
        }
        return relatedResourceDtos;
    }

    // -------------------------------------------------------------------------------------------------------------
    // CODES
    // -------------------------------------------------------------------------------------------------------------

    // Codelists

    public static RelatedResourceDto getCodelistDtoAsRelatedResourceDto(CodelistMetamacDto codelist) {
        RelatedResourceDto relatedResourceDto = getNameableArtefactDtoAsRelatedResourceDto(codelist);
        relatedResourceDto.setType(RelatedResourceTypeEnum.CODELIST);
        return relatedResourceDto;
    }

    public static RelatedResourceDto getCodelistBasicDtoAsRelatedResourceDto(CodelistMetamacBasicDto codelist) {
        RelatedResourceDto relatedResourceDto = getNameableArtefactMetamacBasicDto(codelist);
        relatedResourceDto.setType(RelatedResourceTypeEnum.CODELIST);
        return relatedResourceDto;
    }

    public static List<RelatedResourceDto> getCodelistDtosAsRelatedResourceDtos(List<CodelistMetamacDto> codelistDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(codelistDtos.size());
        for (CodelistMetamacDto codelist : codelistDtos) {
            relatedResourceDtos.add(getCodelistDtoAsRelatedResourceDto(codelist));
        }
        return relatedResourceDtos;
    }

    public static List<RelatedResourceDto> getCodelistBasicDtosAsRelatedResourceDtos(List<CodelistMetamacBasicDto> codelistDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(codelistDtos.size());
        for (CodelistMetamacBasicDto codelist : codelistDtos) {
            relatedResourceDtos.add(getCodelistBasicDtoAsRelatedResourceDto(codelist));
        }
        return relatedResourceDtos;
    }

    // Codelist families

    public static RelatedResourceDto getCodelistFamilyDtoAsRelatedResourceDto(CodelistFamilyDto codelistFamily) {
        return getNameableArtefactDtoAsRelatedResourceDto(codelistFamily);
    }

    public static RelatedResourceDto getCodelistFamilyBasicDtoAsRelatedResourceDto(CodelistFamilyBasicDto codelistFamily) {
        return getNameableArtefactMetamacBasicDto(codelistFamily);
    }

    public static List<RelatedResourceDto> getCodelistFamilyBasicDtosAsRelatedResourceDtos(List<CodelistFamilyBasicDto> codelistFamilyDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(codelistFamilyDtos.size());
        for (CodelistFamilyBasicDto family : codelistFamilyDtos) {
            relatedResourceDtos.add(getCodelistFamilyBasicDtoAsRelatedResourceDto(family));
        }
        return relatedResourceDtos;
    }

    // Variables

    public static RelatedResourceDto getVariableDtoAsRelatedResourceDto(VariableDto variable) {
        return getNameableArtefactDtoAsRelatedResourceDto(variable);
    }

    public static RelatedResourceDto getVariableBasicDtoAsRelatedResourceDto(VariableBasicDto variable) {
        return getNameableArtefactMetamacBasicDto(variable);
    }

    public static List<RelatedResourceDto> getVariableDtosAsRelatedResourceDtos(List<VariableDto> variableDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>();
        for (VariableDto codelist : variableDtos) {
            relatedResourceDtos.add(getVariableDtoAsRelatedResourceDto(codelist));
        }
        return relatedResourceDtos;
    }

    public static List<RelatedResourceDto> getVariableBasicDtosAsRelatedResourceDtos(List<VariableBasicDto> variableDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>();
        for (VariableBasicDto codelist : variableDtos) {
            relatedResourceDtos.add(getVariableBasicDtoAsRelatedResourceDto(codelist));
        }
        return relatedResourceDtos;
    }

    // Variable families

    public static RelatedResourceDto getVariableFamilyDtoAsRelatedResourceDto(VariableFamilyDto variableFamily) {
        return getNameableArtefactDtoAsRelatedResourceDto(variableFamily);
    }

    public static RelatedResourceDto getVariableFamilyBasicDtoAsRelatedResourceDto(VariableFamilyBasicDto variableFamilyBasicDto) {
        return getNameableArtefactMetamacBasicDto(variableFamilyBasicDto);
    }

    public static List<RelatedResourceDto> getVariableFamilyDtosAsRelatedResourceDtos(List<VariableFamilyDto> variableFamilyDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(variableFamilyDtos.size());
        for (VariableFamilyDto family : variableFamilyDtos) {
            relatedResourceDtos.add(getVariableFamilyDtoAsRelatedResourceDto(family));
        }
        return relatedResourceDtos;
    }

    public static List<RelatedResourceDto> getVariableFamilyBasicDtosAsRelatedResourceDtos(List<VariableFamilyBasicDto> variableFamilyBasicDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(variableFamilyBasicDtos.size());
        for (VariableFamilyBasicDto family : variableFamilyBasicDtos) {
            relatedResourceDtos.add(getVariableFamilyBasicDtoAsRelatedResourceDto(family));
        }
        return relatedResourceDtos;
    }

    // Variable elements

    public static RelatedResourceDto getVariableElementDtoAsRelatedResourceDto(VariableElementDto variableElement) {
        RelatedResourceDto relatedResourceDto = getIdentifiableArtefactDtoAsRelatedResourceDto(variableElement);
        relatedResourceDto.setTitle(variableElement.getShortName());
        return relatedResourceDto;
    }

    public static RelatedResourceDto getVariableElementBasicDtoAsRelatedResourceDto(VariableElementBasicDto variableElement) {
        RelatedResourceDto relatedResourceDto = getIdentifiableArtefactMetamacBasicDtoAsRelatedResourceDto(variableElement);
        relatedResourceDto.setTitle(variableElement.getShortName());
        return relatedResourceDto;
    }

    public static List<RelatedResourceDto> getVariableElementDtosAsRelatedResourceDtos(List<VariableElementDto> variableElementDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(variableElementDtos.size());
        for (VariableElementDto element : variableElementDtos) {
            relatedResourceDtos.add(getVariableElementDtoAsRelatedResourceDto(element));
        }
        return relatedResourceDtos;
    }

    public static List<RelatedResourceDto> getVariableElementBasicDtosAsRelatedResourceDtos(List<VariableElementBasicDto> variableElementDtos) {
        List<RelatedResourceDto> relatedResourceDtos = new ArrayList<RelatedResourceDto>(variableElementDtos.size());
        for (VariableElementBasicDto element : variableElementDtos) {
            relatedResourceDtos.add(getVariableElementBasicDtoAsRelatedResourceDto(element));
        }
        return relatedResourceDtos;
    }

    // -------------------------------------------------------------------------------------------------------------
    // GENERIC RELATED RESOURCES
    // -------------------------------------------------------------------------------------------------------------

    public static RelatedResourceDto createRelatedResourceDto(RelatedResourceTypeEnum type, String urn) {
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
}
