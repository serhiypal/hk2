package org.serge.ws.rs.hk2.spring;

import javax.annotation.PostConstruct;

import org.serge.ws.rs.hk2.service.TracingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HK2ServiceConsumer {

    private final TracingService tracingService;

    @Autowired
    public HK2ServiceConsumer(TracingService tracingService) {
        this.tracingService = tracingService;
    }

    @PostConstruct
    public void init() {
        System.out.println(tracingService.trace());
    }

    public String value() {
        return tracingService.trace();
    }
}
