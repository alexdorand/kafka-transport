package com.otz.transport.common;


import java.util.HashMap;
import java.util.Map;

public class Event<T extends EventContent> {

    public ApplicationEventHeader content(T eventContent) {

        return eventHeader -> {

            // Build proper header
            EventHeader header = new EventHeader();
            header.setTopic(Topic.APPLICATION_EVENT);
            header.setTopic(eventContent.getTopic());

            if (eventHeader == null) {
                eventHeader = new HashMap<>();
            }
            header.setParameters(eventHeader);
            header.setType(eventContent.getClass().getSimpleName());

            // Build the envelop
            Envelope envelope = new Envelope();
            envelope.setContent(eventContent.toString());
            envelope.setEventHeader(header);

            return envelope;
        };
    }


    private interface ApplicationEventHeader {

        Envelope header(Map<String, String> headerParameters);

    }

}
