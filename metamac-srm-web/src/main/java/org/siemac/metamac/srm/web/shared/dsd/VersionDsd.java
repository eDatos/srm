package org.siemac.metamac.srm.web.shared.dsd;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class VersionDsd {

    @In(1)
    String                            urn;

    @In(2)
    VersionTypeEnum                   versionType;

    @Out(1)
    DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto;

}
