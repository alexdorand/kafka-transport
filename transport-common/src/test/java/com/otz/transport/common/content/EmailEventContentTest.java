package com.otz.transport.common.content;

import org.junit.Test;

import java.util.Locale;
import java.util.UUID;

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
public class EmailEventContentTest {

    @Test
    public void testCreate() throws Exception {

        String content = EmailEventContent
                .create(UUID.randomUUID().toString())
                .emailType("newUserRegistration")
                .locale(Locale.ENGLISH)
                .value("test", "testValue")
                .envelop()
                .getContent();

        System.out.println(content);

        content = EmailEventContent
                .create(UUID.randomUUID().toString())
                .emailType("newUserRegistration")
                .locale(Locale.ENGLISH)
                .value("test", "testValue")
                .envelop()
                .getContent();

        System.out.println(content);

    }
}