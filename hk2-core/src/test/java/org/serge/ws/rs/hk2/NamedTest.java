package org.serge.ws.rs.hk2;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Test;
import org.serge.ws.rs.hk2.named.LowerCaseService;
import org.serge.ws.rs.hk2.named.TextService;
import org.serge.ws.rs.hk2.named.UpperCaseService;

public class NamedTest {

    @Test
    public void named() {
        ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("hk2");
        ServiceLocatorUtilities.addClasses(serviceLocator, LowerCaseService.class, UpperCaseService.class, TextService.class);
        TextService service = serviceLocator.getService(TextService.class);
        service.print("uppercase"); // prints UPPERCASE

        serviceLocator.shutdown();
        ServiceLocatorFactory.getInstance().destroy(serviceLocator);
    }

}
