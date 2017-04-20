package org.serge.ws.rs.hk2.scope;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import java.util.Random;

import org.glassfish.hk2.api.PerLookup;
import org.jvnet.hk2.annotations.Service;

@Service
@PerLookup
@Named
public class IntegerRandomService implements RandomService {

    private Object random;

    @PostConstruct
    public void init() { random = new Random().nextInt(); }

    @Override
    public Object random() { return random; }

}
