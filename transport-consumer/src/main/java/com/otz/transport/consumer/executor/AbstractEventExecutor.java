package com.otz.transport.consumer.executor;

import com.otz.transport.common.*;

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
@SuppressWarnings("EmptyMethod")
public abstract class AbstractEventExecutor<EC extends EventContent> {

    public void readAndExecute(Envelope envelope) {

        //noinspection unchecked
        EC eventContent = (EC) ContentSerializers.getSerializer(envelope.getContentSerializer()).deserialize(envelope.getContent());
        process(envelope.getEventHeader(), eventContent);
    }

    protected abstract void process(EventHeader eventHeader, EC eventContent);

    public abstract void shutdown();

    public abstract void init();

}