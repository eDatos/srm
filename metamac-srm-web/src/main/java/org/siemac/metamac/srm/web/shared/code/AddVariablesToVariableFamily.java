package org.siemac.metamac.srm.web.shared.code;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class AddVariablesToVariableFamily {

    @In(1)
    List<String>      variableUrns;

    @In(2)
    String            variableFamilyUrn;

    @Out(1)
    VariableFamilyDto variableFamilyDto;
}
