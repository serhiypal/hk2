package org.serge.ws.rs.hk2.qualifier;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.glassfish.hk2.api.IterableProvider;
import org.jvnet.hk2.annotations.Service;

@Service
public class MultiDataExporter implements Exporter {

    @Inject
    private IterableProvider<Storage> storages;

    @Override
    public void export() {
        storages.forEach(storage ->
            storage.persist(new ByteArrayInputStream("Hello, world!".getBytes()), "helloWorld.txt")
        );
    }

    public List<Storage> getStorages() {
        List<Storage> dumpedStorages = new ArrayList<>();
        storages.forEach(dumpedStorages::add);
        return dumpedStorages;
    }

}
