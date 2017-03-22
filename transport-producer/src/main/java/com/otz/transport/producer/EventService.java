package com.otz.transport.producer;

import com.otz.transport.common.Envelope;
import com.otz.transport.common.ProxyKafkaProducer;
import rx.Observable;

import java.util.concurrent.TimeUnit;

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
public class EventService {

    private ProxyKafkaProducer proxyKafkaProducer;

    public EventService(ProxyKafkaProducer proxyKafkaProducer) {
        this.proxyKafkaProducer = proxyKafkaProducer;
    }

    public Observable<Envelope> publishAsync(Envelope envelope) {
        return Observable.just(envelope)
                .flatMap(envelopeCreated -> proxyKafkaProducer.publish(envelope))
                .timeout(100000, TimeUnit.MILLISECONDS, Observable.just(envelope));

    }


}
