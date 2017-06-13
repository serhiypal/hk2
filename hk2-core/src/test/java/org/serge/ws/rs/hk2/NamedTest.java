package org.serge.ws.rs.hk2;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.NamedImpl;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Test;
import org.serge.ws.rs.hk2.named.LowerCaseService;
import org.serge.ws.rs.hk2.named.StringService;
import org.serge.ws.rs.hk2.named.TextService;
import org.serge.ws.rs.hk2.named.UpperCaseService;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class NamedTest {

    @Test
    public void named() {
        ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("hk2");
        ServiceLocatorUtilities.addClasses(serviceLocator, LowerCaseService.class, UpperCaseService.class, TextService.class);
        TextService service = serviceLocator.getService(TextService.class);
        service.print("uppercase"); // prints UPPERCASE
        assertThat(service.getStringService(), instanceOf(UpperCaseService.class));

        serviceLocator.shutdown();
        ServiceLocatorFactory.getInstance().destroy(serviceLocator);
    }

    @Test
    public void namedLiteral() {
        ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("hk2");
        ServiceLocatorUtilities.addClasses(serviceLocator, LowerCaseService.class, UpperCaseService.class, TextService.class);
        StringService service = serviceLocator.getService(StringService.class, new NamedImpl("lower"));
        assertThat(service.apply("LOWERCASE"), is("lowercase")); // prints lowercase

        serviceLocator.shutdown();
        ServiceLocatorFactory.getInstance().destroy(serviceLocator);
    }

}
