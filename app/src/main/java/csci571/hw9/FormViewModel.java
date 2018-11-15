package csci571.hw9;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import csci571.hw9.Model.WebServices;
import csci571.hw9.Model.FormField;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormViewModel extends BaseObservable {
    public FormField form;
    public ObservableBoolean isLocationHere  = new ObservableBoolean();
    private MutableLiveData<FormField> formFields;
    private WebServices webService = new WebServices();
    private ArrayAdapter<String> autoCompAdapter;
    private CompositeDisposable mDisposables = new CompositeDisposable();
    public final List<String> UNIT_LIST = new ArrayList<>(Arrays.asList("Miles","Kilometers"));
    public final List<String> CATEGORY_LIST = new ArrayList<>(Arrays.asList("All","Music","Sports","Arts & Theatre","Film","Miscellaneous"));
    public void init() {
        Log.d("formViewModel", "init");
        form = new FormField();
        formFields = new MutableLiveData<>();
        initFormField();
        mDisposables.add(webService.autoCompleteSource.subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> list) throws Exception {
                refreshAutoComplete(list);
            }
        }));
    }
    private void initFormField() {
        form.keyword = new ObservableField<>();
        form.keyword.set("");
        form.categoryIdx = new ObservableInt();
        form.categoryIdx.set(0);
        form.distance = new ObservableField<>();
        form.distanceUnitIdx = new ObservableInt();
        form.distanceUnitIdx.set(0);
        form.fromWhere = new ObservableField<>();
        form.fromWhere.set("Here");
        form.location = new ObservableField<>();
        form.location.set("");
        isLocationHere.set(true);
        form.keyword.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                getAutoComplete();
            }
        });
    }
    public ArrayAdapter<String> getAutoCompAdapter() {
        return autoCompAdapter;
    }
    private void refreshAutoComplete(List<String> list) {
        autoCompAdapter.clear();
        autoCompAdapter.addAll(list);
        autoCompAdapter.notifyDataSetChanged();
    }
    public void switchToHere() {
        form.fromWhere.set("Here");
        isLocationHere.set(true);
    }
    public void switchToOther() {
        form.fromWhere.set("Other");
        isLocationHere.set(false);
    }
    public void setAutoCompAdapter(ArrayAdapter<String> autoCompAdapter) {
        this.autoCompAdapter = autoCompAdapter;
    }
    public void getAutoComplete() {
        webService.autocomplete(form.keyword.get());
    }
    public void onSubmit() {
        if(form.isValid()) {
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
