package org.serge.ws.rs.hk2.level;

import org.glassfish.hk2.runlevel.RunLevel;

@RunLevel(10)
public class FastPaceService extends AbstractPaceService {

    public FastPaceService() {
        super(100);
    }

}
