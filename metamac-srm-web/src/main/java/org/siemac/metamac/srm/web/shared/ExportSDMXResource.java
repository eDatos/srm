package org.siemac.metamac.srm.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class ExportSDMXResource {

    @In(1)
    String urn;

    @Out(1)
    String fileName;
}
