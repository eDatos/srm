package org.siemac.metamac.srm.web.shared.organisation;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.domain.shared.OrganisationMetamacVisualisationResult;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetOrganisationsByScheme {

    @In(1)
    String                                       schemeUrn;

    @In(2)
    String                                       locale;

    @Out(1)
    List<OrganisationMetamacVisualisationResult> organisations;
}
