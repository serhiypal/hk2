package org.serge.ws.rs.hk2;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Test;
import org.serge.ws.rs.hk2.mem.MemTextDao;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class MemTextDaoTest {

    @Test
    public void testHk2Locator() {
        ServiceLocator serviceLocator = ServiceLocatorUtilities.createAndPopulateServiceLocator();
        MemTextDao textDao = serviceLocator.getService(MemTextDao.class);
        assertThat(textDao, notNullValue());
    }
}
