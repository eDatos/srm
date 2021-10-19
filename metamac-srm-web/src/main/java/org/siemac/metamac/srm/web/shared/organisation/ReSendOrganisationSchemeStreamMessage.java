package org.siemac.metamac.srm.web.shared.organisation;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

@GenDispatch(isSecure = false)
public class ReSendOrganisationSchemeStreamMessage {

    @In(1)
    String                       organisationSchemeUrn;

    @Out(1)
    OrganisationSchemeMetamacDto organisationSchemeMetamacDto;

    @Out(2)
    MetamacWebException          notificationException;
}
