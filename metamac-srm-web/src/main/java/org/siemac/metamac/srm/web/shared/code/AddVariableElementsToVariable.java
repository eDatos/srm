package org.siemac.metamac.srm.web.shared.code;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class AddVariableElementsToVariable {

    @In(1)
    List<String> variableElementUrns;

    @In(2)
    String       variableUrn;
}
