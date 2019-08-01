package mezzari.torres.lucas.livedata_binding.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import mezzari.torres.lucas.livedata_binding.annotation.BindView;
import mezzari.torres.lucas.livedata_binding.source.generic.AnnotatedBinder;

/**
 * @author Lucas T. Mezzari
 * @since 26/07/2019
 **/
public final class BindingUtils extends AnnotatedBinder {
    @BindView(AppCompatTextView.class)
    private void bindTextView(LifecycleOwner owner, MutableLiveData<String> liveData, TextView textView) {
        liveData.observe(owner, (value) -> {
            if (!textView.getText().equals(value)) {
                textView.setText(value);
            }
        });
    }

    @BindView(AppCompatEditText.class)
    private void bindEditText(LifecycleOwner owner, MutableLiveData<String> liveData, EditText editText) {
        liveData.observe(owner, (value) -> {
            if (!editText.getText().toString().equals(value)) {
                editText.setText(value);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(liveData.getValue())) {
                    liveData.setValue(editable.toString());
                }
            }
        });
    }
}
