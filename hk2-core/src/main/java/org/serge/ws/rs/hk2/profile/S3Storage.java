package org.serge.ws.rs.hk2.profile;

import java.io.InputStream;

import org.jvnet.hk2.annotations.Service;

@Service
@Profile({ "s3", "default" })
public class S3Storage implements Storage {

    @Override
    public void persist(InputStream stream, String name) {
        System.out.println("Storing in S3: " + name);
    }

}
