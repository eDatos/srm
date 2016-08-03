package org.siemac.metamac.srm.web.shared.organisation;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class DeleteOrganisations {

    @In(1)
    List<String> urns;
}
