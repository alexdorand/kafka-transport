package com.otz.transport.config;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

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
@Configuration
@ComponentScan(basePackages = {"com.otz.plugin.transport.funnel"})
public class FunnelPluginConfiguration {

    @Bean
    public Cluster couchbaseCluster(Environment environment) {
        String clustersString = environment.getProperty(environment.getActiveProfiles()[0] + ".couchbase");
        String clusters[] = clustersString.split(",");
        return CouchbaseCluster.create(clusters);
    }

    public FunnelPluginConfiguration() {
        System.out.println("------------------------------------------");
        System.out.println("Funnel Plugin Configuration Loaded...");
        System.out.println("------------------------------------------");
    }

}
