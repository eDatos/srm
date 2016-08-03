package org.siemac.metamac.srm.web.shared.organisation;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class ExportOrganisations {

    @In(1)
    String organisationSchemeUrn;

    @Out(1)
    String fileName;
}
