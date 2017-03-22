package com.otz.plugin.transport.funnel.domain;

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
 * Created by alexdorand on 2016-12-08.
 */
public class FunnelIngredientDefinition  {

    private String stageDefinitionId;
    private String stageDefinitionName;

    public FunnelIngredientDefinition() {
    }

    public FunnelIngredientDefinition(String stageDefinitionId, String stageDefinitionName) {
        this.stageDefinitionId = stageDefinitionId;
        this.stageDefinitionName = stageDefinitionName;
    }

    public String getStageDefinitionId() {
        return stageDefinitionId;
    }

    public void setStageDefinitionId(String stageDefinitionId) {
        this.stageDefinitionId = stageDefinitionId;
    }

    public String getStageDefinitionName() {
        return stageDefinitionName;
    }

    public void setStageDefinitionName(String stageDefinitionName) {
        this.stageDefinitionName = stageDefinitionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FunnelIngredientDefinition)) return false;

        FunnelIngredientDefinition that = (FunnelIngredientDefinition) o;

        if (!getStageDefinitionId().equals(that.getStageDefinitionId())) return false;
        return getStageDefinitionName().equals(that.getStageDefinitionName());

    }

    @Override
    public int hashCode() {
        int result = getStageDefinitionId().hashCode();
        result = 31 * result + getStageDefinitionName().hashCode();
        return result;
    }
}
