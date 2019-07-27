package mezzari.torres.lucas.livedata_binding.activity;

import androidx.lifecycle.MutableLiveData;

/**
 * @author Lucas T. Mezzari
 * @since 26/07/2019
 **/
public class MainActivityViewModel {
    private MutableLiveData<String> field = new MutableLiveData<>();

    public MutableLiveData<String> getField() {
        return field;
    }
}
