package mezzari.torres.lucas.livedata_binding.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

/**
 * @author Lucas T. Mezzari
 * @since 26/07/2019
 **/
public final class BindingUtils {
    static <T>void bindView(LifecycleOwner owner, MutableLiveData<T> liveData, View view) {
        if (view instanceof EditText) {
            bindEditText(owner, (MutableLiveData<String>) liveData, (EditText) view);
        } else if (view instanceof TextView) {
            bindTextView(owner, (MutableLiveData<String>) liveData, (TextView) view);
        }
    }

    private static void bindTextView(LifecycleOwner owner, MutableLiveData<String> liveData, TextView textView) {
        liveData.observe(owner, (value) -> {
            if (!textView.getText().equals(value)) {
                textView.setText(value);
            }
        });
    }

    private static void bindEditText(LifecycleOwner owner, MutableLiveData<String> liveData, EditText editText) {
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
