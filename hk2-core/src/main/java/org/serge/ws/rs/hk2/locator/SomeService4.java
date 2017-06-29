package org.serge.ws.rs.hk2.locator;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;
import org.serge.ws.rs.hk2.scope.RandomService;

@Service
public class SomeService4 implements SomeContract {

    @Inject
    private RandomService randomService;

    @Override
    public void doSomething() {
        System.out.println("SomeService4.doSomething : " + randomService.random());
    }

}
