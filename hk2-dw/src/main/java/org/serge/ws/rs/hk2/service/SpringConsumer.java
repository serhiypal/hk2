package org.serge.ws.rs.hk2.service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jvnet.hk2.annotations.Contract;
import org.jvnet.hk2.annotations.Service;
import org.serge.ws.rs.hk2.spring.LazySpring;

@Contract
@Service
public class SpringConsumer {

    @Inject
    private LazySpring lazySpring;

    @PostConstruct
    public void init() {
        System.out.println("LazySpring: " + lazySpring.getStatus());
    }

    public String value() {
        return lazySpring.getStatus();
    }
}
