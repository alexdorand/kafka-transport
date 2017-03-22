package com.otz.plugin.transport.funnel;

import com.otz.plugin.transport.funnel.domain.*;
import com.otz.transport.common.EventImportance;
import com.otz.transport.common.content.ApplicationEventContent;
import com.otz.transport.producer.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
@Service
public class FunnelService {

    @Autowired
    private FunnelRepository funnelRepository;

    @Autowired
    private EventService eventService;


    public StreamDefinitionNames getStreamDefinitionNames() {
        return funnelRepository.getStreamDefinitionNames().toBlocking().single();
    }

    public StreamDefinitions create(StreamDefinitions streamDefinitions) {
        return funnelRepository.createStreamDefinitions(streamDefinitions).toBlocking().single();
    }

    public StreamDefinitions getDefinitionsRaw(String name) {
        return funnelRepository.getStreamDefinitions(name).toBlocking().single();
    }

    public Stage updateStage(String id, String name, String stageId, StageStatus stageStatus) {

        try {
            return funnelRepository.getStreams(id, name)
                    .flatMap(streamList ->

                            Observable.from(streamList.getStreams())
                                    .flatMap(stream -> Observable.from(stream.getStages()))
                                    .filter(stage -> stage.getId().equalsIgnoreCase(stageId))
                                    .first()
                                    .onErrorResumeNext(Observable.error(new RuntimeException()))
                                    .flatMap(stage -> {
                                        if (stage.getId().equalsIgnoreCase(stageId)) {
                                            stage.setStageStatus(stageStatus);
                                        }
                                        return Observable.just(stage);
                                    })
                                    .flatMap(stageUpdated -> funnelRepository.updateStreamList(streamList)
                                            .flatMap(streamListUpdated -> Observable.just(stageUpdated)))

                    )
                    .toBlocking().last();
        } catch (Exception e) {
            return null;
        }

    }


    public Observable<Stage> updateStageAsync(String id, String name, String stageId, StageStatus stageStatus) {

        return funnelRepository.getStreams(id, name)
                .flatMap(streamList ->

                        Observable.from(streamList.getStreams())
                                .flatMap(stream -> Observable.from(stream.getStages()))
                                .filter(stage -> stage.getId().equalsIgnoreCase(stageId))
                                .first()
                                .onErrorResumeNext(Observable.error(new RuntimeException()))
                                .flatMap(stage -> {
                                    if (stage.getId().equalsIgnoreCase(stageId)) {
                                        stage.setStageStatus(stageStatus);
                                    }
                                    return Observable.just(stage);
                                })
                                .flatMap(stageUpdated -> funnelRepository.updateStreamList(streamList)
                                        .flatMap(streamListUpdated -> Observable.just(stageUpdated)))

                );

    }

    public StreamList createStreamsForComponent(String id, String definitionsName) {

        return funnelRepository.generateStreamFor(id, definitionsName)
                .toBlocking().single();

    }


    public StreamList getStreamsFor(String componentId, String streamName) {
        return funnelRepository.getStreams(componentId, streamName)
                .toBlocking().single();
    }

    public StreamDefinitionNames getDefinitionsRegisteredForComponent(String componentId) {
        return funnelRepository.getDefinitionsRegisteredForComponent(componentId).toBlocking().single();
    }

    public void trigger(String attachedId, String definitionName, String stageId, StageStatus stageStatus, Map<String, String> values, Locale locale) {
        try {
            funnelRepository.getStreams(attachedId, definitionName)
                    .flatMap(streamList -> Observable.from(streamList.getStreams()))
                    .onErrorResumeNext(Observable.error(new RuntimeException()))
                    .flatMap(stream -> Observable.from(stream.getStages()))
                    .filter(stage -> stage.getId().equals(stageId))
                    .onErrorResumeNext(Observable.error(new RuntimeException()))
                    .first()
                    .flatMap(stageWithTriggers -> {
                        if (stageWithTriggers.getTriggers() != null) {
                            return Observable.from(stageWithTriggers.getTriggers())
                                    .flatMap(eventTrigger -> eventService.publishAsync(
                                            ApplicationEventContent
                                                    .create(null)
                                                    .eventType("trigger")
                                                    .level(EventImportance.l2)
                                                    .time(System.currentTimeMillis())
                                                    .locale(locale)
                                                    .valueMap(values)
                                                    .value("action", eventTrigger.getAction())
                                                    .value("stageStatus", stageStatus.name())
                                                    .value("id", attachedId)
                                                    .envelop()))
                                    .toList();
                        }
                        return Observable.just(null);
                    })
                    .toBlocking()
                    .last();
        } catch (Exception e) {
            // todo record an error
            System.out.println("Error occured on the trigger");
        }
    }

    public Observable<Stage> triggerAsync(String attachedId, String definitionName, String stageId, StageStatus stageStatus, Map<String, String> values, Locale locale) {
        return funnelRepository.getStreams(attachedId, definitionName)
                .flatMap(streamList -> Observable.from(streamList.getStreams()))
                .onErrorResumeNext(Observable.error(new RuntimeException()))
                .flatMap(stream -> Observable.from(stream.getStages()))
                .filter(stage -> stage.getId().equals(stageId))
                .onErrorResumeNext(Observable.error(new RuntimeException()))
                .first()
                .flatMap(stageWithTriggers -> {
                    if (stageWithTriggers.getTriggers() != null) {
                        return Observable.from(stageWithTriggers.getTriggers())
                                .flatMap(eventTrigger ->
                                        eventService.publishAsync(
                                                ApplicationEventContent
                                                        .create(null)
                                                        .eventType("trigger")
                                                        .level(EventImportance.l2)
                                                        .time(System.currentTimeMillis())
                                                        .locale(locale)
                                                        .valueMap(values)
                                                        .value("action", eventTrigger.getAction())
                                                        .value("stageStatus", stageStatus.name())
                                                        .value("id", attachedId)
                                                        .envelop()))
                                .toList()
                                .flatMap(envelopes -> Observable.just(stageWithTriggers));
                    }
                    return Observable.just(null);
                });
    }

    public static void main(String[] args) {
        List<String> test = new ArrayList<>();
        test.add("1");
        test.add("2");
        test.add("3");

        String result = Observable.from(test)
                .filter(s -> s.equals("4"))
                .first()
                .onErrorResumeNext(Observable.error(new RuntimeException()))
                .flatMap(s -> {
                    System.out.println(s);
                    return Observable.just(s);
                })
                .toBlocking()
                .single();

        System.out.println(result);
    }
}
