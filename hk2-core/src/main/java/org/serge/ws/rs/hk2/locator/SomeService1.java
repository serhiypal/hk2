package org.serge.ws.rs.hk2.locator;

import org.jvnet.hk2.annotations.Service;

@Service
public class SomeService1 implements SomeContract {

    @Override
    public void doSomething() {
        System.out.println("SomeService1.doSomething");
    }

}
