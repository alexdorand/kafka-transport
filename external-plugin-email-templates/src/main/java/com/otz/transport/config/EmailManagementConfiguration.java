package com.otz.transport.config;

import com.google.gson.Gson;
import com.otz.transport.common.config.ContentUtil;
import com.sample.application.email.domain.EmailTemplates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

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
@ComponentScan(basePackages = {"com.sample.application.email"})
public class EmailManagementConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    private Gson gson = new Gson();

    @Bean
    public EmailTemplates emailTemplates() throws IOException {
        String emailTemplatesString  = ContentUtil.getStringContent(applicationContext, "emails.json");
        return gson.fromJson(emailTemplatesString, EmailTemplates.class);
    }


}
