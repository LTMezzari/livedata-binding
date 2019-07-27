package mezzari.torres.lucas.livedata_binding.generic;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import lucas.torres.viewloader.loader.ViewLoader;
import mezzari.torres.lucas.livedata_binding.R;
import mezzari.torres.lucas.livedata_binding.annotation.LayoutReference;
import mezzari.torres.lucas.livedata_binding.util.LiveDataBinding;

/**
 * @author Lucas T. Mezzari
 * @since 26/07/2019
 **/
public abstract class BaseActivity extends AppCompatActivity {

    private View rootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutReference layoutReference = getClass().getAnnotation(LayoutReference.class);
        if (layoutReference == null) {
            throw new RuntimeException("Missing LayoutReference annotation");
        }
        setContentView(layoutReference.value());
        rootView = getWindow().getDecorView().getRootView();
        onLoadComponents();
        onBindComponents();
        onInitComponents();
    }

    protected void onLoadComponents() {
        //Load the activity components
        ViewLoader.with(R.id.class).from(rootView).into(this).load();
    }

    protected void onBindComponents() {
        //Bind the activity components
        LiveDataBinding.bindFields(this, getClass());
    }

    protected abstract void onInitComponents();
}
