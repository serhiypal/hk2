package org.serge.ws.rs.hk2.factory;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

@Service
public class CachableServiceImpl implements CachableService {

    @Inject
    private Cache cache;

    @Override
    public void doSomething() {
        cache.put("Something in key", "Something in value");
    }

}
