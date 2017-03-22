package com.otz.transport.config;

import com.otz.transport.common.ProxyKafkaProducer;
import com.otz.transport.producer.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Properties;

/**
 * Copyright 2016 opentoolzone.com - Kafka Transport
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Created by alexdorand on 2016-12-10.
 */
@Configuration
public class TransportProducerConfiguration {

    public static final String KAFKA = ".kafka";
    public static final String KEY_SERIALIZER = "key.serializer";
    public static final String TRANSPORT_KAFKA_KEY_SERIALIZER = "transport.kafka.key.serializer"; // transport.kafka.key.StringSerializer
    public static final String VALUE_SERIALIZER = "value.serializer";
    public static final String TRANSPORT_KAFKA_VALUE_SERIALIZER = "transport.kafka.value.serializer"; // transport.kafka.value.StringSerializer
    public static final String MAX_BLOCK = "max.block.ms";
    public static final String TRANSPORT_MAX_BLOCK = "transport.kafka.max.block.ms"; // 100
    public static final String ACK = "acks"; // -1
    public static final String TRANSPORT_ACK = "transport.kafka.acks"; // 100
    public static final String BUFFER_MEMORY = "buffer.memory";
    public static final String TRANSPORT_BUFFER_MEMORY = "transport.kafka.buffer.memory"; // 33554432
    public static final String TIMEOUT_MS = "timeout.ms";
    public static final String TRANSPORT_TIMEOUT_MS = "transport.kafka.timeout.ms"; // 500
    public static final String BATCH_SIZE = "batch.size";
    public static final String TRANSPORT_BATCH_SIZE = "transport.kafka.batch.size"; // 16384
    public static final String RETRIES = "retries";
    public static final String TRANSPORT_RETRIES = "transport.kafka.retries"; // 0
    public static final String BOOTSTRAP_SERVERS = "bootstrap.servers";

    @Bean(name = "KafkaProducerProperties")
    public Properties kafkaProducerProperties(Environment environment) {
        String kafkaServer = environment.getProperty(environment.getActiveProfiles()[0] + KAFKA);

        Properties properties = new Properties();
        properties.put(RETRIES, environment.getProperty(TRANSPORT_RETRIES));
        properties.put(BATCH_SIZE, environment.getProperty(TRANSPORT_BATCH_SIZE));
        properties.put(TIMEOUT_MS, environment.getProperty(TRANSPORT_TIMEOUT_MS));
        properties.put(BUFFER_MEMORY, environment.getProperty(TRANSPORT_BUFFER_MEMORY));
        properties.put(ACK, environment.getProperty(TRANSPORT_ACK));
        properties.put(MAX_BLOCK, environment.getProperty(TRANSPORT_MAX_BLOCK));
        properties.put(KEY_SERIALIZER, environment.getProperty(TRANSPORT_KAFKA_KEY_SERIALIZER));
        properties.put(VALUE_SERIALIZER, environment.getProperty(TRANSPORT_KAFKA_VALUE_SERIALIZER));
        properties.put(BOOTSTRAP_SERVERS, kafkaServer);

        return properties;
    }

    @Bean
    public ProxyKafkaProducer proxyKafkaProducer(Environment environment) {
        Properties properties = kafkaProducerProperties(environment);
        ProxyKafkaProducer proxyKafkaProducer = new ProxyKafkaProducer(properties);
        return proxyKafkaProducer;
    }

    @Bean
    public EventService eventService(Environment environment) {
        return new EventService(proxyKafkaProducer(environment));
    }

}
