package com.mayo.server.common.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order
public class ControllerPointCut {

    @Pointcut("execution(* com.mayo.server.*.adapter.*Controller..*(..))")
    public void allController() {
    }

}
