package com.otz.transport.common;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import javax.annotation.PreDestroy;
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
 * Created by Alex Dorandish on 2016-11-25.
 */
public class ProxyKafkaProducer {

    Logger logger = LoggerFactory.getLogger(ProxyKafkaProducer.class);

    /**
     * @link http://people.apache.org/~joestein/kafka-0.7.1-incubating-docs/kafka/producer/Producer.html
     */
    private final Producer<String, String> producer;


    public ProxyKafkaProducer(Properties properties) {
        producer = new KafkaProducer<>(properties);
    }

    //private final Map<String, ContentSerializer> serializers = new HashMap<>();


    @PreDestroy
    public void close() {
        producer.close();
    }


    public Observable<Envelope> publish(Envelope envelope) {

        if (!ContentSerializers.contains(envelope.getContentSerializer())) {
            throw new TransportException("Serializer " + envelope.getContentSerializer() + " not registered");
        }

        return Observable.just(envelope)
                .flatMap(envelopeToBePublished -> {

                    //noinspection unchecked
                    String msg = ContentSerializers.getSerializer(envelope.getEnvelopSerializer()).serialize(envelopeToBePublished);
                    String message = com.otz.transport.common.serializer.JsonEnvelopSerializer.class.getName() + Envelope.MESSAGE_SEPARATOR + msg;
                    try {
                        System.out.println("publishing to : " + envelope.getEventHeader().getTopic().getTopicName());
                        //noinspection unchecked
                        ProducerRecord producerRecord = new ProducerRecord(envelope.getEventHeader().getTopic().getTopicName(), message);
                        //noinspection unchecked
                        producer.send(producerRecord);
                    } catch (Exception e) {
                        e.printStackTrace();
                        producer.flush();
                    }

                    return Observable.just(envelope);
                });
    }


    public void publishNonAsync(Envelope envelope) {

        String msg = ContentSerializers.getSerializer(envelope.getEnvelopSerializer()).serialize(envelope);
        String message = envelope.getContentSerializer() + Envelope.MESSAGE_SEPARATOR + msg;
        try {
            System.out.println("publishing to : " + envelope.getEventHeader().getTopic().getTopicName());
            //noinspection unchecked
            ProducerRecord producerRecord = new ProducerRecord(envelope.getEventHeader().getTopic().getTopicName(), message);
            //noinspection unchecked
            producer.send(producerRecord);
        } catch (Exception e) {
            e.printStackTrace();
            producer.flush();
        }
    }


    public static void main(String[] args) throws Exception {


        //Assign topicName to string variable
        String topicName = "transport-application-event";

        // create instance for properties to access producer configs
        Properties props = new Properties();

        //Assign localhost id
        props.put("bootstrap.servers", "localhost:9092");
        props.put("advertised.host.name", "localhost:9092");

        //Set acknowledgements for producer requests.
        props.put("acks", "all");

        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);

        //Specify buffer size in config
        props.put("batch.size", 16384);

        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);

        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        props.put("request.required.acks", "1");


        Producer<String, String> producer = new KafkaProducer<>(props);

        for (int i = 0; i < 10; i++)
            producer.send(new ProducerRecord<String, String>(topicName, Integer.toString(i)));
        System.out.println("Message sent successfully");
        producer.close();
    }


}
