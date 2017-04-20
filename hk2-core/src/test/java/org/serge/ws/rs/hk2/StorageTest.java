package org.serge.ws.rs.hk2;

import java.util.List;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Test;
import org.serge.ws.rs.hk2.qualifier.DataExporter;
import org.serge.ws.rs.hk2.qualifier.Exporter;
import org.serge.ws.rs.hk2.qualifier.MultiDataExporter;
import org.serge.ws.rs.hk2.qualifier.OneDrive;
import org.serge.ws.rs.hk2.qualifier.OneDriveStorage;
import org.serge.ws.rs.hk2.qualifier.S3;
import org.serge.ws.rs.hk2.qualifier.S3Storage;
import org.serge.ws.rs.hk2.qualifier.Storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class StorageTest {

    @Test
    public void qualifiers() throws NoSuchFieldException, IllegalAccessException {
        ServiceLocator serviceLocator = ServiceLocatorUtilities.bind(dc -> {
            dc.bind(BuilderHelper.link(OneDriveStorage.class).to(Storage.class).qualifiedBy(OneDrive.LITERAL).build());
            dc.bind(BuilderHelper.link(S3Storage.class).to(Storage.class).qualifiedBy(S3.LITERAL).build());
            dc.bind(BuilderHelper.link(DataExporter.class).to(Exporter.class).build());
        });
        Exporter exporter = serviceLocator.getService(Exporter.class);
        exporter.export(); // prints "Storing in OneDrive: helloWorld.txt"

        assertTrue("Wrong instance", serviceLocator.getService(DataExporter.class).getStorage() instanceof OneDriveStorage);
        serviceLocator.shutdown();
    }

    @Test
    public void multi() throws NoSuchFieldException, IllegalAccessException {
        ServiceLocator serviceLocator = ServiceLocatorUtilities.bind(dc -> {
            dc.bind(BuilderHelper.link(OneDriveStorage.class).to(Storage.class).qualifiedBy(OneDrive.LITERAL).build());
            dc.bind(BuilderHelper.link(S3Storage.class).to(Storage.class).qualifiedBy(S3.LITERAL).build());
            dc.bind(BuilderHelper.link(MultiDataExporter.class).to(Exporter.class).build());
        });
        Exporter exporter = serviceLocator.getService(Exporter.class);
        exporter.export(); // prints "Storing in OneDrive: helloWorld.txt"

        List<Storage> storages = serviceLocator.getService(MultiDataExporter.class).getStorages();
        assertNotNull(storages);
        assertEquals("Must be 2 storages", 2, storages.size());
        assertTrue("Must have OneDrive", storages.stream().anyMatch(OneDriveStorage.class::isInstance));
        assertTrue("Must have S3", storages.stream().anyMatch(S3Storage.class::isInstance));
        serviceLocator.shutdown();
    }

}
