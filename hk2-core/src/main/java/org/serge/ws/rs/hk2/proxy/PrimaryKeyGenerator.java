package org.serge.ws.rs.hk2.proxy;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jvnet.hk2.annotations.Service;
import org.serge.ws.rs.hk2.scope.RandomService;

@Service
@Singleton
public class PrimaryKeyGenerator implements KeyGenerator {

    @Inject
    private RandomService randomService;

    public Object getKey() {
        return randomService.random();
    }

}
