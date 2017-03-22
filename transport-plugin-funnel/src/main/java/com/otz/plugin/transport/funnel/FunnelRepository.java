package com.otz.plugin.transport.funnel;

import com.google.gson.Gson;
import com.otz.couchbase.client.AbstractCouchbaseClient;
import com.otz.couchbase.client.Key;
import com.otz.plugin.transport.funnel.domain.*;
import com.otz.plugin.transport.funnel.exception.DefinitionAlreadyExistsException;
import com.otz.plugin.transport.funnel.exception.InvalidDefinitionNameException;
import com.otz.plugin.transport.funnel.util.StreamConverter;
import com.otz.transport.common.Topic;
import org.springframework.stereotype.Repository;
import rx.Observable;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
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
 * Created by alexdorand on 2016-12-05.
 */
@Repository
public class FunnelRepository extends AbstractCouchbaseClient {

    private static final String ALL = "all";
    private static final String NAME_REGEX = "^[a-zA-Z0-9_]*$";

    private Key<ComponentDefinitions> KEY_COMPONENT_STREAM_DEFINITION_NAMES = new Key<>("components::definitions::", null, ComponentDefinitions.class);
    private Key<StreamDefinitions> KEY_STREAM_DEFINITION = new Key<>("definitions::", null, StreamDefinitions.class);
    private Key<StreamDefinitionNames> KEY_STREAM_DEFINITION_NAMES = new Key<>("definitions::names::", null, StreamDefinitionNames.class);
    private Key<StreamList> KEY_STREAM_LIST = new Key<>("streams::", null, StreamList.class);


    @PostConstruct
    public void init() {
        loadBucket("core");
    }

    /**
     * Get the name of all registered definition names
     *
     * @return
     */
    public Observable<StreamDefinitionNames> getStreamDefinitionNames() {
        return get(KEY_STREAM_DEFINITION_NAMES, ALL)
                .defaultIfEmpty(StreamDefinitionNames.create());
    }


    /**
     * @param definitionName
     * @return
     */
    public Observable<Boolean> checkDefinitionNameAlreadyRegistered(String definitionName) {
        return exists(KEY_STREAM_DEFINITION_NAMES, ALL)
                .flatMap(definitionNamesRegistryCreated -> {
                    if (definitionNamesRegistryCreated) {
                        return getStreamDefinitionNames()
                                .flatMap(streamDefinitionNames -> Observable.just(streamDefinitionNames.contains(definitionName)));
                    } else {
                        return Observable.just(false);
                    }
                });
    }

    /**
     * @param streamDefinitions
     * @return
     */
    public Observable<StreamDefinitions> createStreamDefinitions(StreamDefinitions streamDefinitions) {
        return validateDefinitionName(streamDefinitions.getName())
                .flatMap(name -> checkDefinitionNameAlreadyRegistered(streamDefinitions.getName())
                        .flatMap(definitionAlreadyRegistered -> {
                            if (definitionAlreadyRegistered) {
                                return Observable.error(new DefinitionAlreadyExistsException(streamDefinitions.getName() + " already registered"));
                            } else {
                                return upsert(KEY_STREAM_DEFINITION, streamDefinitions.getName(), streamDefinitions)
                                        .flatMap(streamDefinitionsCreated -> updateDefinitionNamesWith(streamDefinitionsCreated.getName()))
                                        .flatMap(streamDefinitionNamesCreated -> Observable.just(streamDefinitions));
                            }
                        }));
    }

    private Observable<String> validateDefinitionName(String name) {
        if (name.matches(NAME_REGEX)) {
            return Observable.just(name);
        }
        return Observable.error(new InvalidDefinitionNameException("Invalid name " + name));
    }


    /**
     * @param name
     * @return
     */
    private Observable<StreamDefinitionNames> updateDefinitionNamesWith(String name) {
        return getStreamDefinitionNames()
                .defaultIfEmpty(StreamDefinitionNames.create())
                .flatMap(streamDefinitionNames -> {
                    streamDefinitionNames.getNames().add(name);
                    return upsert(KEY_STREAM_DEFINITION_NAMES, ALL, streamDefinitionNames);
                });
    }


    /**
     * Loading stream definitions by name
     *
     * @param streamDefinitionsName
     * @return
     */
    public Observable<StreamDefinitions> getStreamDefinitions(String streamDefinitionsName) {
        return get(KEY_STREAM_DEFINITION, streamDefinitionsName);
    }


    /**
     * Build stream from definition
     *
     * @param attachedId            this can be a userId, it can be an imageId, it can be a noteId, anything that needs to be tracked
     * @param streamDefinitionsName the definitions group from which it build the stream
     * @return
     */
    public Observable<StreamList> generateStreamFor(String attachedId, String streamDefinitionsName) {
        return getStreamDefinitions(streamDefinitionsName)
                .flatMap(streamDefinitions -> Observable.just(StreamConverter.convert(streamDefinitions, attachedId)))
                .flatMap(streamList -> upsert(KEY_STREAM_LIST, attachedId + SEPARATOR + streamDefinitionsName, streamList))
                .flatMap(streamList -> updateComponentDefinitionNames(attachedId, streamList));
    }


    public Observable<StreamDefinitionNames> getDefinitionsRegisteredForComponent(String componentId) {
        return get(KEY_COMPONENT_STREAM_DEFINITION_NAMES, componentId)
                .flatMap(componentDefinitions -> Observable.just(componentDefinitions.getStreamDefinitionNames()));
    }


    private Observable<StreamList> updateComponentDefinitionNames(String attachedId, StreamList streamList) {

        return get(KEY_COMPONENT_STREAM_DEFINITION_NAMES, attachedId)
                .defaultIfEmpty(defaultComponentDefinitions(attachedId))
                .flatMap(componentDefinitions -> {
                    componentDefinitions.getStreamDefinitionNames().getNames().add(streamList.getName());
                    return upsert(KEY_COMPONENT_STREAM_DEFINITION_NAMES, attachedId, componentDefinitions);
                })
                .flatMap(componentDefinitionsUpdated -> Observable.just(streamList));

    }

    private ComponentDefinitions defaultComponentDefinitions(String attachedId) {

        ComponentDefinitions componentDefinitions = new ComponentDefinitions();
        componentDefinitions.setComponentId(attachedId);
        componentDefinitions.setStreamDefinitionNames(new StreamDefinitionNames());
        componentDefinitions.getStreamDefinitionNames().setNames(new ArrayList<>());

        return componentDefinitions;
    }


    public Observable<StreamList> updateStreamList(StreamList streamList) {
        return upsert(KEY_STREAM_LIST, streamList.getAttachedId() + SEPARATOR + streamList.getName(), streamList);
    }


    /**
     * Get streams for attachedId
     *
     * @param attachedId            this can be a userId, it can be an imageId, it can be a noteId, anything that needs to be tracked
     * @param streamDefinitionsName the definitions group from which it build the stream
     * @return
     */
    public Observable<StreamList> getStreams(String attachedId, String streamDefinitionsName) {
        return get(KEY_STREAM_LIST, attachedId + SEPARATOR + streamDefinitionsName);
    }


    public static void mains(String[] args) {


    }

    public static void generalUser() {

        StreamDefinitions streamDefinitions = new StreamDefinitions();
        List<StreamDefinition> streamDefinitionsList = new ArrayList<>();

        StreamDefinition generalStreamDefinition = new StreamDefinition();
        generalStreamDefinition.setStageDefinitions(new ArrayList<>());
        generalStreamDefinition.setId("GENERAL_STREAM");
        generalStreamDefinition.setName("General Stream");


        StageDefinition userRegistration = new StageDefinition("USER_DEFINITION", "User Registration", null);
        StageDefinition welcomeEmail = new StageDefinition("WELCOME_EMAIL", "Welcome Email", userRegistration.getId());
        StageDefinition ctaEmailVerified = new StageDefinition("CTA_ACCOUNT_COMPLETION", "CTA Email Verified", welcomeEmail.getId());

        userRegistration.setEventTriggers(new ArrayList<>());
        userRegistration.getEventTriggers().add(new EventTrigger("calculateUserScore"));

        generalStreamDefinition.getStageDefinitions().add(userRegistration);
        generalStreamDefinition.getStageDefinitions().add(welcomeEmail);
        generalStreamDefinition.getStageDefinitions().add(ctaEmailVerified);

        StageDefinition defaultProfileCreated = new StageDefinition(UUID.randomUUID().toString(), "Default Profile Created", userRegistration.getId());
        StageDefinition profileTypeSelected = new StageDefinition(UUID.randomUUID().toString(), "Profile type selected", defaultProfileCreated.getId());

        StageDefinition mandatoryFieldCompleted = new StageDefinition(UUID.randomUUID().toString(), "Completed mandatory fields", profileTypeSelected.getId());

        StageDefinition avatarSet = new StageDefinition(UUID.randomUUID().toString(), "Avatar set", mandatoryFieldCompleted.getId());
        StageDefinition backgroundImageSet = new StageDefinition(UUID.randomUUID().toString(), "Profile Background Image Set", avatarSet.getId());
        StageDefinition albumIsOptimum = new StageDefinition(UUID.randomUUID().toString(), "Album is optimum", backgroundImageSet.getId());
        StageDefinition albumIsOptimumBadgeGranted = new StageDefinition(UUID.randomUUID().toString(), "BADGE: Album is optimum", albumIsOptimum.getId(), "images/badge.ico");
        StageDefinition enteredCompetition = new StageDefinition(UUID.randomUUID().toString(), "Entered competition", albumIsOptimum.getId());
        StageDefinition defaultUserSearchPreferenceBuilt = new StageDefinition(UUID.randomUUID().toString(), "Default User Search Preference Created", defaultProfileCreated.getId());
        StageDefinition defaultProjectSearchPreferenceBuilt = new StageDefinition(UUID.randomUUID().toString(), "Default Project Search Preference Created", defaultUserSearchPreferenceBuilt.getId());

        profileTypeSelected.setEventTriggers(new ArrayList<>());
        profileTypeSelected.getEventTriggers().add(new EventTrigger("updateDefaultSearch"));

        mandatoryFieldCompleted.setEventTriggers(new ArrayList<>());
        mandatoryFieldCompleted.getEventTriggers().add(new EventTrigger("createUserSuggestionList"));
        mandatoryFieldCompleted.getEventTriggers().add(new EventTrigger("findProjectForUser"));
        mandatoryFieldCompleted.getEventTriggers().add(new EventTrigger("profileCompletionCTA"));
        mandatoryFieldCompleted.getEventTriggers().add(new EventTrigger("profileCompletion"));
        mandatoryFieldCompleted.getEventTriggers().add(new EventTrigger("calculateUserScore"));

        albumIsOptimum.setEventTriggers(new ArrayList<>());
        albumIsOptimum.getEventTriggers().add(new EventTrigger("sendAlbumIsOptimumPushNotification"));
        albumIsOptimum.getEventTriggers().add(new EventTrigger("sendAlbumIsOptimumHipChat"));
        albumIsOptimum.getEventTriggers().add(new EventTrigger("executeAlbumOptimumMarketing"));
        albumIsOptimum.getEventTriggers().add(new EventTrigger("considerForAlbumContentAward"));
        albumIsOptimum.getEventTriggers().add(new EventTrigger("calculateUserScore"));


        StreamDefinition accountCompletionDefinition = new StreamDefinition();
        accountCompletionDefinition.setStageDefinitions(new ArrayList<>());
        accountCompletionDefinition.setId(UUID.randomUUID().toString());
        accountCompletionDefinition.setName("Account Completion Stream");

        accountCompletionDefinition.getStageDefinitions().add(defaultProfileCreated);
        accountCompletionDefinition.getStageDefinitions().add(profileTypeSelected);
        accountCompletionDefinition.getStageDefinitions().add(mandatoryFieldCompleted);
        accountCompletionDefinition.getStageDefinitions().add(avatarSet);
        accountCompletionDefinition.getStageDefinitions().add(backgroundImageSet);
        accountCompletionDefinition.getStageDefinitions().add(albumIsOptimum);
        accountCompletionDefinition.getStageDefinitions().add(albumIsOptimumBadgeGranted);
        accountCompletionDefinition.getStageDefinitions().add(defaultUserSearchPreferenceBuilt);
        accountCompletionDefinition.getStageDefinitions().add(defaultProjectSearchPreferenceBuilt);


        StreamDefinition socialMediaDefinition = new StreamDefinition();
        socialMediaDefinition.setStageDefinitions(new ArrayList<>());
        socialMediaDefinition.setId(UUID.randomUUID().toString());
        socialMediaDefinition.setName("Social Media Completion Stream");

        StageDefinition defaultSocialProfileCreated = new StageDefinition(UUID.randomUUID().toString(), "Default Social Profile Created", userRegistration.getId());
        StageDefinition profileSharedOnFaceBook = new StageDefinition(UUID.randomUUID().toString(), "Profile Shared On Facebook", defaultSocialProfileCreated.getId());
        StageDefinition profileSharedOnFacebookBadge = new StageDefinition(UUID.randomUUID().toString(), "BADGE: Profile Shared On Facebook", profileSharedOnFaceBook.getId(), "images/badge.ico");
        StageDefinition profileSharedOnTwitter = new StageDefinition(UUID.randomUUID().toString(), "Profile Shared On Twitter", profileSharedOnFacebookBadge.getId());
        StageDefinition profileSharedOnTwitterBadge = new StageDefinition(UUID.randomUUID().toString(), "BADGE: Profile Shared On Twitter", profileSharedOnTwitter.getId(), "images/badge.ico");
        StageDefinition linkedInstagram = new StageDefinition(UUID.randomUUID().toString(), "Linked to instagram", profileSharedOnTwitterBadge.getId());
        StageDefinition linkedVimeo = new StageDefinition(UUID.randomUUID().toString(), "linked to vimeo", linkedInstagram.getId());
        StageDefinition linkedYoutube = new StageDefinition(UUID.randomUUID().toString(), "linked to youtube", linkedVimeo.getId());
        StageDefinition socialMediaSavvy = new StageDefinition(UUID.randomUUID().toString(), "BADGE: Social media savvy", linkedYoutube.getId(), "images/badge.ico");

        profileSharedOnFaceBook.setEventTriggers(new ArrayList<>());
        profileSharedOnFaceBook.getEventTriggers().add(new EventTrigger("sendProfileSharedOnFacebookHipChat"));
        profileSharedOnFaceBook.getEventTriggers().add(new EventTrigger("calculateSocialStance"));
        profileSharedOnFaceBook.getEventTriggers().add(new EventTrigger("calculateUserScore"));

        profileSharedOnTwitter.setEventTriggers(new ArrayList<>());
        profileSharedOnTwitter.getEventTriggers().add(new EventTrigger("sendProfileSharedOnTwitterHipChat"));
        profileSharedOnTwitter.getEventTriggers().add(new EventTrigger("calculateSocialStance"));
        profileSharedOnTwitter.getEventTriggers().add(new EventTrigger("calculateUserScore"));

        linkedInstagram.setEventTriggers(new ArrayList<>());
        linkedInstagram.getEventTriggers().add(new EventTrigger("sendInstagramLinkedHipChat"));
        linkedInstagram.getEventTriggers().add(new EventTrigger("calculateSocialStance"));
        linkedInstagram.getEventTriggers().add(new EventTrigger("calculateUserScore"));


        linkedVimeo.setEventTriggers(new ArrayList<>());
        linkedVimeo.getEventTriggers().add(new EventTrigger("sendVimeoLinkedHipChat"));
        linkedVimeo.getEventTriggers().add(new EventTrigger("vimeoLinked"));
        linkedVimeo.getEventTriggers().add(new EventTrigger("calculateSocialStance"));
        linkedVimeo.getEventTriggers().add(new EventTrigger("calculateUserScore"));

        linkedYoutube.setEventTriggers(new ArrayList<>());
        linkedYoutube.getEventTriggers().add(new EventTrigger("sendYoutubeLinkedHipChat"));
        linkedYoutube.getEventTriggers().add(new EventTrigger("calculateSocialStance"));
        linkedYoutube.getEventTriggers().add(new EventTrigger("calculateUserScore"));

        socialMediaDefinition.getStageDefinitions().add(defaultSocialProfileCreated);
        socialMediaDefinition.getStageDefinitions().add(profileSharedOnFaceBook);
        socialMediaDefinition.getStageDefinitions().add(profileSharedOnFacebookBadge);
        socialMediaDefinition.getStageDefinitions().add(profileSharedOnTwitter);
        socialMediaDefinition.getStageDefinitions().add(profileSharedOnTwitterBadge);
        socialMediaDefinition.getStageDefinitions().add(linkedInstagram);
        socialMediaDefinition.getStageDefinitions().add(linkedVimeo);
        socialMediaDefinition.getStageDefinitions().add(linkedYoutube);
        socialMediaDefinition.getStageDefinitions().add(socialMediaSavvy);

        StreamDefinition projectDefinition = new StreamDefinition();
        projectDefinition.setStageDefinitions(new ArrayList<>());
        projectDefinition.setId(UUID.randomUUID().toString());
        projectDefinition.setName("Social Media Completion Stream");

        StageDefinition createdFirstProject = new StageDefinition(UUID.randomUUID().toString(), "Created the first project", userRegistration.getId());
        StageDefinition projectCreationEmailSent = new StageDefinition(UUID.randomUUID().toString(), "Project creation email", createdFirstProject.getId());
        StageDefinition projectCreationBadge = new StageDefinition(UUID.randomUUID().toString(), "BADGE: first project create", projectCreationEmailSent.getId(), "images/badge.ico");
        StageDefinition projectCreationCtaSent = new StageDefinition(UUID.randomUUID().toString(), "CTA Project creation presented", projectCreationBadge.getId());
        StageDefinition projectDistributed = new StageDefinition(UUID.randomUUID().toString(), "Project distributed to potential users", projectCreationCtaSent.getId());

        createdFirstProject.setEventTriggers(new ArrayList<>());
        createdFirstProject.getEventTriggers().add(new EventTrigger("generateCandidateForProject"));
        createdFirstProject.getEventTriggers().add(new EventTrigger("sendProjectCreatedPushN"));
        createdFirstProject.getEventTriggers().add(new EventTrigger("calculateUserScore"));


        projectDistributed.setEventTriggers(new ArrayList<>());
        projectDistributed.getEventTriggers().add(new EventTrigger("sendProjectPotentialsInformedPushNotification"));

        projectDefinition.getStageDefinitions().add(createdFirstProject);
        projectDefinition.getStageDefinitions().add(projectCreationEmailSent);
        projectDefinition.getStageDefinitions().add(projectCreationBadge);
        projectDefinition.getStageDefinitions().add(projectCreationCtaSent);
        projectDefinition.getStageDefinitions().add(projectDistributed);

        StreamDefinition recommendationDefinition = new StreamDefinition();
        recommendationDefinition.setStageDefinitions(new ArrayList<>());
        recommendationDefinition.setId(UUID.randomUUID().toString());
        recommendationDefinition.setName("Recommendation Stream");

        StageDefinition userRecommendationCreated = new StageDefinition(UUID.randomUUID().toString(), "User recommendation list for the user created", mandatoryFieldCompleted.getId());
        StageDefinition userRecommendationEmailSent = new StageDefinition(UUID.randomUUID().toString(), "User recommendation list email", userRecommendationCreated.getId());
        StageDefinition userRecommendationAddedToWall = new StageDefinition(UUID.randomUUID().toString(), "User recommendation list added to wall", userRecommendationEmailSent.getId());

        recommendationDefinition.getStageDefinitions().add(userRecommendationCreated);
        recommendationDefinition.getStageDefinitions().add(userRecommendationEmailSent);
        recommendationDefinition.getStageDefinitions().add(userRecommendationAddedToWall);

        userRecommendationCreated.setEventTriggers(new ArrayList<>());
        userRecommendationCreated.getEventTriggers().add(new EventTrigger("calculateUserScore"));

        streamDefinitionsList.add(generalStreamDefinition);
        streamDefinitionsList.add(accountCompletionDefinition);
        streamDefinitionsList.add(projectDefinition);
        streamDefinitionsList.add(socialMediaDefinition);
        streamDefinitionsList.add(recommendationDefinition);

        streamDefinitions.setName("user");
        streamDefinitions.setDefinitions(streamDefinitionsList);

        System.out.println((new Gson()).toJson(streamDefinitions));
    }



    /*



        return streamDefinitions;
     */
}
