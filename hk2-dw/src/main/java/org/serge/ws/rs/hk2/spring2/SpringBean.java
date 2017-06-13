package org.serge.ws.rs.hk2.spring2;

import org.springframework.stereotype.Component;

@Component
public class SpringBean {

    public String getStatus() {
        return "init";
    }

}
