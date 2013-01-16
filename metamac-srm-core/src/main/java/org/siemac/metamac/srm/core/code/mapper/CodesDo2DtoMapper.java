package org.siemac.metamac.srm.core.code.mapper;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.dto.CodeHierarchyDto;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistOrderVisualisationDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public interface CodesDo2DtoMapper {

    // Codelists
    public CodelistMetamacDto codelistMetamacDoToDto(CodelistVersionMetamac source);
    public List<CodelistMetamacDto> codelistMetamacDoListToDtoList(List<CodelistVersionMetamac> sources);

    // Codes
    public CodeMetamacDto codeMetamacDoToDto(CodeMetamac source);
    public List<CodeMetamacDto> codeMetamacDoListToDtoList(List<CodeMetamac> sources);
    public List<CodeHierarchyDto> codeMetamacDoListToCodeHierarchyDtoList(List<CodeMetamac> sources, CodelistOrderVisualisation codelistOrderVisualisation);

    // Codelist family
    public CodelistFamilyDto codelistFamilyDoToDto(CodelistFamily source);

    // Variable family
    public VariableFamilyDto variableFamilyDoToDto(VariableFamily source);

    // Variable
    public VariableDto variableDoToDto(Variable source);
    public RelatedResourceDto variableDoToRelatedResourceDto(Variable source);

    // Variable element
    public VariableElementDto variableElementDoToDto(VariableElement source);
    public RelatedResourceDto variableElementDoToRelatedResourceDto(VariableElement source);

    // Visualisations
    public CodelistOrderVisualisationDto codelistOrderVisualisationDoToDto(CodelistOrderVisualisation source);
    public List<CodelistOrderVisualisationDto> codelistOrderVisualisationsDoToDto(List<CodelistOrderVisualisation> sources);
}
