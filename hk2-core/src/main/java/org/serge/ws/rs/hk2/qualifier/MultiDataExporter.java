package org.serge.ws.rs.hk2.qualifier;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.glassfish.hk2.api.IterableProvider;
import org.jvnet.hk2.annotations.Service;

@Service
public class MultiDataExporter implements Exporter {

    private IterableProvider<Storage> storages;

    @Inject
    public MultiDataExporter(IterableProvider<Storage> storages) {
        this.storages = storages;
    }

    @Override
    public void export() {
        storages.forEach(storage -> {
            try {
                storage.persist(new ByteArrayInputStream("Hello, world!".getBytes()), "helloWorld.txt");
            } catch (IOException e) {
                System.err.println("Skipping storing, error occurred: " + e.getMessage());
            }
        });
    }

    public List<Storage> getStorages() {
        List<Storage> dumpedStorages = new ArrayList<>();
        for (Storage storage : storages) {
            dumpedStorages.add(storage);
        }
        return dumpedStorages;
    }

}
