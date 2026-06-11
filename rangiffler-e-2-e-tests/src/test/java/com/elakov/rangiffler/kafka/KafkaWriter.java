package com.elakov.rangiffler.kafka;

import com.elakov.rangiffler.config.services.ServicesConfig;
import org.aeonbits.owner.ConfigCache;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaWriter {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaWriter.class);
    private static final ServicesConfig CFG = ConfigCache.getOrCreate(ServicesConfig.class, System.getProperties());
    private static final Producer<String, String> stringProducer;

    static {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, CFG.kafkaAddress());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        stringProducer = new KafkaProducer<>(properties);
    }

    private KafkaWriter() {
    }

    public static void sendRecordToTopic(ProducerRecord<String, String> producerRecord) {
        stringProducer.send(producerRecord, (metadata, exception) -> {
            if (exception != null) {
                LOG.error(exception.getMessage(), exception);
            } else {
                LOG.info("### Produced event to topic {}: key = {}, value = {}",
                        producerRecord.topic(), producerRecord.key(), producerRecord.value());
            }
        });
    }
}
