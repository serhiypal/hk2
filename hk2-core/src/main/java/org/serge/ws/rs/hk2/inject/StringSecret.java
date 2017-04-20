package org.serge.ws.rs.hk2.inject;

import javax.inject.Inject;
import javax.inject.Named;

import org.jvnet.hk2.annotations.Service;

@Service
public class StringSecret implements Secret {

    @Decrypt("base64")
    @Named("base64_value")
    @Inject
    private String secret;

    @Override
    public String getValue() {
        return secret;
    }
}
