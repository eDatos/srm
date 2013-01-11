package org.siemac.metamac.srm.web.shared.code;

import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveVariableFamily {

    @In(1)
    VariableFamilyDto variableFamilyDto;

    @Out(1)
    VariableFamilyDto savedVariableFamilyDto;
}
