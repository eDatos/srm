package org.siemac.metamac.srm.web.shared.code;

import java.io.InputStream;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class ImportCodeOrdersCsvInBackground {

    @In(1)
    String      codelistUrn;

    @In(2)
    InputStream csvStream;

    @In(3)
    String      fileName;
}
