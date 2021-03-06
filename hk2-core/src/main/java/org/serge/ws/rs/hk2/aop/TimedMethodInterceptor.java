package org.serge.ws.rs.hk2.aop;

import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.ConstructorInvocation;
import org.aopalliance.intercept.Joinpoint;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class TimedMethodInterceptor implements MethodInterceptor, ConstructorInterceptor {

    @Override
    public Object construct(ConstructorInvocation invocation) throws Throwable {
        return time(invocation, invocation.getConstructor().getName());
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        return time(methodInvocation, methodInvocation.getMethod().getName());
    }

    private Object time(Joinpoint joinpoint, String tag) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return joinpoint.proceed();
        } finally {
            System.out.println(joinpoint.getThis().getClass().getName()
                                       + "."
                                       + tag
                                       + "="
                                       + (System.currentTimeMillis() - start));
        }
    }

}
