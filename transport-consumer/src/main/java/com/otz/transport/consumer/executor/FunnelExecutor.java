package com.otz.transport.consumer.executor;

import com.otz.transport.common.EventContent;
import com.otz.transport.common.EventHeader;
import com.otz.transport.common.ExecutorPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
 * Created by Alex Dorandish on 2016-11-26.
 */
@Service("FunnelExecutor")
public class FunnelExecutor extends AbstractEventExecutor {

    @Autowired
    @Qualifier("FunnelPlugin")
    private ExecutorPlugin applicationPlugin;


    @Override
    public void process(EventHeader eventHeader, EventContent eventContent) {

        applicationPlugin.execute(eventHeader, eventContent);
    }

    @Override
    public void shutdown() {
        applicationPlugin.shutdown();
    }

    @Override
    public void init() {
        applicationPlugin.init();
    }


}