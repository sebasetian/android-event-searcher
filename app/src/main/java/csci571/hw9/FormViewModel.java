package csci571.hw9;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.BindingAdapter;
import android.widget.EditText;
import csci571.hw9.Model.FormField;

public class FormViewModel extends ViewModel {
    private FormField form;
    private MutableLiveData<FormField> formPassed;

    public void init() {
        form = new FormField();

    }
    public void onSubmit() {
        if(!form.isValid()) {
            formPassed.setValue(form);
        }
    }
    public MutableLiveData<FormField> getSubmit() {
        return formPassed;
    }
    @BindingAdapter("error")
    public static void setError(EditText editText, Object strOrResId) {
        if (strOrResId instanceof Integer) {
            editText.setError(editText.getContext().getString((Integer) strOrResId));
        } else {
            editText.setError((String) strOrResId);
        }

    }

}
