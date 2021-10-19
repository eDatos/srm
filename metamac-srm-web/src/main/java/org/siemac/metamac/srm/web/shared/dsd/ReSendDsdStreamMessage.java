package org.siemac.metamac.srm.web.shared.dsd;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

@GenDispatch(isSecure = false)
public class ReSendDsdStreamMessage {

    @In(1)
    String                            dsdUrn;

    @Out(1)
    DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto;

    @Out(2)
    MetamacWebException               notificationException;
}