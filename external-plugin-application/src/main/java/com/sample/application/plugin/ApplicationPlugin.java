package com.sample.application.plugin;

import com.otz.transport.common.EventContent;
import com.otz.transport.common.EventHeader;
import com.otz.transport.common.ExecutorPlugin;
import com.otz.transport.common.Topic;
import com.otz.transport.common.content.ApplicationEventContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 * Created by alexdorand on 2016-12-01.
 */
@Service("ApplicationPlugin")
public class ApplicationPlugin implements ExecutorPlugin {

    public static final String TRIGGER = "trigger";

    public ApplicationPlugin() {
        System.out.println("App plugin done!");
    }

    @Autowired
    private ApplicationFunnelService applicationFunnelService;

    @Autowired
    private ApplicationTriggersService applicationTriggersService;

    @Override
    public Topic topic() {
        return Topic.APPLICATION_EVENT;
    }

    @Override
    public void execute(EventHeader eventHeader, EventContent eventContent) {
        ApplicationEventContent applicationEventContent = ((ApplicationEventContent) eventContent);
        if(applicationEventContent.getApplicationEventType().equals(TRIGGER)) {
            applicationTriggersService.executeTrigger(applicationEventContent.getApplicationEventType(), applicationEventContent.getValues());
        } else {
            applicationFunnelService.executeFunnel(applicationEventContent.getApplicationEventType(), applicationEventContent.getValues());
        }

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void init() {
        System.out.println("Started");
    }
}
