package com.getsimplex.steptimer.service;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;


public class KafkaProducerUtil {

    private final static String BOOTSTRAP_SERVERS = "localhost:9092";

    public static Producer<Long, String>    createProducer(){

        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "STEDI");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        final Producer<Long, String> producer = new KafkaProducer<Long, String>(props);

        return producer;
    }
}
