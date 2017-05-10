package org.serge.ws.rs.hk2.level;

import org.glassfish.hk2.runlevel.RunLevel;

@RunLevel(5)
public class MediumPaceService extends AbstractPaceService {

    public MediumPaceService() {
        super(1000);
    }

}
