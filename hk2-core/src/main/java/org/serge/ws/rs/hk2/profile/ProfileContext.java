package org.serge.ws.rs.hk2.profile;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.api.Context;
import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.glassfish.hk2.utilities.ContextualInput;
import org.glassfish.hk2.utilities.cache.Cache;
import org.jvnet.hk2.annotations.Service;

@Service
@Singleton
public class ProfileContext implements Context<Profile> {

    private final Set<String> profiles;

    private final ServiceLocator serviceLocator;

    private Cache<ContextualInput<Object>, Object> cache = new Cache<>(
        ci -> {
            final ActiveDescriptor<Object> activeDescriptor = ci.getDescriptor();

            final Object cachedVal = activeDescriptor.getCache();
            if (cachedVal != null) {
                return cachedVal;
            }

            final Object createdVal = activeDescriptor.create(ci.getRoot());
            activeDescriptor.setCache(createdVal);
            return createdVal;
        },
        ci -> {
            throw new MultiException(new IllegalStateException(
                    "A circular dependency involving Singleton service " + ci.getDescriptor().getImplementation() +
                            " was found.  Full descriptor is " + ci.getDescriptor()));
        }
    );

    @Inject
    public ProfileContext(ServiceLocator serviceLocator) {
        String profilesProperty = System.getProperty("profiles");
        if (profilesProperty != null && !profilesProperty.isEmpty()) {
            profiles = Arrays.stream(profilesProperty.split(",")).collect(Collectors.toSet());
        } else {
            profiles = Collections.emptySet();
        }
        this.serviceLocator = serviceLocator;
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return Profile.class;
    }

    @Override
    public <U> U findOrCreate(ActiveDescriptor<U> activeDescriptor, ServiceHandle<?> root) {
        if (isDescriptorProfileActive(activeDescriptor)) {
            return (U) cache.compute(new ContextualInput<>((ActiveDescriptor<Object>) activeDescriptor, root));
        }
        Set<ActiveDescriptor<?>> descriptors =
                activeDescriptor.getContractTypes()
                                .stream()
                                .map(contract -> serviceLocator.getDescriptors(BuilderHelper.createContractFilter(contract.getTypeName())))
                                .flatMap(Collection::stream)
                                .filter(descriptor -> !activeDescriptor.equals(descriptor))
                                .filter(this::isDescriptorProfileActive)
                                .collect(Collectors.toSet());
        if (descriptors.size() > 1) {
            throw new MultiException(new IllegalArgumentException("More than one service exists for the same profile"));
        }
        if (descriptors.size() == 0) {
            throw new MultiException(new IllegalArgumentException("At least one service must be active for the profile"));
        }
        ActiveDescriptor<?> descriptor = descriptors.iterator().next();
        ServiceHandle<?> serviceHandle = serviceLocator.getServiceHandle(descriptor);
        return (U) cache.compute(new ContextualInput<>((ActiveDescriptor<Object>) descriptor, serviceHandle));
    }

    @Override
    public boolean containsKey(ActiveDescriptor<?> descriptor) {
        return cache.containsKey(new ContextualInput<>((ActiveDescriptor<Object>) descriptor, null));
    }

    @Override
    public void destroyOne(ActiveDescriptor<?> descriptor) {
        cache.compute(new ContextualInput<>((ActiveDescriptor<Object>) descriptor, null));
        Object value = descriptor.getCache();
        descriptor.releaseCache();
        if (value != null) {
            try {
                ((ActiveDescriptor<Object>) descriptor).dispose(value);
            } catch (Exception e) {
                throw new MultiException(e);
            }
        }
    }

    @Override
    public boolean supportsNullCreation() {
        return false;
    }

    @Override
    public boolean isActive() {
        return !profiles.isEmpty();
    }

    @Override
    public void shutdown() {
        serviceLocator.getAllServiceHandles(BuilderHelper.allFilter())
                      .stream()
                      .filter(h -> h.getActiveDescriptor().getScope().equals(Profile.class.getName()))
                      .filter(ServiceHandle::isActive)
                      .forEach(h -> this.destroyOne(h.getActiveDescriptor()));
        cache.clear();
    }

    private boolean isDescriptorProfileActive(ActiveDescriptor<?> descriptor) {
        return Optional.ofNullable(descriptor.getImplementationClass().getDeclaredAnnotation(Profile.class))
                       .map(Profile::value)
                       .filter(values -> Arrays.stream(values).anyMatch(profiles::contains))
                       .isPresent();
    }

}
