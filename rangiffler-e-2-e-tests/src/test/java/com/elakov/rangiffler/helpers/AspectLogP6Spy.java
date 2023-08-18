package com.elakov.rangiffler.helpers;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;

@Aspect
public class AspectLogP6Spy {

    @Pointcut("@annotation(com.elakov.rangiffler.jupiter.annotation.LogP6Spy)")
    public void annotated() {}

    @Before("annotated()")
    public void before() {
        MDC.put("Enable", "true");
    }

    @After("annotated()")
    public void after() {
        MDC.remove("Enable");
    }
}