package org.siemac.metamac.srm.web.server.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class ActionHandlerLoggerAspect {

    private static Logger logger = LoggerFactory.getLogger(ActionHandlerLoggerAspect.class);

    @AfterThrowing(pointcut = "execution(* com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler.*(..))", throwing = "ex")
    public void logException(JoinPoint jp, Throwable ex) {
        logger = LoggerFactory.getLogger(jp.getTarget().getClass());
        logger.error("Exception ", ex);
    }

}
