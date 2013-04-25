package org.siemac.metamac.srm.web.shared.organisation;

import java.util.List;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetOrganisationsByScheme {

    @In(1)
    String                        schemeUrn;

    @In(2)
    String                        locale;

    @Out(1)
    List<ItemVisualisationResult> organisations;
}
