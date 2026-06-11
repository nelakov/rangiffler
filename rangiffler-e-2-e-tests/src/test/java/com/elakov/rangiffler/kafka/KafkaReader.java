package com.elakov.rangiffler.kafka;

import com.elakov.rangiffler.config.services.ServicesConfig;
import org.aeonbits.owner.ConfigCache;
import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.awaitility.Awaitility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

/**
 * Background consumer collecting every record from the watched topics into
 * an in-memory store, so tests can await an expected message by predicate.
 */
public class KafkaReader implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaReader.class);
    private static final ServicesConfig CFG = ConfigCache.getOrCreate(ServicesConfig.class, System.getProperties());

    public static final List<String> TOPICS = List.of("users");

    private static final Map<String, List<ConsumerRecord<String, String>>> STORE = new ConcurrentHashMap<>();
    private final AtomicBoolean threadStarted = new AtomicBoolean(true);
    private final Consumer<String, String> stringConsumer;

    public KafkaReader() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, CFG.kafkaAddress());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "e2e-kafka-reader");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        this.stringConsumer = new KafkaConsumer<>(properties);
        this.stringConsumer.subscribe(TOPICS);
        TOPICS.forEach(topic -> STORE.put(topic, Collections.synchronizedList(new LinkedList<>())));
    }

    public void stop() {
        this.threadStarted.set(false);
    }

    public static ConsumerRecord<String, String> getMessageByPredicate(String topic,
                                                                       Predicate<ConsumerRecord<String, String>> predicate) {
        return Awaitility.await()
                .atMost(Duration.ofSeconds(15))
                .pollInterval(Duration.ofSeconds(1))
                .ignoreExceptions()
                .until(
                        () -> STORE.get(topic).stream().filter(predicate).findFirst().orElse(null),
                        Objects::nonNull
                );
    }

    @Override
    public void run() {
        try {
            while (threadStarted.get()) {
                var records = stringConsumer.poll(Duration.ofMillis(500));
                for (var record : records) {
                    LOG.info("### Kafka record: topic = {}, partition = {}, offset = {}, value = {}",
                            record.topic(), record.partition(), record.offset(), record.value());
                    STORE.get(record.topic()).add(record);
                }
                try {
                    stringConsumer.commitSync();
                } catch (CommitFailedException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            stringConsumer.close();
            Thread.currentThread().interrupt();
        }
    }
}
