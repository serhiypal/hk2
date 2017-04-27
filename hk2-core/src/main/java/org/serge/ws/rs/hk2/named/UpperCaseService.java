package org.serge.ws.rs.hk2.named;

import javax.inject.Named;

import org.jvnet.hk2.annotations.ContractsProvided;
import org.jvnet.hk2.annotations.Service;

//@Service
@ContractsProvided(StringService.class)
@Named("upper")
public class UpperCaseService implements StringService {

    @Override
    public String apply(String s) {
        return s.toUpperCase();
    }

}
