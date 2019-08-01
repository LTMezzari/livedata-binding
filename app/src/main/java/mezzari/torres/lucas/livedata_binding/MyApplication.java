package mezzari.torres.lucas.livedata_binding;

import android.app.Application;

import mezzari.torres.lucas.livedata_binding.source.LiveDataBinding;
import mezzari.torres.lucas.livedata_binding.util.BindingUtils;

/**
 * @author Lucas T. Mezzari
 * @since 31/07/2019
 **/
public final class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LiveDataBinding.setViewBinder(new BindingUtils());
    }
}
