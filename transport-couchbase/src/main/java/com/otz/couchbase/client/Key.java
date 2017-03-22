package com.otz.couchbase.client;

import java.io.Serializable;

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
 * Created by alexdorand on 2016-12-06.
 */
public class Key<T extends Serializable> {

    private static final String EMPTY = "";
    private final String pre;
    private final String post;
    private final Class<T> forClass;


    public Key(String pre, String post, Class<T> forClass) {
        this.pre = pre;
        this.post = post;
        this.forClass = forClass;
    }


    String fullKey(String id) {
        return (pre != null ? pre : EMPTY) + id + (post != null ? post : EMPTY);
    }

    public Class<T> getForClass() {
        return forClass;
    }
}
