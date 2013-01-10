package org.siemac.metamac.srm.web.shared.code;

import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveCodelistFamily {

    @In(1)
    CodelistFamilyDto codelistFamilyDto;

    @Out(1)
    CodelistFamilyDto savedCodelistFamilyDto;
}
