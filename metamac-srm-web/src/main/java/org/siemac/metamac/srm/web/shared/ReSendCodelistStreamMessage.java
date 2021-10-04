package org.siemac.metamac.srm.web.shared;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

@GenDispatch(isSecure = false)
public class ReSendCodelistStreamMessage {

    @In(1)
    String              codelistUrn;

    @Out(2)
    MetamacWebException notificationException;

}
