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
 * Created by alexdorand on 2016-12-05.
 */
public class StageDefinition implements Serializable {

    private String id;
    private String name;
    private String parentDefinition;
    private String icon;
    private List<EventTrigger> eventTriggers;

    public StageDefinition() {
    }

    public StageDefinition(String id, String name, String parentDefinition) {
        this.id = id;
        this.name = name;
        this.parentDefinition = parentDefinition;
    }

    public StageDefinition(String id, String name, String parentDefinition, String icon) {
        this.id = id;
        this.name = name;
        this.parentDefinition = parentDefinition;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentDefinition() {
        return parentDefinition;
    }

    public void setParentDefinition(String parentDefinition) {
        this.parentDefinition = parentDefinition;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<EventTrigger> getEventTriggers() {
        return eventTriggers;
    }

    public void setEventTriggers(List<EventTrigger> eventTriggers) {
        this.eventTriggers = eventTriggers;
    }
}
