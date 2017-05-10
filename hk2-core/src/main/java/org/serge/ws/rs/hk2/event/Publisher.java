package org.serge.ws.rs.hk2.event;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.glassfish.hk2.api.messaging.Topic;

@Singleton
public class Publisher {

    @Inject
    private Topic<TextEvent> topic;

    public void send(String message) {
        topic.publish(new TextEvent(message));
    }

}
