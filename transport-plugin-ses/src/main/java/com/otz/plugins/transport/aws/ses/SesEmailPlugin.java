package com.otz.plugins.transport.aws.ses;

import com.otz.transport.common.EventContent;
import com.otz.transport.common.EventHeader;
import com.otz.transport.common.ExecutorPlugin;
import com.otz.transport.common.Topic;
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
 * Created by alexdorand on 2016-11-30.
 */
@Service("EmailPlugin")
public class SesEmailPlugin implements ExecutorPlugin {

    @Autowired
    private SpringEmailSender springEmailSender;

    @Override
    public Topic topic() {
        return Topic.EMAIL;
    }

    @Override
    public void execute(EventHeader eventHeader, EventContent eventContent) {

        springEmailSender.send(eventHeader, eventContent);
    }


    @Override
    public void shutdown() {

    }

    @Override
    public void init() {

    }
}
