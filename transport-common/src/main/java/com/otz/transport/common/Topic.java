package com.otz.transport.common;

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
@SuppressWarnings("WeakerAccess")
public enum Topic {

    EMAIL("email", "transport-email"),
    ERROR("error", "transport-error"),

    APPLICATION_LOG("applicationLog", "transport-application-log"),
    APPLICATION_EVENT("applicationEvent", "transport-application-event"),

    SMS("sms", "transport-sms"),
    PUSH_NOTIFICATION("pushNotification", "transport-push-notification"),
    FUNNEL("funnel", "transport-funnel"),
    HIPCHAT("hipchat", "hipchat-funnel");

    private final String name;
    private final String topicName;

    Topic(String name, String topicName) {
        this.topicName = topicName;
        this.name = name;
    }

    public String getTopicName() {
        return topicName;
    }
}
