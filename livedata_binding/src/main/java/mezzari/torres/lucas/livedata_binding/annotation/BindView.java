package mezzari.torres.lucas.livedata_binding.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Lucas T. Mezzari
 * @since 31/07/2019
 *
 * Annotation that should be used to mark the Methods that will handle the View binds
 * It will be processed by a {@link mezzari.torres.lucas.livedata_binding.source.generic.AnnotatedBinder}
 *
 * @see mezzari.torres.lucas.livedata_binding.source.generic.AnnotatedBinder
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindView {
    //The view class that will be handled
    Class<?> value();
}
