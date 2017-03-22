package com.otz.transport.common.content;

import com.otz.transport.common.*;
import com.otz.transport.common.serializer.JsonEnvelopSerializer;
import com.otz.transport.common.serializer.JsonHiptchatContentSerializer;

import java.util.HashMap;
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
 * Created by Alex Dorandish on 2016-11-25.
 */
public class HipchatEventContent extends EventContent {

    private String room;
    private List<String> users;
    private String templateName;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }


    public interface HaipChatRoomInterface {
        UsersInterface room(String room);
    }

    public interface UsersInterface {
        MessageTemplateInterface users(List<String> users);
    }

    public interface MessageTemplateInterface {
        LocaleInterface template(String templateName);
    }

    public interface LocaleInterface {
        HipchatInterface locale(Locale locale);
    }

    public interface HipchatInterface {

        HipchatInterface value(String key, String value);

        HipchatInterface valueMap(Map<String, String> values);

        HipchatEventContent content();

        Envelope envelop();

    }

    public static HaipChatRoomInterface create(String correlationId) {

        HipchatEventContent eventContent = new HipchatEventContent();

        return room -> users -> templateName -> locale -> new HipchatInterface() {

            @Override
            public HipchatInterface value(String key, String value) {
                eventContent.getValues().put(key, value);
                return this;
            }

            @Override
            public HipchatInterface valueMap(Map<String, String> values) {
                for (String key : values.keySet()) {
                    value(key, values.get(key));
                }
                return this;
            }

            @Override
            public HipchatEventContent content() {
                eventContent.setTimestamp(System.currentTimeMillis());
                eventContent.setRoom(room);
                eventContent.setTemplateName(templateName);
                eventContent.setUsers(users);
                eventContent.setCorrelationId(correlationId);
                eventContent.setTopic(Topic.HIPCHAT);


                return eventContent;
            }

            @Override
            public Envelope envelop() {
                ContentSerializer contentSerializer = JsonHiptchatContentSerializer.create();
                ContentSerializer envelopSerializer = JsonEnvelopSerializer.create();
                Envelope envelope = new Envelope(contentSerializer, envelopSerializer);
                envelope.setContent(contentSerializer.serialize(content()));
                envelope.setEventHeader(new EventHeader());
                envelope.getEventHeader().setTopic(Topic.HIPCHAT);
                envelope.getEventHeader().setParameters(new HashMap<>());
                envelope.getEventHeader().setClassName(getClass().getName().split("\\$")[0]);
                return envelope;
            }
        };

    }

}
