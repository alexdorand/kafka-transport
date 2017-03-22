package com.otz.plugin.transport.funnel.exception;

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
 * Created by alexdorand on 2016-12-07.
 */
public class StageNotFoundException extends RuntimeException {

    public StageNotFoundException() {
        super();
    }

    public StageNotFoundException(String message) {
        super(message);
    }

    public StageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public StageNotFoundException(Throwable cause) {
        super(cause);
    }

    protected StageNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}