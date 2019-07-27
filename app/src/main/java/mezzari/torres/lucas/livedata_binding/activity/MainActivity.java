package mezzari.torres.lucas.livedata_binding.activity;

import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

import mezzari.torres.lucas.livedata_binding.R;
import mezzari.torres.lucas.livedata_binding.annotation.BindTo;
import mezzari.torres.lucas.livedata_binding.annotation.LayoutReference;
import mezzari.torres.lucas.livedata_binding.generic.BaseActivity;

/**
 * @author Lucas T. Mezzari
 * @since 26/07/2019
 **/
@LayoutReference(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @BindTo("welcomeMessage")
    private TextView tvWelcomeMessage;
    @BindTo("viewModel.field")
    private EditText etField;
    @BindTo("viewModel.field")
    private TextView tvField;

    private MutableLiveData<String> welcomeMessage = new MutableLiveData<>();
    private MainActivityViewModel viewModel = new MainActivityViewModel();

    @Override
    protected void onInitComponents() {
        String message = "Hey";
        welcomeMessage.setValue(message);
        viewModel.getField().observe(this, (s) -> welcomeMessage.setValue(!s.isEmpty() ? "" : message));
    }
}
