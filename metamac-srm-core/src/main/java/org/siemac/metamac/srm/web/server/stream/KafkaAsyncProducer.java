package org.siemac.metamac.srm.web.server.stream;

import org.siemac.metamac.srm.core.facade.StreamMessagingServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class KafkaAsyncProducer implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaAsyncProducer.class);

    @Autowired
    @Qualifier("streamMessagingServiceFacade")
    public StreamMessagingServiceFacade streamMessagingServiceFacade;

    @Override
    @Async
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            streamMessagingServiceFacade.resendAllPendingAndFailedMessages();
        } catch (Exception e) {
            LOGGER.error("Could not send kafka messages", e);
        }
    }
}
