package com.otz.plugin.transport.funnel.domain;

import java.io.Serializable;
import java.util.List;

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
public class StreamList implements Serializable {

    private List<Stream> streams;
    private String name;
    private String attachedId;

    public List<Stream> getStreams() {
        return streams;
    }

    public void setStreams(List<Stream> streams) {
        this.streams = streams;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAttachedId() {
        return attachedId;
    }

    public void setAttachedId(String attachedId) {
        this.attachedId = attachedId;
    }
}
