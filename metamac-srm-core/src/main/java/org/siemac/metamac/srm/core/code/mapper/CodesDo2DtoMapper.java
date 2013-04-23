package org.siemac.metamac.srm.core.code.mapper;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableElementOperation;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.code.dto.VariableBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementOperationDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public interface CodesDo2DtoMapper {

    // Codelists
    public CodelistMetamacDto codelistMetamacDoToDto(CodelistVersionMetamac source);
    public CodelistMetamacBasicDto codelistMetamacDoToBasicDto(CodelistVersionMetamac source);
    public RelatedResourceDto codelistMetamacDoToRelatedResourceDto(CodelistVersionMetamac source);
    public List<CodelistMetamacBasicDto> codelistMetamacDoListToDtoList(List<CodelistVersionMetamac> sources);

    // Codes
    public CodeMetamacDto codeMetamacDoToDto(CodeMetamac source);
    public CodeMetamacBasicDto codeMetamacDoToBasicDto(CodeMetamac source);

    // Codelist family
    public CodelistFamilyDto codelistFamilyDoToDto(CodelistFamily source);
    public CodelistFamilyBasicDto codelistFamilyDoToBasicDto(CodelistFamily source);

    // Variable family
    public VariableFamilyDto variableFamilyDoToDto(VariableFamily source);
    public VariableFamilyBasicDto variableFamilyDoToBasicDto(VariableFamily source);

    // Variable
    public VariableDto variableDoToDto(Variable source);
    public VariableBasicDto variableDoToBasicDto(Variable source);
    public RelatedResourceDto variableDoToRelatedResourceDto(Variable source);

    // Variable element
    public VariableElementDto variableElementDoToDto(VariableElement source);
    public VariableElementBasicDto variableElementDoToBasicDto(VariableElement source);
    public RelatedResourceDto variableElementDoToRelatedResourceDto(VariableElement source);
    public VariableElementOperationDto variableElementOperationDoToDto(VariableElementOperation source);
    public List<VariableElementOperationDto> variableElementOperationsDoToDto(List<VariableElementOperation> sources);

    // Visualisations
    public CodelistVisualisationDto codelistOrderVisualisationDoToDto(CodelistOrderVisualisation source);
    public List<CodelistVisualisationDto> codelistOrderVisualisationsDoToDto(List<CodelistOrderVisualisation> sources);
    public CodelistVisualisationDto codelistOpennessVisualisationDoToDto(CodelistOpennessVisualisation source);
    public List<CodelistVisualisationDto> codelistOpennessVisualisationsDoToDto(List<CodelistOpennessVisualisation> sources);

}
