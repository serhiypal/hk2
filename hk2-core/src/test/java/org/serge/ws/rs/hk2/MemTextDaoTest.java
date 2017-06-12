package org.serge.ws.rs.hk2;

import javax.inject.Singleton;
import java.util.UUID;

import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.DescriptorImpl;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Test;
import org.serge.ws.rs.hk2.dto.Text;
import org.serge.ws.rs.hk2.mem.MemTextDao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class MemTextDaoTest {

    @Test
    public void testHk2Locator() {
        ServiceLocator serviceLocator = ServiceLocatorUtilities.createAndPopulateServiceLocator();
        MemTextDao textDao = serviceLocator.getService(MemTextDao.class);
        assertThat(textDao, notNullValue());
    }

    @Test
    public void testHkFactoryCreateService() {
        ServiceLocatorFactory factory = ServiceLocatorFactory.getInstance();
        ServiceLocator locator = factory.create("testHkFactoryCreateService");
        MemTextDao dao = locator.create(MemTextDao.class);
        Text text = dao.create(new Text(UUID.randomUUID(), "TEST"));
        assertThat(text, is(notNullValue()));
        assertThat(text.getText(), is("TEST"));
    }

    @Test
    public void descriptorCreation() {
        ServiceLocatorFactory factory = ServiceLocatorFactory.getInstance();
        ServiceLocator locator = factory.create("DescriptorImpl");
        DescriptorImpl descriptor = new DescriptorImpl();
        descriptor.addAdvertisedContract(TextDao.class.getName());
        descriptor.addAdvertisedContract(MemTextDao.class.getName());
        descriptor.setImplementation(MemTextDao.class.getName());
        descriptor.setScope(Singleton.class.getName());
        DynamicConfiguration dynamic = locator.getService(DynamicConfigurationService.class).createDynamicConfiguration();
        dynamic.bind(descriptor);
        dynamic.commit();

        assertNotNull(locator.getService(TextDao.class));
    }

}
