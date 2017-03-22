package com.otz.transport.common.content;

import com.otz.transport.common.*;
import com.otz.transport.common.serializer.JsonEnvelopSerializer;
import com.otz.transport.common.serializer.JsonLogEventContentSerializer;

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
public class PushNotificationContent extends EventContent {

    private String notificationType;
    private String intendedFor;
    private String deviceId;


    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getIntendedFor() {
        return intendedFor;
    }

    public void setIntendedFor(String intendedFor) {
        this.intendedFor = intendedFor;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public interface NotificationTypeInterface {
        ImportanceInterface type(String notificationType);
    }

    public interface ImportanceInterface {
        TimestampInterface level(EventImportance eventImportance);
    }

    public interface TimestampInterface {
        LocaleInterface time(long timestamp);
    }

    public interface LocaleInterface {
        IntendedForInterface locale(Locale locale);
    }

    public interface IntendedForInterface {
        DeviceInterface intendedFor(String intendedFor);
    }

    public interface DeviceInterface {
        ValuePushNotificationInterface deviceId(String deviceId);
    }

    public interface ValuePushNotificationInterface {

        ValuePushNotificationInterface value(String key, String value);

        ValuePushNotificationInterface valueMap(Map<String, String> values);

        PushNotificationContent content();

        Envelope envelop();

    }

    public static NotificationTypeInterface create(String correlationId) {

        PushNotificationContent eventContent = new PushNotificationContent();
        eventContent.setTopic(Topic.PUSH_NOTIFICATION);

        return notificationType -> eventImportance -> timestamp -> locale -> intendedFor -> deviceId -> new ValuePushNotificationInterface() {
            @Override
            public ValuePushNotificationInterface value(String key, String value) {
                eventContent.getValues().put(key, value);
                return this;
            }

            @Override
            public ValuePushNotificationInterface valueMap(Map<String, String> values) {
                for (String key : values.keySet()) {
                    value(key, values.get(key));
                }
                return this;
            }

            @Override
            public PushNotificationContent content() {
                eventContent.setTimestamp(timestamp);
                eventContent.setNotificationType(notificationType);
                eventContent.setIntendedFor(intendedFor);
                eventContent.setDeviceId(deviceId);
                eventContent.setEventImportance(eventImportance);
                eventContent.setCorrelationId(correlationId);

                return eventContent;
            }

            @Override
            public Envelope envelop() {
                ContentSerializer contentSerializer = JsonLogEventContentSerializer.create();
                ContentSerializer envelopSerializer = JsonEnvelopSerializer.create();
                Envelope envelope = new Envelope(contentSerializer, envelopSerializer);
                envelope.setContent(contentSerializer.serialize(content()));
                envelope.setEventHeader(new EventHeader());
                envelope.getEventHeader().setTopic(Topic.PUSH_NOTIFICATION);
                envelope.getEventHeader().setParameters(new HashMap<>());
                envelope.getEventHeader().setClassName(getClass().getName().split("\\$")[0]);
                return envelope;
            }
        };
    }

}
