package org.serge.ws.rs.hk2.guice;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;

public class GuiceModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(Status.class).to(GuiceStatus.class).in(Singleton.class);
    }

}
