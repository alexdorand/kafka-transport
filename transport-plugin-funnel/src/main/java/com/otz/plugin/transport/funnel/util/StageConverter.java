package com.otz.plugin.transport.funnel.util;

import com.otz.plugin.transport.funnel.domain.Stage;
import com.otz.plugin.transport.funnel.domain.StageDefinition;
import com.otz.plugin.transport.funnel.domain.StageStatus;

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
public class StageConverter {

    public static Stage convert(StageDefinition stageDefinition) {

        Stage stage = new Stage();
        stage.setStageStatus(StageStatus.notStarted);
        stage.setId(stageDefinition.getId());
        stage.setParentId(stageDefinition.getParentDefinition());
        stage.setTimestamp(System.currentTimeMillis());
        stage.setIcon(null);
        stage.setName(stageDefinition.getName());
        stage.setStageDefinitionName(stageDefinition.getName());
        stage.setTriggers(stageDefinition.getEventTriggers());

        return stage;

    }

}
