package com.otz.transport.common;


import java.util.HashMap;

public class Envelope {

    public static final String MESSAGE_SEPARATOR = "||";

    private EventHeader eventHeader;
    private String content;
    private String contentSerializer;
    private String envelopSerializer;

    public Envelope(ContentSerializer contentSerializer, ContentSerializer envelopSerializer) {
        this.contentSerializer = contentSerializer.getClass().getName();
        this.envelopSerializer = envelopSerializer.getClass().getName();
    }

    public Envelope() {
    }

    public static Envelope create(EventHeader eventHeader, EventContent eventContent, ContentSerializer contentSerializer, ContentSerializer envelopSerializer) {

        Envelope envelope = new Envelope(contentSerializer, envelopSerializer);
        envelope.setEventHeader(eventHeader);

        //noinspection unchecked
        envelope.setContent(contentSerializer.serialize(eventContent));
        envelope.setContentSerializer(contentSerializer.getClass().getName());
        envelope.setEnvelopSerializer(envelopSerializer.getClass().getName());
        return envelope;

    }

    public static Envelope create(EventContent eventContent, ContentSerializer contentSerializer, ContentSerializer envelopSerializer) {

        Envelope envelope = new Envelope(contentSerializer, envelopSerializer);
        EventHeader eventHeader = new EventHeader();
        eventHeader.setTopic(eventContent.getTopic());
        eventContent.setValues(new HashMap<>());
        envelope.setEventHeader(eventHeader);

        //noinspection unchecked
        envelope.setContent(contentSerializer.serialize(eventContent));
        envelope.setContentSerializer(contentSerializer.getClass().getName());
        envelope.setEnvelopSerializer(envelopSerializer.getClass().getName());

        return envelope;
    }

    public EventHeader getEventHeader() {
        return eventHeader;
    }

    public void setEventHeader(EventHeader eventHeader) {
        this.eventHeader = eventHeader;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentSerializer() {
        return contentSerializer;
    }

    private void setContentSerializer(String contentSerializer) {
        this.contentSerializer = contentSerializer;
    }

    public String getEnvelopSerializer() {
        return envelopSerializer;
    }

    private void setEnvelopSerializer(String envelopSerializer) {
        this.envelopSerializer = envelopSerializer;
    }
}
