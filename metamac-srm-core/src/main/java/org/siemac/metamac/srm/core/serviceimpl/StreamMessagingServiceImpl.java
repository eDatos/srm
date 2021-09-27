package org.siemac.metamac.srm.core.serviceimpl;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.serviceimpl.result.SendStreamMessageResult;
import org.siemac.metamac.srm.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.srm.core.stream.mappers.Do2AvroMapper;
import org.siemac.metamac.srm.core.serviceapi.StreamMessagingService;
import org.siemac.metamac.srm.web.server.stream.AvroMessage;
import org.siemac.metamac.srm.web.server.stream.KafkaCustomProducer;
import org.siemac.metamac.srm.web.server.stream.MessageBase;
import org.siemac.metamac.srm.web.server.stream.ProducerBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static org.siemac.edatos.core.common.constants.shared.ConfigurationConstants.KAFKA_BOOTSTRAP_SERVERS;
import static org.siemac.edatos.core.common.constants.shared.ConfigurationConstants.KAFKA_SCHEMA_REGISTRY_URL;

@Service
public class StreamMessagingServiceImpl implements StreamMessagingService, ApplicationListener<ContextClosedEvent> {

    private static final Logger LOGGER                = LoggerFactory.getLogger(StreamMessagingServiceImpl.class);
    private static final String CONSUMER_QUERY_1_NAME = "structural_resources_producer";

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    private ProducerBase<Object, SpecificRecordBase> producer;

    @Override
    public <E, A extends SpecificRecordBase, M extends Do2AvroMapper<E, A>> SendStreamMessageResult sendMessage(E messageContent, StreamMessagingCallback<E, A, M> streamMessagingCallback) {
        try {
            updateMessageStatus(messageContent, StreamMessageStatusEnum.PENDING, streamMessagingCallback);
            buildAndSendMessage(messageContent, streamMessagingCallback.getProducerRecordKey(messageContent), streamMessagingCallback.getMapper());
            updateMessageStatus(messageContent, StreamMessageStatusEnum.SENT, streamMessagingCallback);
            return new SendStreamMessageResult(streamMessagingCallback.getStreamMessageStatus(messageContent), null);
        } catch (MetamacException e) {
            updateMessageStatus(messageContent, StreamMessageStatusEnum.FAILED, streamMessagingCallback);
            return new SendStreamMessageResult(streamMessagingCallback.getStreamMessageStatus(messageContent),
                    Collections.singletonList(new MetamacException(e, ServiceExceptionType.UNABLE_TO_SEND_STREAM_MESSAGING_TO_STREAM_MESSAGING_SERVER)));
        }
    }

    @Override
    public <E, A extends SpecificRecordBase, M extends Do2AvroMapper<E, A>> void resendAllPendingAndFailedMessages(StreamMessagingCallback<E, A, M> streamMessagingCallback) throws MetamacException {
        LOGGER.debug("Checking if there are failed or pending messages waiting to be sent trough Kafka...");
        List<E> messages = streamMessagingCallback.getPendingOrFailedMessages();
        if (messages.isEmpty()) {
            LOGGER.debug("There aren't any messages to be sent through Kafka");
        } else {
            LOGGER.info("{} messages are going to be sent through Kafka", messages.size());
            int sentMessages = 0;
            for (E message : messages) {
                try {
                    LOGGER.debug("Sending message with code '{}'", streamMessagingCallback.getUniqueIdentifier(message));
                    SendStreamMessageResult result = getTransactionTemplate().execute(new MetamacExceptionTransactionCallback<SendStreamMessageResult>() {

                        @Override
                        protected SendStreamMessageResult doInMetamacTransaction(TransactionStatus status) {
                            return sendMessage(message, streamMessagingCallback);
                        }
                    });
                    if (result.isOk()) {
                        sentMessages++;
                    } else {
                        LOGGER.warn("An error occurred while trying to send through Kafka the message with code '{}'", streamMessagingCallback.getUniqueIdentifier(message), result.getMainException());
                    }
                } catch (Exception e) {
                    LOGGER.warn("An error occurred while trying to send through Kafka the message with code '{}'", streamMessagingCallback.getUniqueIdentifier(message), e);
                }
            }
            if (sentMessages == messages.size()) {
                LOGGER.info("All messages sent");
            } else {
                LOGGER.warn("Sent {} of {} messages, check log for more info", sentMessages, messages.size());
            }
        }
    }

    private <E, A extends SpecificRecordBase, M extends Do2AvroMapper<E, A>> void updateMessageStatus(E messageContent, StreamMessageStatusEnum status, StreamMessagingCallback<E, A, M> streamMessagingCallback) {
        if (messageContent != null) {
            streamMessagingCallback.setStreamMessageStatus(messageContent, status);
        }
    }

    private <E, A extends SpecificRecordBase, M extends Do2AvroMapper<E, A>> void buildAndSendMessage(E messageContent, Object key, M mapper) throws MetamacException {
        if (messageContent == null) {
            return;
        }

        A messageContentAvro = mapper.toAvro(messageContent);

        MessageBase<Object, SpecificRecordBase> message = new AvroMessage<>(key, messageContentAvro);
        String topic = configurationService.retrieveKafkaTopicOperationsPublication(); // TODO EDATOS-3433: choose between having a unique topic for every message (and change the retrieve method) or getting each one from streamMessagingCallback

        getProducer().sendMessage(message, topic);
    }

    private ProducerBase<Object, SpecificRecordBase> getProducer() throws MetamacException {
        if (producer == null) {
            producer = new KafkaCustomProducer<>(getProducerProperties());
        }
        return producer;
    }

    private Properties getProducerProperties() throws MetamacException {
        Properties props = new Properties();

        String bootstrapServers = configurationService.retrieveProperty(KAFKA_BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        props.put(ProducerConfig.CLIENT_ID_CONFIG, CONSUMER_QUERY_1_NAME);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        props.put(ProducerConfig.RETRIES_CONFIG, 10);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);

        String kafkaSchemaRegistryUrl = configurationService.retrieveProperty(KAFKA_SCHEMA_REGISTRY_URL);
        props.put(SCHEMA_REGISTRY_URL_CONFIG, kafkaSchemaRegistryUrl);

        return props;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        if (producer != null) {
            producer.close();
            producer = null;
        }
    }

    private TransactionTemplate getTransactionTemplate() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionTemplate;
    }

    abstract static class MetamacExceptionTransactionCallback<T> implements TransactionCallback<T> {

        @Override
        public final T doInTransaction(TransactionStatus status) {
            try {
                return doInMetamacTransaction(status);
            } catch (MetamacException e) {
                throw new RuntimeException("Error in transactional method", e);
            }
        }

        protected abstract T doInMetamacTransaction(TransactionStatus status) throws MetamacException;
    }
}
