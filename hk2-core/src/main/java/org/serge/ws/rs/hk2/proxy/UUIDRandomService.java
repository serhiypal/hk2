package org.serge.ws.rs.hk2.proxy;

import javax.annotation.PostConstruct;
import java.util.UUID;

import org.glassfish.hk2.api.PerThread;
import org.glassfish.hk2.api.UseProxy;
import org.jvnet.hk2.annotations.Service;
import org.serge.ws.rs.hk2.scope.RandomService;

@Service
@PerThread
@UseProxy
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
