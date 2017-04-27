package org.serge.ws.rs.hk2.named;

import javax.inject.Inject;
import javax.inject.Named;

import org.jvnet.hk2.annotations.Contract;
import org.jvnet.hk2.annotations.Service;

//@Contract
//@Service
public class TextService {

    @Inject
    @Named("upper")
    private StringService stringService;

    public void print(String string) {
        System.out.println(stringService.apply(string));
    }
}
