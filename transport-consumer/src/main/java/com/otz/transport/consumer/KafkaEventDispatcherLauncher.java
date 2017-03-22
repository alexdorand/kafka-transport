package com.otz.transport.consumer;

import com.otz.transport.common.config.Plugins;
import com.otz.transport.consumer.dispatcher.AbstractEventDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PreDestroy;

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
@SuppressWarnings("SpringFacetCodeInspection")
@EnableAsync
@SpringBootApplication
@ComponentScan(basePackages = {"com.otz.transport"})
public class KafkaEventDispatcherLauncher implements CommandLineRunner {

//    @Autowired
//    private DiscoveryClient discovery;
//
//    @Autowired
//    private LoadBalancerClient loadBalancer;



    @Autowired
    public Plugins configMap;

    @Autowired
    Environment environment;



    @Override
    public void run(String... args) throws Exception {

        System.out.println("   __                                                            __        \n" +
                "  / /_   _____  ____ _   ____    _____    ____   ____    _____  / /_       \n" +
                " / __/  / ___/ / __ `/  / __ \\  / ___/   / __ \\ / __ \\  / ___/ / __/       \n" +
                "/ /_   / /    / /_/ /  / / / / (__  )   / /_/ // /_/ / / /    / /_         \n" +
                "\\__/  /_/     \\__,_/  /_/ /_/ /____/   / .___/ \\____/ /_/     \\__/         \n" +
                "                                      /_/                                  ");
        System.out.println("-----------------------------------------------------------------");

    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(KafkaEventDispatcherLauncher.class, args);
    }

    @PreDestroy
    public void destroy() throws Exception {
        //applicationEventDispatcher.shutdown();
    }

}
