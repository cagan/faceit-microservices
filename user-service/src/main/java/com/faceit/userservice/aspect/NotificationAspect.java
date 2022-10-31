package com.faceit.userservice.aspect;

import com.faceit.userservice.domain.event.UserEntityUpdated;
import com.faceit.userservice.error.UserAlreadyExistsException;
import com.faceit.userservice.error.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@AllArgsConstructor
public class NotificationAspect {
    private final ApplicationEventPublisher eventPublisher;

    @Pointcut("@annotation(notifyUserChange)")
    public void notificationPointcut(NotifyUserChange notifyUserChange) {}

    @AfterThrowing(pointcut = "notificationPointcut(notifyUserChange)", throwing = "e", argNames = "joinPoint,e,notifyUserChange")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e, NotifyUserChange notifyUserChange) {
        Object[] args = joinPoint.getArgs();
        if (e instanceof UserAlreadyExistsException) {
            log.info("Attempted create has been failed: [User: {}] already exists", args[0]);
        }
        if (e instanceof UserNotFoundException) {
            log.info("Attempted update/delete has been failed: [User: {}] not found", args[0]);
        }
    }

    @AfterReturning(value = "notificationPointcut(notifyUserChange)", argNames = "joinPoint,notifyUserChange")
    public void publishUserEvents(JoinPoint joinPoint, NotifyUserChange notifyUserChange) {
        log.info("Publishing UserEntityUpdated event");

        if (notifyUserChange.type().equals(NotificationOnType.UPDATE)) {
            eventPublisher.publishEvent(new UserEntityUpdated(this, joinPoint.getArgs()[1], notifyUserChange.type()));
        } else {
            eventPublisher.publishEvent(new UserEntityUpdated(this, joinPoint.getArgs()[0], notifyUserChange.type()));
        }

    }
}
