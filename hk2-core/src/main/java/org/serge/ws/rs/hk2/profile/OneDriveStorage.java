package org.serge.ws.rs.hk2.profile;

import java.io.InputStream;

import org.jvnet.hk2.annotations.Service;

@Service
@Profile("onedrive")
public class OneDriveStorage implements Storage {

    @Override
    public void persist(InputStream stream, String name) {
        System.out.println("Storing in OneDrive: " + name);
    }

}
