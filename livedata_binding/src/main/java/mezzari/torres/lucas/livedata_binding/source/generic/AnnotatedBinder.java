package mezzari.torres.lucas.livedata_binding.source.generic;

import android.util.Log;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import mezzari.torres.lucas.livedata_binding.BuildConfig;
import mezzari.torres.lucas.livedata_binding.annotation.BindView;
import mezzari.torres.lucas.livedata_binding.source.ViewBinder;

/**
 * @author Lucas T. Mezzari
 * @since 31/07/2019
 *
 * Abstract class to facilitate the LiveData binding
 * ViewBinders should extend this class and annotated their methods with {@link BindView}
 * Methods annotated with it will be called whenever a View class matches with the parameter
 *
 * @see mezzari.torres.lucas.livedata_binding.source.ViewBinder
 * @see mezzari.torres.lucas.livedata_binding.annotation.BindView
 **/
public abstract class AnnotatedBinder implements ViewBinder {

    //Declares a hash map of binder methods
    private HashMap<Class<?>, Method> binderMethods;

    @Override
    @CallSuper
    public <T> void bindView(LifecycleOwner owner, MutableLiveData<T> liveData, View view) {
        //Check if the binderMethods variable was initialized
        if (binderMethods == null) {
            //Extract the binder methods
            binderMethods = extractMethods();
        }
        //Get the method at the view class
        Method method = binderMethods.get(view.getClass());
        //Check if a method was found
        if (method != null) {
            //Get the method accessibility
            boolean isAccessible = method.isAccessible();
            //Set the method accessible
            method.setAccessible(true);
            //Try to invoke it
            try {
                //Get the total of parameters of the method
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 3) {
                    //Throw a exception informing that the method is invalid
                    throw new IllegalArgumentException(
                            String.format("%s should contain three parameters", method.getName())
                    );
                } else if (!View.class.isAssignableFrom(parameterTypes[2])) {
                    //Throw a exception informing that the last parameter should be a View child
                    throw new IllegalArgumentException(
                            String.format("The last parameter of a binder method should be a child object of View (%s)", method.getName())
                    );
                }
                //Invoke the method
                method.invoke(this, owner, liveData, parameterTypes[2].cast(view));
            } catch (IllegalAccessException e) {
                //Handle the exception
                handleException(e);
            } catch (InvocationTargetException e) {
                //Log a message
                log(String.format("The invocation of the method %s was not successful", method.getName()));
                //Handle the exception
                handleException(e);
            }
            //Set the method accessibility back to normal
            method.setAccessible(isAccessible);
        } else {
            //Throw a exception when view is annotated but no binder method was found
            throw new IllegalArgumentException(
                    String.format("Missing %s as BindView parameter.", view.getClass().getName())
            );
        }
    }

    /**
     * Method that extract all methods with BindView annotation.
     * The methods will be used later when a View class matches it's annotation parameter
     *
     * @return A HashMap of Class and Methods.
     */
    private HashMap<Class<?>, Method> extractMethods() {
        //Initializes the map
        HashMap<Class<?>, Method> binderMethods = new HashMap<>();
        //Get the declared methods
        Method[] declaredMethods = getClass().getDeclaredMethods();
        //Loops through the declared methods
        for (Method method : declaredMethods) {
            //Get the BindView annotation
            BindView bindView = method.getAnnotation(BindView.class);
            //Check if the annotation was found
            if (bindView == null) {
                //continue to the next method
                continue;
            } else if (!View.class.isAssignableFrom(bindView.value())) {
                //If the annotation value is not a View, throw a exception
                throw new IllegalArgumentException("Methods assigned with BindView annotation" +
                        " should have a Child class of View");
            }
            //Put the method at the annotation value
            binderMethods.put(bindView.value(), method);
        }
        //Return the populated map
        return binderMethods;
    }

    /**
     * Helper method to log only when Debugging
     *
     * @param message The message to be logged
     */
    private static void log(String message) {
        //Check if it is debug
        if (BuildConfig.DEBUG) {
            //Log the given message
            Log.d(AnnotatedBinder.class.getSimpleName(), message);
        }
    }


    /**
     * Helper method to print the exception stack trace only when Debugging
     *
     * @param e The message to be logged
     */
    private static void handleException(Exception e) {
        //Check if it is debug
        if (BuildConfig.DEBUG) {
            //Print the stack trace
            e.printStackTrace();
        }
    }
}
