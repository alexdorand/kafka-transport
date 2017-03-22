package com.otz.plugin.transport.funnel.util;

import com.otz.couchbase.client.KeyGenerator;
import com.otz.plugin.transport.funnel.domain.*;

import java.util.ArrayList;

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
 * Created by alexdorand on 2016-12-07.
 */
public class StreamConverter {

    public static StreamList convert(StreamDefinitions streamDefinitions, String attachedId) {
        StreamList streamList = new StreamList();
        streamList.setStreams(new ArrayList<>());
        streamList.setName(streamDefinitions.getName());
        streamList.setAttachedId(attachedId);

        for(StreamDefinition streamDefinition: streamDefinitions.getDefinitions()) {
            streamList.getStreams().add(convert(streamDefinition));
        }

        return streamList;
    }

    public static Stream convert(StreamDefinition streamDefinition) {

        Stream stream = new Stream();
        stream.setDefinitionId(streamDefinition.getId());
        stream.setId(streamDefinition.getId());
        stream.setStages(new ArrayList<>());

        for(StageDefinition stageDefinition: streamDefinition.getStageDefinitions()) {
            stream.getStages().add(StageConverter.convert(stageDefinition));
        }

        return stream;
    }

}
