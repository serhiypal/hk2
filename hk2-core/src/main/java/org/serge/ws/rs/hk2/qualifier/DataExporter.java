package org.serge.ws.rs.hk2.qualifier;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jvnet.hk2.annotations.Service;

@Service
public class DataExporter implements Exporter {

    @Inject
    @OneDrive
    private Storage storage;

    @Override
    public void export() {
        try {
            storage.persist(new ByteArrayInputStream("Hello, world!".getBytes()), "helloWorld.txt");
        } catch (IOException e) {
            System.err.println("Skipping storing, error occurred: " + e.getMessage());
        }
    }

    public Storage getStorage() {
        return this.storage;
    }

}
