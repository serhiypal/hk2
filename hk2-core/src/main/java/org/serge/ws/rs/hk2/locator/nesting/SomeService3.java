package org.serge.ws.rs.hk2.locator.nesting;

import org.jvnet.hk2.annotations.Service;

@Service
public class SomeService3 implements SomeContract {

    @Override
    public void doSomething() {
        System.out.println("SomeService3.doSomething");
    }

}