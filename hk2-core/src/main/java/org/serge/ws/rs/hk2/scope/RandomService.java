package org.serge.ws.rs.hk2.scope;

import org.jvnet.hk2.annotations.Contract;

@Contract
public interface RandomService {

    Object random();

}
