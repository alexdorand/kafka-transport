package com.otz.transport.common.serializer;

import com.google.gson.Gson;
import com.otz.transport.common.ContentSerializer;
import com.otz.transport.common.content.EmailEventContent;
import com.otz.transport.common.content.FunnelEventContent;

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
public class JsonFunnelContentSerializer implements ContentSerializer<FunnelEventContent> {

    private static final Gson gson = new Gson();

    public static JsonFunnelContentSerializer create() {
        return new JsonFunnelContentSerializer();
    }

    @Override
    public String serialize(FunnelEventContent funnelEventContent) {
        return gson.toJson(funnelEventContent);
    }

    @Override
    public FunnelEventContent deserialize(String content) {
        return gson.fromJson(content, FunnelEventContent.class);
    }
}
