package pl.lambda.foundationminecraft.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface LDeprecated
{
    boolean willBeDeleted() default false;
    String newUsage();
}
