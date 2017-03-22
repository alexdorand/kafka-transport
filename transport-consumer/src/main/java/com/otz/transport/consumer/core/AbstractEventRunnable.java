package com.otz.transport.consumer.core;

import com.otz.transport.common.ContentSerializers;
import com.otz.transport.common.Envelope;
import com.otz.transport.common.serializer.JsonEnvelopSerializer;
import com.otz.transport.consumer.dispatcher.AbstractEventDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public abstract class AbstractEventRunnable implements Runnable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    Properties props;
    int interval;

    public void wakeUp() {
        getConsumer().wakeup();
    }

    protected abstract AbstractEventDispatcher getDispatcher();

    protected abstract KafkaConsumer getConsumer();


    @Override
    public void run() {

        try {

            while (!getDispatcher().getClosed().get()) {
                //noinspection unchecked
                ConsumerRecords<String, String> records = getConsumer().poll(interval);

                // records might be empty
                for (ConsumerRecord<String, String> record : records) {

                    String message[] = record.value().split("\\|\\|");
                    Envelope envelope = (Envelope) ContentSerializers.getSerializer(message[0]).deserialize(message[1]);

                    getDispatcher().dispatch(envelope);
                }

                if (!records.isEmpty()) getConsumer().commitAsync();
            }
        } catch (WakeupException e) {
            // Ignore exception if closing
            if (!getDispatcher().getClosed().get()) throw e;
        } finally {
            logger.info("kafka consumer closed.");
        }
    }

}
