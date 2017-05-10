package org.serge.ws.rs.hk2.event;

import javax.inject.Singleton;

import org.glassfish.hk2.api.messaging.MessageReceiver;
import org.glassfish.hk2.api.messaging.SubscribeTo;
import org.jvnet.hk2.annotations.Service;

@Service
@Singleton
@MessageReceiver
public class Subscriber {

    private TextEvent textEvent;

    public void listen(@SubscribeTo TextEvent textEvent) {
        this.textEvent = textEvent;
    }

    public String getMessage() {
        return textEvent != null ? textEvent.getMessage() : null;
    }

}
