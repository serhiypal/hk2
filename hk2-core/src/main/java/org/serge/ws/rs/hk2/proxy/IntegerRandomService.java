package org.serge.ws.rs.hk2.proxy;

import javax.annotation.PostConstruct;
import java.util.Random;

import org.glassfish.hk2.api.PerLookup;
import org.jvnet.hk2.annotations.Service;
import org.serge.ws.rs.hk2.scope.RandomService;

@Service
@PerLookup
public class IntegerRandomService implements RandomService {

    private Object random;

    @PostConstruct
    public void init() { random = new Random().nextInt(); }

    @Override
    public Object random() { return random; }

}
