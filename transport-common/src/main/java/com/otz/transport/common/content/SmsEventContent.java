package com.otz.transport.common.content;

import com.otz.transport.common.*;
import com.otz.transport.common.serializer.JsonEmailContentSerializer;
import com.otz.transport.common.serializer.JsonEnvelopSerializer;
import com.otz.transport.common.serializer.JsonSmsContentSerializer;

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
public class SmsEventContent extends EventContent {

    private String smsType;

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }

    public interface SmsTypeInterface {
        SmsLocaleInterface smsType(String smsType);
    }

    public interface SmsLocaleInterface {

        SmsValueInterface locale(Locale locale);
    }

    public interface SmsValueInterface {

        SmsValueInterface value(String key, String value);

        SmsValueInterface valueMap(Map<String, String> values);

        SmsEventContent content();

        Envelope envelop();

    }


    public static SmsTypeInterface create(String correlationId) {
        SmsEventContent eventContent = new SmsEventContent();

        return smsType -> locale -> new SmsValueInterface() {
            @Override
            public SmsValueInterface value(String key, String value) {
                eventContent.getValues().put(key, value);
                return this;
            }

            @Override
            public SmsValueInterface valueMap(Map<String, String> values) {
                for (String key : values.keySet()) {
                    value(key, values.get(key));
                }
                return this;
            }

            @Override
            public SmsEventContent content() {
                eventContent.setSmsType(smsType);
                eventContent.setTopic(Topic.EMAIL);
                eventContent.setLocale(locale);
                eventContent.setCorrelationId(correlationId);
                return eventContent;
            }

            @Override
            public Envelope envelop() {
                ContentSerializer contentSerializer = JsonSmsContentSerializer.create();
                ContentSerializer envelopSerializer = JsonEnvelopSerializer.create();

                Envelope envelope = new Envelope(contentSerializer, envelopSerializer);
                envelope.setContent(contentSerializer.serialize(content()));
                envelope.setEventHeader(new EventHeader());
                envelope.getEventHeader().setTopic(Topic.SMS);
                envelope.getEventHeader().setParameters(new HashMap<>());
                envelope.getEventHeader().setClassName(getClass().getName().split("\\$")[0]);
                return envelope;
            }
        };

    }


}
