package com.otz.transport.common.content;

import com.otz.transport.common.*;
import com.otz.transport.common.serializer.JsonApplicationEventContentSerializer;
import com.otz.transport.common.serializer.JsonEnvelopSerializer;

import java.util.HashMap;
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
 * Created by Alex Dorandish on 2016-11-25.
 */
public class ApplicationEventContent extends EventContent {

    private String applicationEventType;

    public String getApplicationEventType() {
        return applicationEventType;
    }

    public void setApplicationEventType(String applicationEventType) {
        this.applicationEventType = applicationEventType;
    }


    public interface EventTypeInterface {
        ImportanceInterface eventType(String eventType);
    }

    public interface TimestampInterface {
        LocaleInterface time(long timestamp);
    }

    public interface ImportanceInterface {
        TimestampInterface level(EventImportance eventImportance);
    }

    public interface LocaleInterface {
        ApplicationValuesInterface locale(Locale locale);
    }

    public interface ApplicationValuesInterface {

        ApplicationValuesInterface value(String key, String value);

        ApplicationValuesInterface valueMap(Map<String, String> values);

        ApplicationEventContent content();

        Envelope envelop();

    }


    public static EventTypeInterface create(String correlationId) {
        ApplicationEventContent eventContent = new ApplicationEventContent();

        return eventType -> eventImportance -> timestamp -> locale -> new ApplicationValuesInterface() {
            @Override
            public ApplicationValuesInterface value(String key, String value) {
                eventContent.getValues().put(key, value);
                return this;
            }

            @Override
            public ApplicationValuesInterface valueMap(Map<String, String> values) {
                for (String key : values.keySet()) {
                    value(key, values.get(key));
                }
                return this;
            }

            @Override
            public ApplicationEventContent content() {
                eventContent.setTimestamp(timestamp);
                eventContent.setTopic(Topic.APPLICATION_EVENT);
                eventContent.setApplicationEventType(eventType);
                eventContent.setCorrelationId(correlationId);
                eventContent.setEventImportance(eventImportance);
                return eventContent;
            }

            @Override
            public Envelope envelop() {
                ContentSerializer contentSerializer = JsonApplicationEventContentSerializer.create();
                ContentSerializer envelopSerializer = JsonEnvelopSerializer.create();

                Envelope envelope = new Envelope(contentSerializer, envelopSerializer);
                envelope.setContent(contentSerializer.serialize(content()));
                envelope.setEventHeader(new EventHeader());
                envelope.getEventHeader().setTopic(Topic.APPLICATION_EVENT);
                envelope.getEventHeader().setParameters(new HashMap<>());
                envelope.getEventHeader().setClassName(getClass().getName().split("\\$")[0]);
                return envelope;
            }
        };

    }

}
