package org.serge.ws.rs.hk2.scope;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.UUID;

import org.glassfish.hk2.api.PerThread;
import org.jvnet.hk2.annotations.Service;

@Service
@PerThread
@Named
public class UUIDRandomService implements RandomService {

    private Object random;

    @PostConstruct
    public void init() {
        random = UUID.randomUUID();
    }
    @Override
    public Object random() {
        return random;
    }

}
