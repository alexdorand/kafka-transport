package com.sample.application.plugin;

import com.otz.transport.common.content.EmailEventContent;
import com.otz.transport.producer.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

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
 * Created by alexdorand on 2016-12-02.
 */
@Service
public class ApplicationTriggersService {

    @Autowired
    private EventService eventService;

    public void executeTrigger(String name, Map<String, String> parameters) {
        System.out.println("Executing trigger: " + parameters.get("action"));
        if (parameters.get("action").equals("projectCreated")) {
            eventService.publishAsync(
                    EmailEventContent
                            .create(null)
                            .emailType("projectCreated")
                            .locale(Locale.ENGLISH)
                            .value("test", "test")
                            .value("to", "alexdorand@gmail.com")
                            .envelop()

            ).toBlocking().last();
        }

    }

}
