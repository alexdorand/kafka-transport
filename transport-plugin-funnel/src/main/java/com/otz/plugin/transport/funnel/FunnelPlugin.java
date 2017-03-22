package com.otz.plugin.transport.funnel;

import com.otz.plugin.transport.funnel.domain.StageStatus;
import com.otz.transport.common.EventContent;
import com.otz.transport.common.EventHeader;
import com.otz.transport.common.ExecutorPlugin;
import com.otz.transport.common.Topic;
import com.otz.transport.common.content.FunnelEventContent;
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
@Service("FunnelPlugin")
public class FunnelPlugin implements ExecutorPlugin {

    @Autowired
    private FunnelService funnelService;


    @Override
    public Topic topic() {
        return Topic.FUNNEL;
    }

    @Override
    public void execute(EventHeader eventHeader, EventContent eventContent) {

        FunnelEventContent funnelEventContent = (FunnelEventContent) eventContent;

        switch (funnelEventContent.getAction()) {
            case "update":
//                funnelService.updateStageAsync(funnelEventContent.getAttachedId(), funnelEventContent.getDefinitionName(), funnelEventContent.getStageId(), StageStatus.valueOf(funnelEventContent.getStatus()))
//                        .flatMap(stage -> funnelService.triggerAsync(funnelEventContent.getAttachedId(), funnelEventContent.getDefinitionName(), funnelEventContent.getStageId(), StageStatus.valueOf(funnelEventContent.getStatus()), funnelEventContent.getValues(), funnelEventContent.getLocale()))
//                        .toBlocking().last();
                funnelService.updateStage(funnelEventContent.getAttachedId(), funnelEventContent.getDefinitionName(), funnelEventContent.getStageId(), StageStatus.valueOf(funnelEventContent.getStatus()));
                funnelService.trigger(funnelEventContent.getAttachedId(), funnelEventContent.getDefinitionName(), funnelEventContent.getStageId(), StageStatus.valueOf(funnelEventContent.getStatus()), funnelEventContent.getValues(), funnelEventContent.getLocale());
                break;
            case "attach":
                funnelService.createStreamsForComponent(funnelEventContent.getAttachedId(), funnelEventContent.getDefinitionName());
                break;
        }

        System.out.println("Sending funnel update notification <<<<------");
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void init() {

    }
}
