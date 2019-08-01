package mezzari.torres.lucas.livedata_binding.source;

import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

/**
 * @author Lucas T. Mezzari
 * @since 31/07/2019
 **/
public interface ViewBinder {
    <T>void bindView(LifecycleOwner owner, MutableLiveData<T> liveData, View view);
}
