package com.otz.transport.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class EventContent implements Serializable {


    private EventImportance eventImportance;
    private Topic topic;
    private long timestamp;
    private Map<String, String> values = new HashMap<>();
    private String action;
    private String definitionName;
    private String stageId;
    private String attachedId;
    private String status;
    private Locale locale;
    private String correlationId;

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDefinitionName() {
        return definitionName;
    }

    public void setDefinitionName(String definitionName) {
        this.definitionName = definitionName;
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public void setAttachedId(String attachedId) {
        this.attachedId = attachedId;
    }

    public String getAttachedId() {
        return attachedId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public EventImportance getEventImportance() {
        return eventImportance;
    }

    public void setEventImportance(EventImportance eventImportance) {
        this.eventImportance = eventImportance;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
}
