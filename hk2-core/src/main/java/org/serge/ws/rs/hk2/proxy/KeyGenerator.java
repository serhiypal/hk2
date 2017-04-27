package org.serge.ws.rs.hk2.proxy;

import org.jvnet.hk2.annotations.Contract;

@Contract
public interface KeyGenerator {

    Object getKey();

}
