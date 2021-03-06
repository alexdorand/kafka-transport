package com.otz.transport.common.config;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;

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
 * Created by alexdorand on 2016-12-03.
 */
public class ContentUtil {

    public static String getStringContent(ApplicationContext context, String fileName) throws IOException {

        Resource resource = context.getResource("classpath:/" + fileName);
        int availableByte = resource.getInputStream().available();
        byte[] contentBytes = new byte[availableByte];
        resource.getInputStream().read(contentBytes);
        return new String(contentBytes);
    }
}
