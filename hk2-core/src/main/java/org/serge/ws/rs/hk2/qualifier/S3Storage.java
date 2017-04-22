package org.serge.ws.rs.hk2.qualifier;

import java.io.IOException;
import java.io.InputStream;

import org.jvnet.hk2.annotations.Service;

@S3
@Service
public class S3Storage implements Storage {

    @Override
    public void persist(InputStream stream, String name) throws IOException {
        System.out.println("Storing in S3: " + name);
    }

}
