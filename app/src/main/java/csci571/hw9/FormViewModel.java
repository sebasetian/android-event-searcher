package csci571.hw9;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.Observable;
import android.databinding.Observable.OnPropertyChangedCallback;
import android.databinding.ObservableField;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import csci571.hw9.Model.WebServices;
import csci571.hw9.Model.FormField;
import java.util.List;

public class FormViewModel extends ViewModel {
    private FormField form;
    private MutableLiveData<FormField> formPassed;
    public ObservableField<String> keywordForAutoComplete;
    private WebServices webService = new WebServices();
    private ArrayAdapter<String> autoCompAdapter;
    public void init() {
        Log.d("formViewModel", "init");
        form = new FormField();
        formPassed = new MutableLiveData<>();
        keywordForAutoComplete = new ObservableField<>();
        keywordForAutoComplete.set("laker");
    }

    public ArrayAdapter<String> getAutoCompAdapter() {
        return autoCompAdapter;
    }
    public void reflashAutoComplete() {
        autoCompAdapter.clear();
        autoCompAdapter.addAll(getAutoComplete());
        autoCompAdapter.notifyDataSetChanged();
    }
    public void setAutoCompAdapter(ArrayAdapter<String> autoCompAdapter) {
        this.autoCompAdapter = autoCompAdapter;
    }
    public List<String> getAutoComplete() {
        Log.d("formViewModel", "getAutoComplete");
        return webService.autocomplete(keywordForAutoComplete.get());
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
