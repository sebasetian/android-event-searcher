package csci571.hw9;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.BindingAdapter;
import android.databinding.Observable;
import android.databinding.Observable.OnPropertyChangedCallback;
import android.databinding.ObservableField;
import android.widget.EditText;
import csci571.hw9.Model.WebServices;
import csci571.hw9.Model.FormField;

public class FormViewModel extends ViewModel {
    private FormField form;
    private MutableLiveData<FormField> formPassed;
    public ObservableField<String> keywordForAutoComplete;
    public String[] autoCompleteTitles;
    private WebServices webService = new WebServices();
    public void init() {
        form = new FormField();
        keywordForAutoComplete.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                autoCompleteTitles = webService.autocomplete(keywordForAutoComplete.get());
            }
        });
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
