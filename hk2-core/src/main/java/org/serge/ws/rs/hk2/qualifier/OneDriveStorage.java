package org.serge.ws.rs.hk2.qualifier;

import java.io.IOException;
import java.io.InputStream;

import org.jvnet.hk2.annotations.Service;

@Service
public class OneDriveStorage implements Storage {

    @Override
    public void persist(InputStream stream, String name) throws IOException {
        System.out.println("Storing in OneDrive: " + name);
    }

}
