package com.otz.transport.consumer.dispatcher;

import com.otz.transport.common.Envelope;
import com.otz.transport.common.config.Plugins;
import com.otz.transport.consumer.core.SmsEventRunnable;
import com.otz.transport.consumer.executor.AbstractEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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
@Service("SmsEventDispatcher")
public class SmsEventDispatcher extends AbstractEventDispatcher {

    private final Logger logger = LoggerFactory.getLogger(SmsEventDispatcher.class);

    @Autowired
    @Qualifier("SmsEventExecutor")
    private AbstractEventExecutor executor;

    @Autowired
    @Qualifier("KafkaProperties")
    private Properties kafkaProperties;

    @Autowired
    public Plugins loadedPlugins;

    @Autowired
    private Environment environment;

    private ExecutorService executorService;

    public static final String SMS = "sms";

    private final List<SmsEventRunnable> listeners = new ArrayList<>();
    private final AtomicBoolean closed = new AtomicBoolean(false);


    @PostConstruct
    public void init() {
        if (loadedPlugins.getNames().contains(SMS)) {
            String numberOfThreads = environment.getProperty(environment.getActiveProfiles()[0] + SERVICES + SMS);

            int dispatchersCount = Integer.parseInt(numberOfThreads);

            executorService = Executors.newFixedThreadPool(dispatchersCount);

            //subscriptions.
            for (int i = 0; i < dispatchersCount; i++) {
                SmsEventRunnable smsEventRunnable = new SmsEventRunnable(this, kafkaProperties, 200);
                listeners.add(smsEventRunnable);
                executorService.submit(smsEventRunnable);
            }

        }
    }


    @Override
    public void shutdown() throws Exception {

        logger.info("kafka event dispatcher shutting down.");
        closed.set(true);
        listeners.forEach(SmsEventRunnable::wakeUp);

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

    }


    @Override
    public AtomicBoolean getClosed() {
        return closed;
    }

    @Override
    public void dispatch(Envelope envelope) {
        executor.readAndExecute(envelope);
    }

    @Override
    public AbstractEventExecutor getExecutor() {
        return executor;
    }
}
