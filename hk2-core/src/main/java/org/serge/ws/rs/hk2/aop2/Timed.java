package org.serge.ws.rs.hk2.aop2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.glassfish.hk2.extras.interception.InterceptionBinder;

@InterceptionBinder
@Retention(RetentionPolicy.RUNTIME)
public @interface Timed {
}
