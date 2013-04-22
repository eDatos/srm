package org.siemac.metamac.srm.web.shared.code;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeVariableElementNormalisationResult;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class NormaliseVariableElementsToCodes {

    @In(1)
    String                                       codelistUrn;

    @In(2)
    String                                       locale;

    @Out(1)
    List<CodeVariableElementNormalisationResult> codeVariableElementNormalisationResults;
}
