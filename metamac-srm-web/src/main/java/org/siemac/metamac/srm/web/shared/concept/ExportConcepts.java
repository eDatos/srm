package org.siemac.metamac.srm.web.shared.concept;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class ExportConcepts {

    @In(1)
    String conceptSchemeUrn;

    @Out(1)
    String fileName;
}
