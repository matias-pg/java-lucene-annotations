package dev.matiaspg.luceneannotations.lucene.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Sorted {
    String SORT_FIELD_SUFFIX = "--sorted";

    String value() default "";
}