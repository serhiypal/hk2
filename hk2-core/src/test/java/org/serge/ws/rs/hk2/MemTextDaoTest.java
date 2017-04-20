package org.serge.ws.rs.hk2;

import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;

import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.api.Descriptor;
import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.PerLookup;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Test;
import org.serge.ws.rs.hk2.dto.Text;
import org.serge.ws.rs.hk2.mem.MemTextDao;
import org.serge.ws.rs.hk2.service.TextServiceImpl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
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

}
