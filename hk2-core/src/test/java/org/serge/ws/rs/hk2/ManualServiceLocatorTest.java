package org.serge.ws.rs.hk2;

import javax.inject.Singleton;
import java.util.Base64;

import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.glassfish.hk2.utilities.DescriptorImpl;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Test;
import org.serge.ws.rs.hk2.dto.Text;
import org.serge.ws.rs.hk2.inject.DecryptResolver;
import org.serge.ws.rs.hk2.inject.Secret;
import org.serge.ws.rs.hk2.inject.StringSecret;
import org.serge.ws.rs.hk2.mem.MemTextDao;
import org.serge.ws.rs.hk2.service.TextServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ManualServiceLocatorTest {

    @Test
    public void createServices() {
        ServiceLocatorFactory factory = ServiceLocatorFactory.getInstance();
        ServiceLocator serviceLocator = factory.create("hk2");
        DynamicConfigurationService service = serviceLocator.getService(DynamicConfigurationService.class);

        DynamicConfiguration dynamicConfiguration = service.createDynamicConfiguration();

        DescriptorImpl descriptor = new DescriptorImpl();
        descriptor.addAdvertisedContract(TextDao.class.getName());
        descriptor.addAdvertisedContract(MemTextDao.class.getName());
        descriptor.setScope(Singleton.class.getName());
        descriptor.setImplementation(MemTextDao.class.getName());

        dynamicConfiguration.bind(descriptor);
        dynamicConfiguration.bind(BuilderHelper.link(TextServiceImpl.class).to(TextService.class).in(Singleton.class).build());
        dynamicConfiguration.commit();

        TextService textService = serviceLocator.getService(TextService.class);
        assertNotNull(textService);

        Text text = textService.newText("TEST");
        assertNotNull(text);

        serviceLocator.shutdown();
        factory.destroy(serviceLocator);
    }

    @Test
    public void decryptInjectionResolver() {
        ServiceLocatorFactory factory = ServiceLocatorFactory.getInstance();
        ServiceLocator serviceLocator = factory.create("hk2");

        DynamicConfigurationService service = serviceLocator.getService(DynamicConfigurationService.class);
        DynamicConfiguration dynamicConfiguration = service.createDynamicConfiguration();

        dynamicConfiguration.bind(BuilderHelper.link(DecryptResolver.class).to(InjectionResolver.class).in(Singleton.class).build());
        dynamicConfiguration.bind(BuilderHelper.createConstantDescriptor(
                new String(Base64.getEncoder().encode("DecryptResolver".getBytes())),
                "base64_value",
                String.class));
        dynamicConfiguration.bind(BuilderHelper.link(StringSecret.class).to(Secret.class).in(Singleton.class).build());
        dynamicConfiguration.commit();

        String base64Value = serviceLocator.getService(String.class, "base64_value");
        assertNotNull(base64Value);
        assertNotEquals("DecryptResolver", base64Value);
        Secret secret = serviceLocator.getService(StringSecret.class);
        assertEquals("DecryptResolver", secret.getValue());

        serviceLocator.shutdown();
        factory.destroy(serviceLocator);
    }

    @Test
    public void manualCreateInitInjectDestroy() {
        ServiceLocatorFactory factory = ServiceLocatorFactory.getInstance();
        ServiceLocator locator = factory.create("hk2");

        ServiceLocatorUtilities.addClasses(locator, MemTextDao.class);

        TextService textService = locator.create(TextServiceImpl.class);
        locator.postConstruct(textService);
        locator.inject(locator.getService(TextDao.class));

        Text text = textService.newText("TEST");
        assertNotNull(text);
        assertNull(locator.getService(TextService.class));

        locator.preDestroy(textService);

        locator.shutdown();
        factory.destroy(locator);
    }

}
