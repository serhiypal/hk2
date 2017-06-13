package org.serge.ws.rs.hk2.spring2;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jvnet.hk2.annotations.Contract;
import org.jvnet.hk2.annotations.Service;

@Contract
@Service
public class SpringConsumer {

    @Inject
    private SpringBean springBean;

    @PostConstruct
    public void init() {
        System.out.println("SpringBean: " + springBean.getStatus());
    }

    public String value() {
        return springBean.getStatus();
    }
}
