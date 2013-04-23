package org.siemac.metamac.srm.web.shared.code;

import java.io.InputStream;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class ImportVariableElementCSVInBackground {

    @In(1)
    String      variableUrn;

    @In(2)
    InputStream csvStream;

    @In(3)
    String      fileName;

    @In(4)
    Boolean     updateAlreadyExisting;
}
