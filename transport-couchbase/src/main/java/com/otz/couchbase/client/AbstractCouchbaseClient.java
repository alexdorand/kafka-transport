package com.otz.couchbase.client;

import com.couchbase.client.core.time.Delay;
import com.couchbase.client.java.AsyncBucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.RawJsonDocument;
import com.couchbase.client.java.error.CASMismatchException;
import com.couchbase.client.java.error.DocumentDoesNotExistException;
import com.couchbase.client.java.util.retry.RetryBuilder;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import rx.Observable;
import rx.functions.Func1;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

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
public class AbstractCouchbaseClient {

    public static final String SEPARATOR = "::";

    @Autowired
    private Environment environment;

    @Autowired
    private Cluster cluster;

    private AsyncBucket bucket;
    private Gson gson = new Gson();

    public void loadBucket(String bucketName) {
        bucket = cluster.openBucket(bucketName).async();
    }

    public <T extends Serializable> Observable<T> upsert(Key<T> key, String id, T object) {
        return bucket.upsert(RawJsonDocument.create(key.fullKey(id), gson.toJson(object)))
                .flatMap(rawJsonDocument -> Observable.just(object));
    }

    public <T extends Serializable> Observable<T> lockAndUpsert(Key<T> key, String id, T object) {
        return Observable.just(RawJsonDocument.create(key.fullKey(id), gson.toJson(object)))
                .flatMap(rawJsonDocument -> bucket.replace(rawJsonDocument)
                        .retryWhen(RetryBuilder.anyOf(CASMismatchException.class)
                                .max(10).delay(Delay.fixed(10, TimeUnit.MICROSECONDS)).build())
                        .onErrorResumeNext((Func1<Throwable, Observable<RawJsonDocument>>) throwable -> {
                            // if doc does not exist on replace, fall back to insert
                            if (throwable instanceof DocumentDoesNotExistException) {
                                return bucket.insert(rawJsonDocument);
                            }
                            // if other error, forward it
                            return Observable.error(throwable);
                        }))
                .flatMap(createdDocument -> Observable.just(object));
    }

    public <T extends Serializable> Observable<Boolean> exists(Key<T> key, String id) {
        return bucket.exists(key.fullKey(id));
    }

    public <T extends Serializable> Observable<T> get( Key<T> key, String id) {

        return bucket.get( RawJsonDocument.create(key.fullKey(id)) )
                .flatMap(rawJsonDocument -> from(rawJsonDocument.content(), key.getForClass()));
    }

    private <T> Observable<T> from( String json, Class<T> classOfT ) {
        return Observable.just(this.gson.fromJson( json, classOfT ));
    }

    public static void main(String[] args) {

    }


}
