//TODO: Разобраться! Для настройки фильтра p6spy + logback
//package com.elakov.rangiffler.helper;
//
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.slf4j.MDC;
//
//@Aspect
//public class AspectLogP6Spy {
//
//    @Pointcut("@annotation(com.elakov.rangiffler.jupiter.annotation.meta.LogP6Spy)")
//    public void annotated() {}
//
//    @Before("annotated()")
//    public void before() {
//        MDC.put("Enable", "true");
//    }
//
//    @After("annotated()")
//    public void after() {
//        MDC.remove("Enable");
//    }
//}