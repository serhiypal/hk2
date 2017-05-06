package org.serge.ws.rs.hk2.aop;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.InterceptionService;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.jvnet.hk2.annotations.Service;

@Service
public class TimedInterceptionService implements InterceptionService {

    private static final TimedMethodInterceptor interceptor = new TimedMethodInterceptor();

    @Override
    public Filter getDescriptorFilter() {
        return BuilderHelper.allFilter();
    }

    @Override
    public List<MethodInterceptor> getMethodInterceptors(Method method) {
        return method.isAnnotationPresent(Timed.class) ? Collections.singletonList(interceptor) : null;
    }

    @Override
    public List<ConstructorInterceptor> getConstructorInterceptors(Constructor<?> constructor) {
        return constructor.isAnnotationPresent(Timed.class) ? Collections.singletonList(interceptor) : null;
    }

}
