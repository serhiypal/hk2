package org.serge.ws.rs.hk2.profile;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;

import org.jvnet.hk2.annotations.Service;

@Service
public class DataExporter implements Exporter {

    @Inject
    private Storage storage;

    @Override
    public void export() {
        storage.persist(new ByteArrayInputStream("Hello, world!".getBytes()), "helloWorld.txt");
    }

    public Storage getStorage() {
        return this.storage;
    }

}
