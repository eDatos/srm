package org.siemac.metamac.srm.core.facade.serviceimpl;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.StreamMessagingServiceFacade;
import org.siemac.metamac.srm.core.serviceapi.StreamMessagingService;
import org.siemac.metamac.srm.core.serviceapi.StreamMessagingService.StreamMessagingCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of StreamMessagingServiceFacade.
 */
@Service("streamMessagingServiceFacade")
public class StreamMessagingServiceFacadeImpl implements StreamMessagingServiceFacade {

    @Autowired
    private StreamMessagingService streamMessagingService;

    @Autowired
    private List<StreamMessagingCallback<?,?,?>> streamMessagingCallbacks;

    @Override
    public void resendAllPendingAndFailedMessages() throws MetamacException {
        for(StreamMessagingCallback<?,?,?> streamMessagingCallback : streamMessagingCallbacks) {
            streamMessagingService.resendAllPendingAndFailedMessages(streamMessagingCallback);
        }
    }
}
