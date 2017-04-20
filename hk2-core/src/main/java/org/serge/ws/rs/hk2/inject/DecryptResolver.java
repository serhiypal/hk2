package org.serge.ws.rs.hk2.inject;

import javax.inject.Inject;
import java.util.Base64;

import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.hk2.api.ServiceLocator;

public class DecryptResolver implements InjectionResolver<Decrypt> {

    private ServiceLocator serviceLocator;

    @Inject
    public DecryptResolver(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> root) {
        Object o = serviceLocator.getService(serviceLocator.getInjecteeDescriptor(injectee), root, injectee);
        if (o instanceof String) {
            Decrypt decrypt = injectee.getParent().getAnnotation(Decrypt.class);
            if (decrypt != null) {
                switch (decrypt.value()) {
                case "base64": return new String(Base64.getDecoder().decode((String) o));
                }
            }
        }
        return o;
    }

    @Override
    public boolean isConstructorParameterIndicator() {
        return false;
    }

    @Override
    public boolean isMethodParameterIndicator() {
        return false;
    }
}
