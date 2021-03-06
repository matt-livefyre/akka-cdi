package com.livefyre.akka.cdi;

import javax.enterprise.util.Nonbinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, FIELD, PARAMETER})
public @interface UsingActorSystem {

    @Nonbinding
    String name();

    String AKKA_DEFAULT = "akka-cdi";
}
