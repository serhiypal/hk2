package org.serge.ws.rs.hk2.qualifier;

import java.io.IOException;
import java.io.InputStream;

import org.jvnet.hk2.annotations.Contract;

@Contract
public interface Storage {

    void persist(InputStream stream, String name) throws IOException;
}
