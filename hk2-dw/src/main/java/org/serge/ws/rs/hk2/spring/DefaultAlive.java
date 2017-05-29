package org.serge.ws.rs.hk2.spring;

import org.springframework.stereotype.Component;

@Component
public class DefaultAlive implements Alive {

    @Override
    public String status() {
        return "OK";
    }

}
