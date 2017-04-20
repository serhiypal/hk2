package org.serge.ws.rs.hk2.inject;

import org.jvnet.hk2.annotations.Contract;

@Contract
public interface Secret {

    String getValue();

}
