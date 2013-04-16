package org.siemac.metamac.srm.web.shared.code;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class VersionCodelist {

    @In(1)
    String             urn;

    @In(2)
    VersionTypeEnum    versionType;

    @In(3)
    Boolean            versionCodes;

    @Out(1)
    Boolean            isPlannedInBackground;

    @Out(2)
    CodelistMetamacDto codelistMetamacDto;
}
