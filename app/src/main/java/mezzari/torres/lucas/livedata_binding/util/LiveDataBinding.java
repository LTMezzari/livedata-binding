package mezzari.torres.lucas.livedata_binding.util;

import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import java.lang.reflect.Field;

import mezzari.torres.lucas.livedata_binding.BuildConfig;
import mezzari.torres.lucas.livedata_binding.annotation.BindTo;

/**
 * @author Lucas T. Mezzari
 * @since 26/07/2019
 *
 * Util class that bind View's to MutableLiveData using Reflection and BindTo annotation
 *
 * @see mezzari.torres.lucas.livedata_binding.annotation.BindTo
 **/
public final class LiveDataBinding {
    /**
     * Public method that bind MutableLiveData to annotated View's
     *
     * @param activity The Activity owner.
     * @param activityClass The Activity class.
     */
    public static void bindFields(AppCompatActivity activity, Class<?> activityClass) {
        //Calling default bind method
        bindFields(activity, activity, activityClass);
    }

    /**
     * Public method that bind MutableLiveData to annotated View's
     *
     * @param owner The Observer owner of the LiveData's
     * @param object The Object that owns the views.
     * @param objectClass The Object class.
     */
    public static void bindFields(LifecycleOwner owner, Object object, Class<?> objectClass) {
        //Checking if the object is assignable from the parameter class
        if (!objectClass.isAssignableFrom(object.getClass())) {
            //Throw exception informing that the object does not belong to the class
            throw new IllegalArgumentException(
                    String.format("The object is not a %s", objectClass.getSimpleName())
            );
        }
        //Get declared fields
        Field[] declaredFields = objectClass.getDeclaredFields();
        //Loops through fields
        for (Field field : declaredFields) {
            //Get the BindTo annotation
            BindTo bindTo = field.getAnnotation(BindTo.class);
            //Check if the field is valid to be bind
            if (!isFieldValid(field, bindTo)) {
                //Log that the field is not valid
                log(String.format("%s is not valid", field.getName()));
                //Continues forward in the loop
                continue;
            }
            //Handle the binding
            handleBinding(field, bindTo, owner, object, objectClass);
        }
    }

    /**
     * Helper method that validate if a given field is valid to be bind
     *
     * @param field The field to be validated
     * @param bindTo The field's BindTo annotation
     * @return If the field is valid (true) or not (false)
     */
    private static boolean isFieldValid(Field field, BindTo bindTo) {
        if (bindTo == null) {
            //Return invalid if the field does not have a BindTo annotation
            return false;
        } else if (bindTo.value().isEmpty()) {
            //Throw a exception informing that the annotation is invalid
            throw new IllegalArgumentException("BindTo annotation requires a valid expression");
        } else if (!View.class.isAssignableFrom(field.getType())) {
            //Throw a exception informing that the field is not a view
            throw new IllegalArgumentException(
                    String.format("%s is not a view", field.getName())
            );
        }
        //Return that the field is valid
        return true;
    }

    /**
     * Do the binding
     *
     * @param field The Field to be bind
     * @param bindTo The Field BindTO annotation
     * @param owner The ObserverOwner of the bind
     * @param object The Field owner
     * @param objectClass The owner class
     */
    private static void handleBinding(Field field, BindTo bindTo, LifecycleOwner owner, Object object, Class<?> objectClass){
        //Get the field accessibility
        boolean isAccessible = field.isAccessible();
        //Set true the field accessibility
        field.setAccessible(true);
        //Try to execute the binding
        try {
            //Get the field value and cast it to a View
            View view = (View) field.get(object);
            //Get the MutableLiveData
            MutableLiveData<?> liveData = getMutableLiveData(bindTo.value(), object, objectClass);
            //Call the BindingUtils method
            BindingUtils.bindView(owner, liveData, view);
        } catch (IllegalAccessException e) {
            //Handle exception
            handleException(e);
        }
        //Set the field accessibility back to normal
        field.setAccessible(isAccessible);
    }

    /**
     * Get the MutableLiveData out of the BindTo expression
     *
     * @param expression The BindTo expression
     * @param object The starter object from were the MutableLiveData will be extracted
     * @param objectClass The class of the Object
     * @return The MutableLiveData found from the expression
     */
    private static MutableLiveData getMutableLiveData(String expression, Object object, Class<?> objectClass) {
        //Split the expression
        String[] splitExpression = expression.split("\\.");
        //Save the current object for further validations
        Object o = object;
        //Save the current class for further validations
        Class<?> oClass = objectClass;
        //Declares a name variable to hold the field names
        String name = "";
        //Loops through the split expression
        for (String fieldName : splitExpression) {
            //Save the field name
            name = fieldName;
            //Get the current object
            o = getObject(name, o, oClass);
            //Save the object class
            oClass = o.getClass();
        }
        //Check if the final object is a MutableLiveData
        if (!(o instanceof MutableLiveData)) {
            //Throw a exception notifying that the founded object is not a MutableLiveData
            throw new IllegalArgumentException(String.format("%s is not a MutableLiveData", name));
        }
        //Return the casted object
        return (MutableLiveData) o;
    }

    /**
     * Helper method to get the object with the given name
     *
     * @param name The requested object
     * @param object The field holder
     * @param objectClass The holder class
     * @return The found object with given name
     */
    private static Object getObject(String name, Object object, Class<?> objectClass) {
        try {
            //Try to find the object
            Field field = objectClass.getDeclaredField(name);
            //Save the field's accessibility
            boolean isAccessible = field.isAccessible();
            //Set the field's accessibility to true
            field.setAccessible(true);
            //Get the result object from the field
            Object result = field.get(object);
            //Set the field accessibility back to normal
            field.setAccessible(isAccessible);
            //Return the result object
            return result;
        } catch (IllegalAccessException e) {
            //Handle the exception
            handleException(e);
        } catch (NoSuchFieldException e) {
            //Throw a exception informing that no field was found with the given name
            throw new IllegalArgumentException(
                    String.format("%s not found in %s", name, objectClass.getSimpleName())
            );
        }
        //Throw a exception informing that something went wrong during the bind
        throw new IllegalArgumentException(
                String.format("Something went wrong while searching for %s in %s",
                        name, objectClass.getSimpleName())
        );
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
            Log.d(BindingUtils.class.getSimpleName(), message);
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
