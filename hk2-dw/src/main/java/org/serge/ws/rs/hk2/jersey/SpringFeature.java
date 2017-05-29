package org.serge.ws.rs.hk2.jersey;

import javax.inject.Inject;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.jvnet.hk2.spring.bridge.api.SpringBridge;
import org.jvnet.hk2.spring.bridge.api.SpringIntoHK2Bridge;
import org.jvnet.hk2.spring.bridge.api.SpringScopeImpl;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import lombok.extern.slf4j.Slf4j;

/**
 * Jersey Spring Feature to register Spring context within Jersey/HK2 context so spring can refer HK2 services
 * and HK2 - Spring beans
 */
@Slf4j
@Provider
public class SpringFeature implements Feature {

    public static final String PACKAGES_PROPERTY = SpringFeature.class + ".packages";

    private final AnnotationConfigApplicationContext applicationContext;

    private final ServiceLocator serviceLocator;

    @Inject
    public SpringFeature(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
        applicationContext = new AnnotationConfigApplicationContext();
    }

    /**
     * Configuring Spring's application context with specified packages in Jersey configuration
     * @param context feature context to get additional information for initialization
     * @return true if configured, otherwise - false, no packages are provided
     */
    @Override
    public boolean configure(FeatureContext context) {
        Object packages = context.getConfiguration().getProperty(PACKAGES_PROPERTY);
        if (packages == null) {
            log.info("{} is not configured, nothing to initialize, skipping...", PACKAGES_PROPERTY);
            return false;
        }
        String[] scanPackages = new String[0];
        if (packages instanceof Collection<?>) {
            scanPackages = ((Collection<?>) packages).stream()
                                                     .filter(Objects::nonNull)
                                                     .map(Object::toString)
                                                     .toArray(String[]::new);
        } else if (packages instanceof String) {
            scanPackages = Arrays.stream(((String) packages).split(",")).filter(p -> !p.isEmpty()).toArray(String[]::new);
        }
        log.info("{} configured for Spring", Arrays.toString(scanPackages));
        applicationContext.scan(scanPackages);
        registerHK2InSpring(applicationContext, serviceLocator);
        applicationContext.refresh();
        applicationContext.start();
        return true;
    }

    /**
     * Adding a magic to bridge Spring and HK2 bidirectionally.
     * @param applicationContext to register in HK2
     * @param locator to register in Spring
     */
    private static void registerHK2InSpring(GenericApplicationContext applicationContext, ServiceLocator locator) {
        SpringBridge.getSpringBridge().initializeSpringBridge(locator);
        SpringIntoHK2Bridge spring2HK2 = locator.getService(SpringIntoHK2Bridge.class);
        spring2HK2.bridgeSpringBeanFactory(applicationContext);

        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        SpringScopeImpl springScope = new SpringScopeImpl();
        springScope.setServiceLocator(locator);
        beanFactory.registerScope("hk2", springScope);

        locator.getAllServiceHandles(BuilderHelper.createTokenizedFilter(String.format(";qualifier=%s", Spring.class.getName())))
               .forEach(serviceHandle -> applicationContext.registerBeanDefinition(
                       serviceHandle.getActiveDescriptor().getContractTypes().iterator().next().getTypeName(),
                       BeanDefinitionBuilder.genericBeanDefinition(serviceHandle.getActiveDescriptor().getImplementationClass())
                                            .setScope("hk2")
                                            .getBeanDefinition())
        );
    }
}