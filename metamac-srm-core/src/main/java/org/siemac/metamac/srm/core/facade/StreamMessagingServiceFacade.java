package org.siemac.metamac.srm.core.facade;

import org.siemac.metamac.core.common.exception.MetamacException;

public interface StreamMessagingServiceFacade {
    void resendAllPendingAndFailedMessages() throws MetamacException;
}
