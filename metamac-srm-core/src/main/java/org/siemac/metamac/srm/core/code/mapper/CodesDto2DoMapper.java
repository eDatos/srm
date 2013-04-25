package org.siemac.metamac.srm.core.code.mapper;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;

public interface CodesDto2DoMapper {

    public CodelistVersionMetamac codelistDtoToDo(CodelistMetamacDto source) throws MetamacException;
    public CodeMetamac codeDtoToDo(CodeMetamacDto source) throws MetamacException;
    public CodelistFamily codelistFamilyDtoToDo(CodelistFamilyDto source) throws MetamacException;
    public VariableFamily variableFamilyDtoToDo(VariableFamilyDto source) throws MetamacException;
    public Variable variableDtoToDo(VariableDto source) throws MetamacException;
    public VariableElement variableElementDtoToDo(VariableElementDto source) throws MetamacException;
    public CodelistOrderVisualisation codelistOrderVisualisationDtoToDo(CodelistVisualisationDto source) throws MetamacException;
    public CodelistOpennessVisualisation codelistOpennessVisualisationDtoToDo(CodelistVisualisationDto source) throws MetamacException;
    public CodelistOrderVisualisation retrieveCodelistOrderVisualisation(String urn) throws MetamacException;
    public CodelistOpennessVisualisation retrieveCodelistOpennessVisualisation(String urn) throws MetamacException;

}