package org.serge.ws.rs.hk2;

import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.serge.ws.rs.hk2.profile.DataExporter;
import org.serge.ws.rs.hk2.profile.Exporter;
import org.serge.ws.rs.hk2.profile.OneDriveStorage;
import org.serge.ws.rs.hk2.profile.ProfileContext;
import org.serge.ws.rs.hk2.profile.S3Storage;

import static org.junit.Assert.assertTrue;

public class ProfileTest {

    private ServiceLocator serviceLocator;

    @Before
    public void setUp() {
        serviceLocator = ServiceLocatorFactory.getInstance().create("hk2");
    }

    @After
    public void tearDown() {
        serviceLocator.shutdown();
        ServiceLocatorFactory.getInstance().destroy(serviceLocator);
    }

    @Test(expected = MultiException.class)
    public void none() {
        System.clearProperty("profiles");
        ServiceLocatorUtilities.addClasses(serviceLocator, ProfileContext.class, OneDriveStorage.class, S3Storage.class, DataExporter.class);
        Exporter exporter = serviceLocator.getService(Exporter.class);
        exporter.export(); // prints "Storing in S3: helloWorld.txt"
    }

    @Test
    public void s3() {
        System.setProperty("profiles", "s3");
        ServiceLocatorUtilities.addClasses(serviceLocator, ProfileContext.class, OneDriveStorage.class, S3Storage.class, DataExporter.class);
        Exporter exporter = serviceLocator.getService(Exporter.class);
        exporter.export(); // prints "Storing in S3: helloWorld.txt"

        assertTrue("Wrong instance", serviceLocator.getService(DataExporter.class).getStorage() instanceof S3Storage);
    }

    @Test
    public void defaultProfile() {
        System.setProperty("profiles", "default");
        ServiceLocatorUtilities.addClasses(serviceLocator, ProfileContext.class, OneDriveStorage.class, S3Storage.class, DataExporter.class);
        Exporter exporter = serviceLocator.getService(Exporter.class);
        exporter.export(); // prints "Storing in S3: helloWorld.txt"

        assertTrue("Wrong instance", serviceLocator.getService(DataExporter.class).getStorage() instanceof S3Storage);
    }

    @Test
    public void oneDrive() {
        System.setProperty("profiles", "onedrive");
        ServiceLocatorUtilities.addClasses(serviceLocator, ProfileContext.class, OneDriveStorage.class, S3Storage.class, DataExporter.class);
        Exporter exporter = serviceLocator.getService(Exporter.class);
        exporter.export(); // prints "Storing in S3: helloWorld.txt"

        assertTrue("Wrong instance", serviceLocator.getService(DataExporter.class).getStorage() instanceof OneDriveStorage);
    }

}
