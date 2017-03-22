package com.otz.transport.consumer.core;

import com.otz.transport.common.Topic;
import com.otz.transport.common.content.ErrorEventContent;
import com.otz.transport.consumer.dispatcher.AbstractEventDispatcher;
import com.otz.transport.consumer.dispatcher.ErrorEventDispatcher;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
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
public class ErrorEventRunnable extends AbstractEventRunnable {

    private final ErrorEventDispatcher dispatcher;
    private final KafkaConsumer consumer;


    public ErrorEventRunnable(ErrorEventDispatcher dispatcher, Properties props, int interval) {

        this.dispatcher = dispatcher;
        this.props = props;
        this.interval = interval;

        consumer = new KafkaConsumer(props);

        consumer.subscribe(Collections.singletonList(Topic.ERROR.getTopicName()));

        getDispatcher().dispatch(
                ErrorEventContent
                        .create(null)
                        .errorType("testError")
                        .message("something went wrong")
                        .value("test", "test")
                        .envelop());

    }


    @Override
    public AbstractEventDispatcher getDispatcher() {
        return dispatcher;
    }

    @Override
    public KafkaConsumer getConsumer() {
        return consumer;
    }
}
