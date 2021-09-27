package org.siemac.metamac.srm.web.server.stream;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;

public class KafkaCustomProducer<K, V extends SpecificRecordBase> implements ProducerBase<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaCustomProducer.class);
    private static final int KAFKA_TIMEOUT = 500;
    private static final String[] MANDATORY_SETTINGS = {SCHEMA_REGISTRY_URL_CONFIG, BOOTSTRAP_SERVERS_CONFIG};
    private static final Map<String, Object> DEFAULT_SETTINGS;

    private final KafkaProducer<K, V> producer;

    static {
        DEFAULT_SETTINGS = new HashMap<>();
        DEFAULT_SETTINGS.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        DEFAULT_SETTINGS.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        DEFAULT_SETTINGS.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000); // Time to wait if Kafka cluster is down or buffer is full
        DEFAULT_SETTINGS.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 10485760); // 10 MB, note: The Broker has: message.max.bytes=10485760 (10MB)
        DEFAULT_SETTINGS.put(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, Duration.ofMinutes(1).toMillis());
        DEFAULT_SETTINGS.put(ProducerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, Duration.ofHours(6).toMillis());
    }

    public KafkaCustomProducer(Properties props) throws MetamacException {
        super();
        checkForMissingProperties(props);
        producer = new KafkaProducer<>(props);
    }

    @Override
    public void sendMessage(MessageBase<K, V> message, String topic) throws MetamacException {
        ProducerRecord<K, V> producerRecord = new ProducerRecord<>(topic, message.getKey(), message.getContent());
        try {
            Future<RecordMetadata> sendResult = producer.send(producerRecord);
            sendResult.get(KAFKA_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (SerializationException | ExecutionException | TimeoutException e) {
            throw MetamacExceptionBuilder.builder().withCause(e).build();
        } catch (InterruptedException e) {
            LOGGER.error("Thread interrupted, could not send message", e);
            Thread.currentThread().interrupt(); // See SonarLint java:S2142
        }
    }

    private Properties checkForMissingProperties(Properties props) throws MetamacException {
        checkMissingMandatoryProperties(props);
        return fillMissingDefaultProperties(props);
    }

    private void checkMissingMandatoryProperties(Properties props) throws MetamacException {
        for (String prop : KafkaCustomProducer.MANDATORY_SETTINGS) {
            if (!props.containsKey(prop)) {
                throw new MetamacExceptionBuilder().withMessageParameters(ServiceExceptionType.STREAM_MESSAGING_MISSING_MANDATORY_SETTINGS.getMessageForReasonType()).build();
            }
        }
    }

    private Properties fillMissingDefaultProperties(Properties props) {
        for (Object key : DEFAULT_SETTINGS.keySet()) {
            if (!props.containsKey(key)) {
                props.put(key, DEFAULT_SETTINGS.get(key));
            }
        }
        return props;
    }

    @Override
    public void close() {
        if (null != producer) {
            producer.close(Duration.ZERO);
        }
    }
}
