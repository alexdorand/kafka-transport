package com.otz.transport.consumer.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.otz.transport.common.ContentSerializers;
import com.otz.transport.common.ProxyKafkaProducer;
import com.otz.transport.common.config.Plugins;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.text.SimpleDateFormat;
import java.util.List;
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
 * Created by Alex Dorandish on 2016-11-26.
 */
@SuppressWarnings({"WeakerAccess", "SpringFacetCodeInspection"})
@Configuration
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableWebMvc
public class TransportConfiguration extends WebMvcConfigurerAdapter {

    public static final String KAFKA = ".kafka";
    public static final String GROUP_ID = "group.id";
    public static final String TRANSPORT_KAFKA_GROUP_ID = "transport.kafka.group.id";
    public static final String ENABLE_AUTO_COMMIT = "enable.auto.commit";
    public static final String TRANSPORT_KAFKA_ENABLE_AUTO_COMMIT = "transport.kafka.enable.auto.commit";
    public static final String AUTO_COMMIT_INTERVAL_MS = "auto.commit.interval.ms";
    public static final String TRANSPORT_KAFKA_AUTO_COMMIT_INTERVAL_MS = "transport.kafka.auto.commit.interval.ms";
    public static final String SESSION_TIMEOUT_MS = "session.timeout.ms";
    public static final String TRANSPORT_KAFKA_SESSION_TIMEOUT_MS = "transport.kafka.session.timeout.ms";

    public static final String KEY_DESERIALIZER = "key.deserializer";
    public static final String TRANSPORT_KAFKA_KEY_DESERIALIZER = "transport.kafka.key.deserializer";
    public static final String VALUE_DESERIALIZER = "value.deserializer";
    public static final String TRANSPORT_KAFKA_VALUE_DESERIALIZER = "transport.kafka.value.deserializer";


    public static final String KEY_SERIALIZER = "key.serializer";
    public static final String TRANSPORT_KAFKA_KEY_SERIALIZER = "transport.kafka.key.serializer";
    public static final String VALUE_SERIALIZER = "value.serializer";
    public static final String TRANSPORT_KAFKA_VALUE_SERIALIZER = "transport.kafka.value.serializer";


    public static final String BOOTSTRAP_SERVERS = "bootstrap.servers";



    @Autowired
    private ApplicationContext applicationContext;

    @Value("${transport.load}")
    private String apps;

    @Value("${transport.serializers}")
    private String serializers;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        //builder.propertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        builder.serializationInclusion(JsonInclude.Include.NON_EMPTY);
        builder.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));

        super.configureMessageConverters(converters);
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        return builder;
    }

    @Bean
    public Plugins getApplicationThreads(Environment environment) {

        String slzs[] = serializers.split(",");
        for (String serializer : slzs) {
            ContentSerializers.registerSerializers(serializer.trim());

        }

        Plugins plugins = new Plugins();
        for (String appName : apps.split(",")) {
            plugins.getNames().add(appName);
        }
        return plugins;
    }

    @Bean(name = "KafkaProperties")
    public Properties kafkaProperties(Environment environment) {
        String kafkaServer = environment.getProperty(environment.getActiveProfiles()[0] + KAFKA);

        Properties properties = new Properties();
        properties.put(GROUP_ID, environment.getProperty(TRANSPORT_KAFKA_GROUP_ID));
        properties.put(ENABLE_AUTO_COMMIT, environment.getProperty(TRANSPORT_KAFKA_ENABLE_AUTO_COMMIT));
        properties.put(AUTO_COMMIT_INTERVAL_MS, environment.getProperty(TRANSPORT_KAFKA_AUTO_COMMIT_INTERVAL_MS));
        properties.put(SESSION_TIMEOUT_MS, environment.getProperty(TRANSPORT_KAFKA_SESSION_TIMEOUT_MS));
        properties.put(KEY_DESERIALIZER, environment.getProperty(TRANSPORT_KAFKA_KEY_DESERIALIZER));
        properties.put(VALUE_DESERIALIZER, environment.getProperty(TRANSPORT_KAFKA_VALUE_DESERIALIZER));

        properties.put(KEY_SERIALIZER, environment.getProperty(TRANSPORT_KAFKA_KEY_SERIALIZER));
        properties.put(VALUE_SERIALIZER, environment.getProperty(TRANSPORT_KAFKA_VALUE_SERIALIZER));

        properties.put(BOOTSTRAP_SERVERS, kafkaServer);

        return properties;
    }





    @Bean
    public ProxyKafkaProducer getProxyKafkaProducer(Environment environment) {
        ProxyKafkaProducer proxyKafkaProducer = new ProxyKafkaProducer(kafkaProperties(environment));
        return proxyKafkaProducer;
    }


}
