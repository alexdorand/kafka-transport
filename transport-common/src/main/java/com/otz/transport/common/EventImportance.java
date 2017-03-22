package com.otz.transport.common;

/**
 * Copyright 2017 opentoolzone.com - Kafka Transport
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
 * Created by alexdorand on 2017-01-01.
 */
public enum EventImportance {

    l1("Extreme importance"),
    l2("Strong importance"),
    l3("Moderate importance"),
    l4("Weak importance");

    private String description;

    EventImportance(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
