package mezzari.torres.lucas.livedata_binding.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Lucas T. Mezzari
 * @since 26/07/2019
 *
 * Annotation that should be used to mark the Views that should be bind to a MutableLiveData
 * It will be processed by the {@link mezzari.torres.lucas.livedata_binding.source.LiveDataBinding}
 *
 * @see mezzari.torres.lucas.livedata_binding.source.LiveDataBinding
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BindTo {
    //A string expression that will lead to a MutableLiveData
    String value();
}
