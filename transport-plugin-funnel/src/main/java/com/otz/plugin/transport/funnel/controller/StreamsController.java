package com.otz.plugin.transport.funnel.controller;

import com.otz.plugin.transport.funnel.FunnelService;
import com.otz.plugin.transport.funnel.domain.*;
import com.otz.plugin.transport.funnel.model.StageModel;
import com.otz.plugin.transport.funnel.model.StreamModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rx.Observable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
 * Created by alexdorand on 2016-12-06.
 */
@Controller
public class StreamsController {

    @Autowired
    private FunnelService funnelService;

    @CrossOrigin
    @RequestMapping(path = "/definitions")
    @ResponseBody
    public ResponseEntity<StreamDefinitionNames> getStreams() {

        return new ResponseEntity(funnelService.getStreamDefinitionNames(), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path = "/component/{id}/streams/{name}/stages/{stageId}/status/{status}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Stage> getStreams(@PathVariable(value = "id") String id,
                                            @PathVariable(value = "name") String name,
                                            @PathVariable(value = "stageId") String stageId,
                                            @PathVariable(value = "status") StageStatus status) {

        Stage updatedStage = funnelService.updateStageAsync(id, name, stageId, status)
                .flatMap(stage ->
                        funnelService.triggerAsync(id, name, stageId, status, new HashMap<>(), Locale.ENGLISH)
                                .flatMap(stage1 -> Observable.just(stage)))
                .toBlocking()
                .first();
        ;
        return new ResponseEntity(updatedStage, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path = "/definitions/{name}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<StreamDefinitions> createDefinitions(@PathVariable(value = "name") String name) {

        return new ResponseEntity(funnelService.getDefinitionsRaw(name), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path = "/components/{componentId}/definitions", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<StreamDefinitionNames> getDefinitionsRegisteredForComponent(@PathVariable(value = "componentId") String componentId) {

        return new ResponseEntity(funnelService.getDefinitionsRegisteredForComponent(componentId), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path = "/definitions", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<StreamDefinitions> createDefinitions(@RequestBody StreamDefinitions streamDefinitions) {

        return new ResponseEntity(funnelService.create(streamDefinitions), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(path = "/components/{componentId}/streams/{streamName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<StreamModel> getStreamsForComponent(@PathVariable(value = "componentId") String componentId,
                                                              @PathVariable(value = "streamName") String streamName) {

        return new ResponseEntity(convertFromStreamList(funnelService.getStreamsFor(componentId, streamName)), HttpStatus.OK);
    }


    @CrossOrigin
    @RequestMapping(path = "/components/{id}/definitions/{definitionsName}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<StreamModel> generateStreamsForComponent(@PathVariable(value = "id") String id,
                                                                   @PathVariable(value = "definitionsName") String definitionsName) {

        return new ResponseEntity(convertFromStreamList(funnelService.createStreamsForComponent(id, definitionsName)), HttpStatus.OK);
    }


    private StreamModel convertFromStreamList(StreamList streamList) {
        StreamModel streamModel = new StreamModel();
        streamModel.setModels(new ArrayList<>());

        for (Stream stream : streamList.getStreams()) {

            for (Stage stage : stream.getStages()) {
                StageModel stageModel = new StageModel(stage.getId(), stage.getName(), stage.getParentId(), stage.getIcon());
                stageModel.setSource("/images/" + stage.getStageStatus().name() + ".ico");
                stageModel.setTriggers(convertTriggers(stage.getTriggers()));
                streamModel.getModels().add(stageModel);

            }
        }
        return streamModel;
    }

    private String convertTriggers(List<EventTrigger> triggerObjects) {
        if (triggerObjects != null) {
            List<String> triggers = triggerObjects.stream().map(eventTrigger -> eventTrigger.getAction()).collect(Collectors.toList());
            return StringUtils.join(triggers, "\n");
        } else {
            return "";
        }
    }


}
