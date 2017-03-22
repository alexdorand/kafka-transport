package com.otz.transport.common.content;


import com.otz.transport.common.*;
import com.otz.transport.common.serializer.JsonEnvelopSerializer;
import com.otz.transport.common.serializer.JsonFunnelContentSerializer;

import java.util.Locale;
import java.util.Map;

public class FunnelEventContent extends EventContent {

    public interface ActionInterface {

        LocaleUpdateInterface update();

        LocaleAttachInterface attach();

    }

    public interface LocaleUpdateInterface {
        UpdateInterface locale(Locale locale);
    }

    public interface LocaleAttachInterface {
        AttachInterface locale(Locale locale);
    }

    public interface UpdateInterface {
        UpdateDefinitionInterface stage(String stageId);
    }

    public interface UpdateDefinitionInterface {
        UpdateComponentInterface definition(String definitionName);
    }

    public interface UpdateComponentInterface {

        UpdateStatusComponentInterface attachedTo(String attachedId);

    }

    public interface UpdateStatusComponentInterface {

        AuxInterface status(String status);

    }

    public interface AttachInterface {

        AttachComponentInterface definition(String definitionName);

    }


    public interface AttachComponentInterface {

        AuxInterface attachTo(String attachedId);

    }


    public interface AuxInterface {

        AuxInterface value(String key, String value);

        AuxInterface values(Map<String, String> values);

        EventContent content();

        Envelope envelop();
    }

    public static ActionInterface create(String correlationId) {

        ContentSerializer contentSerializer = JsonFunnelContentSerializer.create();
        ContentSerializer envelopSerializer = JsonEnvelopSerializer.create();
        EventContent eventContent = new FunnelEventContent();

        return new ActionInterface() {
            @Override
            public LocaleUpdateInterface update() {

                return locale -> stageId -> definitionName -> attachedId -> status -> new AuxInterface() {
                    @Override
                    public AuxInterface value(String key, String value) {
                        eventContent.getValues().put(key, value);
                        return this;
                    }

                    @Override
                    public AuxInterface values(Map<String, String> values) {
                        for (String key : values.keySet()) {
                            value(key, values.get(key));
                        }
                        return this;
                    }

                    @Override
                    public EventContent content() {
                        eventContent.setTopic(Topic.FUNNEL);
                        eventContent.setTimestamp(System.currentTimeMillis());
                        eventContent.setAction("update");
                        eventContent.setDefinitionName(definitionName);
                        eventContent.setStageId(stageId);
                        eventContent.setAttachedId(attachedId);
                        eventContent.setStatus(status);
                        eventContent.setLocale(locale);
                        eventContent.setCorrelationId(correlationId);

                        return eventContent;
                    }

                    @SuppressWarnings("Duplicates")
                    @Override
                    public Envelope envelop() {
                        Envelope envelope = new Envelope(contentSerializer, envelopSerializer);
                        envelope.setEventHeader(new EventHeader());
                        envelope.getEventHeader().setTopic(Topic.FUNNEL);
                        envelope.setContent(contentSerializer.serialize(content()));
                        return envelope;
                    }
                };
            }

            @Override
            public LocaleAttachInterface attach() {

                return locale -> definitionName -> attachedId -> new AuxInterface() {
                    @Override
                    public AuxInterface value(String key, String value) {
                        eventContent.getValues().put(key, value);
                        return this;
                    }

                    @Override
                    public AuxInterface values(Map<String, String> values) {
                        for (String key : values.keySet()) {
                            value(key, values.get(key));
                        }
                        return this;
                    }

                    @Override
                    public EventContent content() {
                        EventContent dynamicEventContent = new FunnelEventContent();
                        dynamicEventContent.setTopic(Topic.FUNNEL);
                        dynamicEventContent.setTimestamp(System.currentTimeMillis());
                        dynamicEventContent.setAction("attach");
                        dynamicEventContent.setDefinitionName(definitionName);
                        dynamicEventContent.setAttachedId(attachedId);
                        dynamicEventContent.setLocale(locale);
                        dynamicEventContent.setCorrelationId(correlationId);


                        return dynamicEventContent;
                    }

                    @SuppressWarnings("Duplicates")
                    @Override
                    public Envelope envelop() {
                        Envelope envelope = new Envelope(contentSerializer, envelopSerializer);
                        envelope.setEventHeader(new EventHeader());
                        envelope.getEventHeader().setTopic(Topic.FUNNEL);
                        envelope.setContent(contentSerializer.serialize(content()));
                        return envelope;
                    }
                };
            }
        };
    }


}
