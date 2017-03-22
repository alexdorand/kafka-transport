package com.otz.transport.common;

import com.otz.transport.common.content.ApplicationEventContent;
import com.otz.transport.common.content.EmailEventContent;
import com.otz.transport.common.serializer.JsonApplicationEventContentSerializer;
import com.otz.transport.common.serializer.JsonEmailContentSerializer;
import com.otz.transport.common.serializer.JsonFunnelContentSerializer;
import org.junit.Test;

import java.util.*;

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
 * Created by Alex Dorandish on 2016-11-25.
 */
public class ProxyKafkaProducerTest {

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


    @Test
    public void testPublish() throws Exception {


        Map<String, String> environment = new HashMap<>();
        environment.put("transport.kafka.acks", "-1");
        environment.put("transport.kafka.retries", "0");
        environment.put("transport.kafka.batch.size", "16384");

        environment.put("transport.kafka.max.block.ms", "10000");
        environment.put("transport.kafka.ack", "0");
        environment.put("transport.kafka.timeout.ms", "500");
        environment.put("transport.kafka.buffer.memory", "33554432");
        environment.put("transport.kafka.key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        environment.put("transport.kafka.value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Properties properties = new Properties();
        properties.put(RETRIES, environment.get(TRANSPORT_RETRIES));
        properties.put(BATCH_SIZE, environment.get(TRANSPORT_BATCH_SIZE));
        properties.put(TIMEOUT_MS, environment.get(TRANSPORT_TIMEOUT_MS));
        properties.put(BUFFER_MEMORY, environment.get(TRANSPORT_BUFFER_MEMORY));
        properties.put(ACK, environment.get(TRANSPORT_ACK));
        properties.put(MAX_BLOCK, environment.get(TRANSPORT_MAX_BLOCK));
        properties.put(KEY_SERIALIZER, environment.get(TRANSPORT_KAFKA_KEY_SERIALIZER));
        properties.put(VALUE_SERIALIZER, environment.get(TRANSPORT_KAFKA_VALUE_SERIALIZER));
        properties.put(BOOTSTRAP_SERVERS, "localhost:9092");

        Envelope envelope = ApplicationEventContent
                .create(UUID.randomUUID().toString())
                .eventType("trigger")
                .level(EventImportance.l1)
                .time(System.currentTimeMillis())
                .locale(Locale.ENGLISH)
                .valueMap(new HashMap<>())
                .value("action", "createApp")
                .value("stageStatus", "completed")
                .value("id", UUID.randomUUID().toString())
                .envelop();

        ProxyKafkaProducer proxyKafkaProducer = new ProxyKafkaProducer(properties);
        ContentSerializers.registerSerializers(com.otz.transport.common.serializer.JsonEnvelopSerializer.class.getName());
        ContentSerializers.registerSerializers(JsonEmailContentSerializer.class.getName());
        ContentSerializers.registerSerializers(JsonFunnelContentSerializer.class.getName());
        ContentSerializers.registerSerializers(JsonApplicationEventContentSerializer.class.getName());

        //ContentSerializer serializer = proxyKafkaProducer.getSerializers().get(envelope.getContentSerializer());

        //System.out.println(serializer);
        //String msg = ;

        proxyKafkaProducer.publishNonAsync(envelope);

    }



}