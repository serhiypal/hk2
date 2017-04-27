package org.serge.ws.rs.hk2.named;

import javax.inject.Named;

import org.jvnet.hk2.annotations.ContractsProvided;

//@Service
@ContractsProvided(StringService.class)
@Named("lower")
public class LowerCaseService implements StringService {

    @Override
    public String apply(String s) {
        return s.toLowerCase();
    }

}
