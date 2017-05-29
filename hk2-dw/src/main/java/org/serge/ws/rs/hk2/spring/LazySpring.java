package org.serge.ws.rs.hk2.spring;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Lazy
@Scope("hk2")
public class LazySpring {

    public String getStatus() {
        return "lazy";
    }

}
