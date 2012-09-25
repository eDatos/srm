package org.siemac.metamac.srm.web.shared.organisation;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class VersionOrganisationScheme {

    @In(1)
    String                       urn;

    @In(2)
    VersionTypeEnum              versionType;

    @Out(1)
    OrganisationSchemeMetamacDto organisationSchemeMetamacDto;

}
