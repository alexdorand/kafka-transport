package com.otz.transport.common.content;

import com.otz.transport.common.*;
import com.otz.transport.common.serializer.JsonEnvelopSerializer;
import com.otz.transport.common.serializer.JsonErrorContentSerializer;

import java.util.HashMap;
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
public class ErrorEventContent extends EventContent {

    private String errorType;
    private String errorMessage;

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public interface ErrorTypeInterface {
        ErrorMessageInterface errorType(String errorType);
    }

    public interface ErrorMessageInterface {
        ErrorValueInterface message(String message);
    }

    public interface ErrorValueInterface {

        ErrorValueInterface value(String key, String value);

        ErrorValueInterface valueMap(Map<String, String> values);

        ErrorEventContent content();

        Envelope envelop();

    }


    public static ErrorTypeInterface create(String correlationId) {
        ErrorEventContent eventContent = new ErrorEventContent();

        return errorType -> message -> new ErrorValueInterface() {

            @Override
            public ErrorValueInterface value(String key, String value) {
                eventContent.getValues().put(key, value);
                return this;
            }

            @Override
            public ErrorValueInterface valueMap(Map<String, String> values) {
                for (String key : values.keySet()) {
                    value(key, values.get(key));
                }
                return this;
            }

            @Override
            public ErrorEventContent content() {
                eventContent.setTimestamp(System.currentTimeMillis());
                eventContent.setErrorType(errorType);
                eventContent.setErrorMessage(message);
                eventContent.setCorrelationId(correlationId);
                eventContent.setTopic(Topic.APPLICATION_EVENT);

                return eventContent;
            }

            @Override
            public Envelope envelop() {
                ContentSerializer contentSerializer = JsonErrorContentSerializer.create();
                ContentSerializer envelopSerializer = JsonEnvelopSerializer.create();

                Envelope envelope = new Envelope(contentSerializer, envelopSerializer);
                envelope.setContent(contentSerializer.serialize(content()));
                envelope.setEventHeader(new EventHeader());
                envelope.getEventHeader().setTopic(Topic.ERROR);
                envelope.getEventHeader().setParameters(new HashMap<>());
                envelope.getEventHeader().setClassName(getClass().getName().split("\\$")[0]);
                return envelope;
            }
        };

    }


}
