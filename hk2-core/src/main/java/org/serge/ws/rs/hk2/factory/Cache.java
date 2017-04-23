package org.serge.ws.rs.hk2.factory;

import org.jvnet.hk2.annotations.Contract;

@Contract
public interface Cache {

    Object get(Object key);

    Object put(Object key, Object value);

    Object remove(Object key);

    void removeAll();

}
