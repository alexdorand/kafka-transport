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
public class Stage implements Serializable {

    private String id;
    private String parentId;
    private String icon;

    private StageStatus stageStatus;
    private long timestamp;
    private String name;
    private String stageDefinitionName;
    private List<EventTrigger> triggers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public StageStatus getStageStatus() {
        return stageStatus;
    }

    public void setStageStatus(StageStatus stageStatus) {
        this.stageStatus = stageStatus;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStageDefinitionName() {
        return stageDefinitionName;
    }

    public void setStageDefinitionName(String stageDefinitionName) {
        this.stageDefinitionName = stageDefinitionName;
    }

    public void setTriggers(List<EventTrigger> triggers) {
        this.triggers = triggers;
    }

    public List<EventTrigger> getTriggers() {
        return triggers;
    }
}
