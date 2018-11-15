package csci571.hw9;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import csci571.hw9.Model.WebServices;
import csci571.hw9.Model.FormField;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import java.util.List;

public class FormViewModel extends ViewModel {
    private FormField form;
    private MutableLiveData<FormField> formFields;
    private WebServices webService = new WebServices();
    private ArrayAdapter<String> autoCompAdapter;
    private CompositeDisposable mDisposables = new CompositeDisposable();

    public ObservableField<String> keywordForAutoComplete;
    public void init() {
        Log.d("formViewModel", "init");
        form = new FormField();
        formFields = new MutableLiveData<>();
        keywordForAutoComplete = new ObservableField<>();
        keywordForAutoComplete.set("");
        mDisposables.add(webService.autoCompleteSource.subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> list) throws Exception {
                refreshAutoComplete(list);
            }
        }));
    }

    public ArrayAdapter<String> getAutoCompAdapter() {
        return autoCompAdapter;
    }
    private void refreshAutoComplete(List<String> list) {
        autoCompAdapter.clear();
        autoCompAdapter.addAll(list);
        autoCompAdapter.notifyDataSetChanged();
    }
    public void setAutoCompAdapter(ArrayAdapter<String> autoCompAdapter) {
        this.autoCompAdapter = autoCompAdapter;
    }
    public void getAutoComplete() {
        webService.autocomplete(keywordForAutoComplete.get());
    }
    public void onSubmit() {
        if(!form.isValid()) {
            formFields.setValue(form);
        }
    }
    public MutableLiveData<FormField> getSubmit() {
        return formFields;
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
