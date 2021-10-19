package org.siemac.metamac.srm.web.server.stream;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.TopicExistsException;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Component
public class KafkaInitializeTopic implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaInitializeTopic.class);
    private static final int NUM_OF_PARTITIONS = 1;
    private static final short NUM_OF_REPLICATION = (short) 1;
    private static final int TIMEOUT = 1000;
    private static final String RETENTION_MS = "retention.ms";
    private static final Map<String, String> TOPIC_DEFAULT_SETTINGS;

    static {
        TOPIC_DEFAULT_SETTINGS = new HashMap<>();
        TOPIC_DEFAULT_SETTINGS.put(RETENTION_MS, "-1");
    }

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext ac = event.getApplicationContext();
        if (ac.getParent() == null) {
            try {
                LOGGER.info("Initializing Kafka topics...");
                propagateCreationOfTopics();
            } catch (Exception e) {
                LOGGER.error("Could not initialize Kafka topics", e);
            }
        }
    }

    private void propagateCreationOfTopics() throws MetamacException {
        Properties kafkaProperties = getKafkaProperties();
        List<NewTopic> topics = getTopics();
        CreateTopicsOptions topicsOptions = getTopicsOptions();
        LOGGER.info("Kafka Properties: {}, Topics: {}", kafkaProperties, topics);
        createTopics(kafkaProperties, topics, topicsOptions);
    }

    private Properties getKafkaProperties() throws MetamacException {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, configurationService.retrieveKafkaBootStrapServers());
        return properties;
    }

    private List<NewTopic> getTopics() throws MetamacException {
        List<NewTopic> topics = new ArrayList<>();
        topics.add(createTopic(configurationService.retrieveKafkaTopicCodelistsPublication()));
        topics.add(createTopic(configurationService.retrieveKafkaTopicDsdPublication()));
        topics.add(createTopic(configurationService.retrieveKafkaTopicConceptSchemesPublication()));
        topics.add(createTopic(configurationService.retrieveKafkaTopicOrganisationSchemesPublication()));
        topics.add(createTopic(configurationService.retrieveKafkaTopicCategorySchemesPublication()));
        return topics;
    }

    private NewTopic createTopic(String topic) {
        return new NewTopic(topic, NUM_OF_PARTITIONS, NUM_OF_REPLICATION).configs(TOPIC_DEFAULT_SETTINGS);
    }

    private CreateTopicsOptions getTopicsOptions() {
        return new CreateTopicsOptions().timeoutMs(TIMEOUT);
    }

    private void createTopics(Properties kafkaProperties, List<NewTopic> topics, CreateTopicsOptions topicsOptions) {
        try (AdminClient adminClient = AdminClient.create(kafkaProperties)) {
            adminClient.createTopics(topics, topicsOptions).all().get();
        } catch (KafkaException | ExecutionException e) {
            if (e.getCause() instanceof TopicExistsException) {
                LOGGER.info("{}", e.getCause().getMessage());
            } else {
                throw new RuntimeException("Imposible to create/check Topic in Kafka", e);
            }
        } catch (InterruptedException e) {
            LOGGER.error("Thread interrupted, imposible to create/check Topic in Kafka", e);
            Thread.currentThread().interrupt(); // See SonarLint java:S2142
        }
    }
}